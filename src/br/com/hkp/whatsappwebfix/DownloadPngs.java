package br.com.hkp.whatsappwebfix;

import br.com.hkp.whatsappwebfix.global.Global;
import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import javax.swing.JFrame;

/*****************************************************************************
 * Baixa os arquivos PNG com figuras de Emojis estilo WhatsApp no site da 
 * Emojipedia.
 * 
 * @since 27 de novembro de 2020 
 * @version 1.0
 * @author "Pedro Reis"
 ****************************************************************************/
public final class DownloadPngs
{
    private final ProgressFrame frame;
    
    private static final String TARGET_DIR = "png";
   
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe. Configura a janela que exibe o progresso do 
     * download.
     * 
     * O programa e encerrado com o fechamento desta janela.
     */
    public DownloadPngs()
    {
        frame = new ProgressFrame("Baixando...", 800, 450, JFrame.EXIT_ON_CLOSE);
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa os arquivos listados na pagina de WhatsApp emojis da Emojipedia.
     * 
     * @param emojipediaFile Arquivo com o fonte da pagina HTML da Emojipedia 
     * que lista os emojis do whatsapp. Nesta pagina ha a URL de cada arquivo
     * PNG com a figura de cada emoji utilizado pelo Whatsapp.
     * 
     * @throws IOException Em caso de erro de IO
     */
    public void downloadPngs(final File emojipediaFile) throws IOException
    {
      
        /*---------------------------------------------------------------------
        Cria o diretorio para onde serao baixados os arquivos, se este ainda nao
        existir
        ----------------------------------------------------------------------*/
        File downloaDir = new File(TARGET_DIR);
        
        if ((!downloaDir.exists()) && (!downloaDir.mkdirs()))
            throw new IOException("Erro ao criar pasta " + TARGET_DIR); 
        /*--------------------------------------------------------------------*/
        
        /*
        emojipediaFile eh o arquivo com o codigo fonte da pagina da Emojipedia.
        Esse arquivo eh lido para a String emojipediaPage
        */
        String emojipediaPage = FileTools.readTextFile(emojipediaFile);
        
        /*
        Na classe Global esta definida a regex que localiza na pag. da Emojipeia
        a URL dos arquivos que devem ser baixados
        */
        Matcher m = Global.PNG_PATTERN.matcher(emojipediaPage);
        
        frame.setVisible(true);//Abre a janela
        
        int count = 0;//Contador para os arquivos que sao baixados
        
        /*
        No loop while o objeto m localiza cada URL alvo na pagina e faz o 
        download do arquivo para o diretorio TARGET_DIR
        */
        while (m.find())
        {
            String url = m.group();//Localiza a URL de um arq. PNG
                       
            FileTools.downloadUrl(url, TARGET_DIR);//Baixa pra pasta TARGET_DIR
            
            frame.println(String.format("%04d - %s\n", ++count, url));
        }//while
                  
        frame.setTitle("");
        
        frame.println("Arquivos baixados para a pasta " + TARGET_DIR);
               
        java.awt.Toolkit.getDefaultToolkit().beep();//Alerta sonoro de termino
   
    }//downloadPngs()
    
}//classe DownloadPngs
