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
    private void downloadAll(final Matcher m, final String toDelete) 
        throws IOException
    {
        int count = 0;
        
        while (m.find())
        {
            String url = m.group().replace(toDelete, "");
            
            FileTools.downloadUrl(url, "png");
            
            frame.println(String.format("%04d - %s\n", ++count, url));
        }
        
        System.out.println("");
        
    }//downloadAll()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa os arquivos listados na pagina de WhatsApp emojis da Emojipedia.
     * 
     * @param emojipediaFile
     * @throws IOException Em caso de erro de IO
     */
    public void downloadPngs(final File emojipediaFile) throws IOException
    {
      
        File downloaDir = new File("png");
        
        if (!downloaDir.exists())
        {
            if (!downloaDir.mkdirs())
            {
                System.err.println("Erro ao criar pasta /png");
                System.exit(1);
            }
        } 
                       
        String contentFile = FileTools.readTextFile(emojipediaFile);
             
      /*---------------------------------------------------------------------*/
            
        frame.setVisible(true);
        
        /*
        regex para localizar nomes de arquivos PNG no atributo srcset
        */
        Pattern srcSet = Pattern.compile(" srcset=\"https.+?\\.png\\b");
        
        Matcher m = srcSet.matcher(contentFile);
        
        downloadAll(m, " srcset=\"");//baixa os arquivos que regex localizar
        
        /*
        regex para localizar nomes de arquivos PNG no atributo data-src
        */
        Pattern dataSrc = Pattern.compile("data-src=\"https.+?\\.png\\b");
        
        m = dataSrc.matcher(contentFile);
        
        downloadAll(m, "data-src=\"" );//baixa os arqs. que regex localizar
        
        frame.setTitle("");
        
        frame.println("Arquivos baixados para a pasta png");
               
        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//downloadPngs()
    
}//classe DownloadPngs
