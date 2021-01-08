package br.com.hkp.whatsappwebfix;

import br.com.hkp.whatsappwebfix.global.Global;
import static br.com.hkp.whatsappwebfix.global.Global.EMOJIS_DIRNAME;
import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import br.com.hkp.whatsappwebfix.util.Normalizer;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;

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
   
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe
     * 
     * @param pastaBase PastaBase com arquivos comuns
     * 
     * @param closeOperation Indica o que fazer ao fechar a janela 
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
        
        File emojipediaFile = new File(emojisAbsoluteDirName + "/whatsapp");
        
        /*
        Le o conteudo deste arquivo para a String emojipediaPage
        */
        String emojipediaPage = FileTools.readTextFile(emojipediaFile);
        
        /*
        Deleta o arquivo, jah que nao serah mais necessario
        */
        emojipediaFile.delete();
        
        /*
        Na classe Global esta declarada a regex que localiza URLs de PNGs nesta
        pagina da Emojipedia
        */
        Matcher m = Global.PNG_PATTERN.matcher(emojipediaPage);
        
        /*
        Essa estrutura serve para associar cada URL localizada ao nome que sera
        dado ao arquivo que foi baixado com essa mesma URL. O nome do arquivo
        serah normalizado
        */
        TreeMap<String, String> mapUrl2filename = new TreeMap<>();
       
        while (m.find())
        {
            String url = m.group();
            
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
        
        int newEmojis = mapUrl2filename.size();
        
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
        
            frame.setProgressBarVisible(newEmojis);
            
            String s = (newEmojis > 1) ? "s" : "";   
            
            frame.println
            (
                String.format
                (
                    "%s%s %d %s%s %s%s.", 
                    "Encontrado", s, newEmojis, "novo", s, "emoji", s
                )
            );

            int count = 0;
            
            /*
            As URLs de arquivos que foram localizadas sao baixadas
            */
            for(String url: mapUrl2filename.keySet())
            {
                FileTools.downloadUrl2Pathname(url, mapUrl2filename.get(url));

                frame.println(String.format("%04d - %s\n", ++count, url));

                frame.setProgressBarValue(count);
            }//for
              
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
            
            /*
            Um arquivo de log eh gravado com as entradas do TreeMap
            */
            FileTools.writeLogFile(frame, mapUrl2filename, emojisAbsoluteDirName);
            
        }//if-else
                 
        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//downloadPngs()
    
}//classe Updater

