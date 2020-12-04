package br.com.hkp.whatsappwebfix;

import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DownloadJpgs
{
    private final ProgressFrame frame;
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe
     */
    public DownloadJpgs()
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
            /*
            Deleta o prefixo e strings amp; e as aspas no final
            */
            String url = 
                m.group().replace("href=\"", "").
                replace("amp;", "").
                replace("\"", "");
            
                        
            FileTools.downloadUrl(url, "jpg");
            
            frame.println(String.format("%04d - %s\n", ++count, url));
        }
 
    }//downloadAll()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa os arquivos listados na pagina
     * 
     * @param file o arquivo com as URLs
     * 
     * @throws IOException Em caso de erro de IO
     */
    public void downloadJpgs(final File file) throws IOException
    {
      
        File downloaDir = new File("jpg");
        
        if (!downloaDir.exists())
        {
            if (!downloaDir.mkdirs())
            {
                System.err.println("Erro ao criar pasta /jpg");
                System.exit(1);
            }
        } 
                       
        String contentFile = FileTools.readTextFile(file);
             
      /*---------------------------------------------------------------------*/
            
        frame.setVisible(true);
        
        /*
        regex para localizar nomes de arquivos PNG no atributo srcset
        */
        Pattern href = Pattern.compile("href=\"https.+?\"");
                  
        downloadAll(href.matcher(contentFile));//baixa os arquivos 
                     
        frame.setTitle("");
        
        frame.println("Arquivos baixados para a pasta jpg");
               
        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//downloadJpgs()
    
}//classe DownloadJpgs

