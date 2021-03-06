package br.com.hkp.whatsappwebfix;

import br.com.hkp.whatsappwebfix.global.Global;
import static br.com.hkp.whatsappwebfix.global.Global.EMOJIS_DIRNAME;
import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import br.com.hkp.whatsappwebfix.util.Normalizer;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*****************************************************************************
 * Baixa os arquivos PNG com figuras de Emojis estilo WhatsApp no site da 
 * Emojipedia. Somente aqueles que ainda nao foram baixados.
 * 
 * @since 4 de janeiro de 2021 
 * @version 1.0
 * @author "Pedro Reis"
 ****************************************************************************/
public final class Updater
{
    private final ProgressFrame frame;
    
    private final String emojisAbsoluteDirName;
    
    private final AtomicBoolean abort;
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe
     * 
     * @param pastaBase PastaBase com arquivos comuns
     * 
     * @param closeOperation O tipo de operacao quando o botao de fechar eh 
     * clicado.
     * 
     * @throws IOException Se o diretorio para baixar os emojis nao puder ser 
     * criado
     */
    public Updater(final File pastaBase, final int closeOperation) 
        throws IOException
    {
        /*
        Quando um objeto desta classe eh criado pelo app Update, o programa deve
        se encerrar com o fechamento desta janela. Mas quando o app FixGui usa
        um objeto dessa classe, o download deve ocorrer em segundo plano em 
        relacao ao programa principal. Portanto nesse caso closeOperation apenas
        fecha e libera a memoria do objeto frame, sem encerrar a aplicacao.
        */
        frame = 
            new ProgressFrame("Pesquisando...", 800, 450, closeOperation);
        
        if (closeOperation == JFrame.DO_NOTHING_ON_CLOSE)
            frame.addWindowListener(new CloseWindowHandler());
        
        abort = new AtomicBoolean(false);
        
        emojisAbsoluteDirName = FileTools.makeSubDir(pastaBase, EMOJIS_DIRNAME);
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa os arquivos listados na pagina de WhatsApp emojis da Emojipedia que
     * a pasta emoji-images ainda nao tenha.
     * 
     * 
     * @throws IOException Em caso de erro de IO
     */
    public void downloadPngs() throws IOException
    {
        frame.setVisible(true);
        
        frame.println
        (
            "\nProcurando por novos emojis na Emojipedia. Aguarde..."
        );
        
        /*
        Baixa o fonte da pag. da Emojipedia com as URLs dos PNGs de figuras de 
        emojis
        */
        FileTools.downloadUrl
        (
            "https://emojipedia.org/whatsapp", emojisAbsoluteDirName
        );
         
        /*
        emojipediaFile eh um objeto de referencia ao arquivo que foi baixado
        */
        File emojipediaFile = new File(emojisAbsoluteDirName + "/whatsapp");
        
        /*
        Le o conteudo deste arquivo para a String emojipediaPage
        */
        String emojipediaPage = FileTools.readTextFile(emojipediaFile);
            
        /*
        Deleta o arquivo, jah que nao serah mais necessario
        */
        emojipediaFile.delete();
        
        if (abort.get()) return;//A janela foi fechada pelo usuario
        
        /*
        Na classe Global esta declarada a regex que localiza URLs de PNGs nesta
        pagina da Emojipedia
        */
        Matcher m = Global.PNG_PATTERN.matcher(emojipediaPage);
        
        /*
        Essa estrutura serve para associar cada URL localizada ao nome que sera
        dado ao arquivo que foi baixado com essa mesma URL. O nome do arquivo
        serah normalizado.
        */
        TreeMap<String, String> mapUrl2filename = new TreeMap<>();
       
        while (m.find())
        {
            if (abort.get()) return;//A janela foi fechada pelo usuario
            
            String url = m.group();//Localiza a URL de um PNG
            
            /*
            Eh obtido o nome normalizado que o arquivo linkado por esta URL 
            devera receber.
            */
            String pathname = 
                emojisAbsoluteDirName + '/' +
                Normalizer.
                filenameToCodepoints(FileTools.extractFilenameFromUrl(url)) +
                ".png";
            
            /*
            Apenas arquivos que ainda nao existem no diretorio emoji-images 
            serao inlucido no TreeMap para serem baixados.
            */
            if (!(new File(pathname).exists()))
                mapUrl2filename.put(url, pathname);
           
        }//while
        
        int newEmojis = mapUrl2filename.size();//Quantos arquivos serao baixados
        
        if (newEmojis == 0) //Se nenhuma entrada foi inserida no TreeMap
        {
            frame.setTitle("Nada a Fazer Desta Vez");
            
            frame.println
            (
                "Nenhum novo emoji para baixar. Biblioteca atualizada."
            );
        }
        else //Se foram encontrados novos arquivos PNG na EmojiPedia
        {
            frame.setTitle("Baixando...");
            
            /*
            Configura barra de progresso com num. de arqs. que serao baixados.
            */
            frame.setProgressBarVisible(newEmojis);
            
            /*-----------------------------------------------------------------
            Informa na janela qtos. arqs. serao baixados. Se apenas 1, a msg eh
            escrita no singular. Mais de um acrescenta S no final dos substant.
            ------------------------------------------------------------------*/
            String s = (newEmojis > 1) ? "s" : "";   
            
            frame.println
            (
                String.format
                (
                    "%s%s %d %s%s %s%s.", 
                    "Encontrado", s, newEmojis, "novo", s, "emoji", s
                )
            );
            /*----------------------------------------------------------------*/

            int count = 0;
            
            /*-----------------------------------------------------------------
                  As URLs de arquivos que foram localizadas sao baixadas
            ------------------------------------------------------------------*/
            for(String url: mapUrl2filename.keySet())
            {
                if (abort.get()) return;//A janela foi fechada pelo usuario
                
                /*
                Esse metodo estatico da classe FileTools baixa o arquivo e o
                nomeia com o nome indicado.
                */
                FileTools.downloadUrl2Pathname(url, mapUrl2filename.get(url));

                frame.println(String.format("%04d - %s\n", ++count, url));

                frame.setProgressBarValue(count);//Atualiza barra de progresso
            }//for
            /*---------------------------------------------------------------*/
              
            frame.setTitle("");

            frame.println
            (
                String.format
                (
                    "%s%s %s%s %s %s.", 
                    "Arquivo", s, "baixado", s, "para a pasta", 
                    emojisAbsoluteDirName
                )
            );
            
            if (abort.get()) return;//A janela foi fechada pelo usuario
            
            /*
            Um arquivo de log eh gravado com as entradas do TreeMap. Se o 
            processo for abortado pelo fechamento da janela, o arquivo de log 
            nao eh gravado.
            */
            FileTools.writeLogFile(frame, mapUrl2filename, emojisAbsoluteDirName);
            
        }//if-else
                 
        java.awt.Toolkit.getDefaultToolkit().beep();//Beepa termino
        
        abort.set(true);
     
    }//downloadPngs()
    
    /*=========================================================================
    *            Classes interna. Handler do fechamento da janela.
    ==========================================================================*/
    /*-------------------------------------------------------------------------
                    Encerra o update ao fechamento da janela
     -------------------------------------------------------------------------*/
    private final class CloseWindowHandler extends WindowAdapter
    {
        private final String[] options = {"Aborta", "Cancela"};
        
        /*[01]------------------------------------------------------------------
        
        ----------------------------------------------------------------------*/
        /**
         * Encerra o programa se nenhuma thread estiver executando.
         * 
         * @param e n/a
         */
        @Override
        public void windowClosing(WindowEvent e)
        {
            if (!abort.get())
            {
                if 
                (
                    JOptionPane.showOptionDialog
                    (
                        frame, "Confirma abortar?", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.
                        WARNING_MESSAGE,
                        null, options, options[1]
                    ) != 0
                ) return;
                
                abort.set(true);
            }//if
            
            frame.dispose();
        }//windowClosing()
           
    }//classe CloseWindow
    
}//classe Updater

