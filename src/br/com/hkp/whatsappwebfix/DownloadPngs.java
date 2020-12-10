package br.com.hkp.whatsappwebfix;

import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Construtor da classe
     */
    public DownloadPngs()
    {
        frame = new ProgressFrame("Baixando...", 800, 450);
    }//construtor
   
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    private void downloadAll(final Matcher m) throws IOException
    {
        int count = 0;
        
        while (m.find())
        {
            String s = m.group();
            
            String url = s.substring(s.indexOf("https:"), s.length());
            
            FileTools.downloadUrl(url, TARGET_DIR);
            
            frame.println(String.format("%04d - %s\n", ++count, url));
        }
 
    }//downloadAll()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa os arquivos listados na pagina de WhatsApp emojis da Emojipedia.
     * 
     * @param emojipediaFile Arquivo com o fonte da pagina HTML da Emojipedia 
     * que lista os emojis do whatsapp
     * 
     * @throws IOException Em caso de erro de IO
     */
    public void downloadPngs(final File emojipediaFile) throws IOException
    {
      
        File downloaDir = new File(TARGET_DIR);
        
        if (!downloaDir.exists())
        {
            if (!downloaDir.mkdirs())
            {
                System.err.println("Erro ao criar pasta /" + TARGET_DIR);
                System.exit(1);
            }
        } 
                       
        String contentFile = FileTools.readTextFile(emojipediaFile);
             
      /*---------------------------------------------------------------------*/
            
        frame.setVisible(true);
        
        /*
        regex para localizar nomes de arquivos PNG no atributo srcset
        */
        Pattern srcSet = 
            Pattern.compile(" srcset=\"https.+?\\." + TARGET_DIR + "\\b");
        
        Matcher m = srcSet.matcher(contentFile);
        
        downloadAll(m);//baixa os arquivos que regex localizar
        
        /*
        regex para localizar nomes de arquivos PNG no atributo data-src
        */
        Pattern dataSrc = 
            Pattern.compile("data-src=\"https.+?\\." + TARGET_DIR + "\\b");
        
        m = dataSrc.matcher(contentFile);
        
        downloadAll(m);//baixa os arqs. que regex localizar
        
        frame.setTitle("");
        
        frame.println("Arquivos baixados para a pasta " + TARGET_DIR);
               
        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//downloadPngs()
    
}//classe DownloadPngs
