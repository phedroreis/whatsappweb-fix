package br.com.hkp.whatsappwebfix;

import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DownloadJpgs
{
    private final ProgressFrame frame;
    
    private int count;
    
    private static final String TARGET_DIR = "jpg";
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe
     */
    public DownloadJpgs()
    {
        frame = new ProgressFrame("Baixando...", 800, 450);
        
        count = 0;
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    private void downloadUrl(final String url) throws IOException
    {
        int start = url.lastIndexOf('/');
        
        String filename = url.substring(start, url.indexOf(".jpg", start) + 4);
        
        URL download = new URL(url);
  
        ReadableByteChannel rbc = Channels.newChannel(download.openStream());
        
        FileOutputStream fos = new FileOutputStream(TARGET_DIR + filename);
        
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        
        fos.close();
           
    }//downloadUrl()
   
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    private void downloadHrefs(final Matcher m) throws IOException
    {
                
        while (m.find())
        {
            /*
            Deleta o prefixo e strings amp; e as aspas no final
            */
            String url = 
                m.group().replace("href=\"", "").
                replace("amp;", "").
                replace("\"", "");
            
            try
            {
                downloadUrl(url);

                frame.println(String.format("%04d - %s\n", ++count, url));
            }
            catch (IOException ex)
            {
                System.out.println(ex);
            }
        }
 
    }//downloadHrefs()
    
    /*[03]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    private void downloadHttps(final Matcher m) throws IOException
    {
               
        while (m.find())
        {
            String url = m.group().replace("\\/","/").replace("\"", "");
            
            try
            {
                downloadUrl(url);
                
                frame.println(String.format("%04d - %s\n", ++count, url));
            }
            catch (IOException ex)
            {
                System.err.println(ex);
            }
        }
 
    }//downloadHttps()
    
    /*[04]---------------------------------------------------------------------
    
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
      
        File downloaDir = new File(TARGET_DIR);
        
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
        regex para localizar nomes de arquivos jpg em tags img
        */
        Pattern href = Pattern.compile("href=\"https.+?[.]jpg.+?\"");
                  
        downloadHrefs(href.matcher(contentFile));//baixa os arquivos 
        
        /*
        regex para localizar nomes de arquivos jpg em scripts
        */
        Pattern http = Pattern.compile("https:\\\\/\\\\/.+?[.]jpg.+?\"");
                  
        downloadHttps(http.matcher(contentFile));//baixa os arquivos 
                     
        frame.setTitle("");
        
        frame.println("Arquivos baixados para a pasta jpg");
               
        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//downloadJpgs()
    
}//classe DownloadJpgs

