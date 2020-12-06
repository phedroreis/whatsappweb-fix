package br.com.hkp.whatsappwebfix;

import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DownloadJpgs
{
    private final ProgressFrame frame;
    
    private int count;
       
    private static final String TARGET_DIR = "jpg";
    
    private static final String EXT = "." + TARGET_DIR;
    
    private FileOutputStream fos;
    
    private final HashMap<String, Integer> filenameIndexMap;
    
    private final HashSet<String> urlSet;
    
     
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe
     */
    public DownloadJpgs()
    {
        frame = new ProgressFrame("Baixando...", 800, 450);
        
        count = 0;
               
        urlSet = new HashSet<>(2000);
        
        filenameIndexMap = new HashMap<>(2000);
     
        try 
        {
           fos = new FileOutputStream("err.txt");
           final PrintStream ps = new PrintStream(fos);
           System.setErr(ps);
        } 
        catch(final FileNotFoundException e) 
        {
           System.err.println("Falha ao criar err.txt");
        }
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    private void downloadUrl(final String url) 
    {
        if (!urlSet.add(url)) return;
        
        int start = url.lastIndexOf('/');
        
        int extPosition = url.indexOf(EXT, start);
        
        if (extPosition == -1) return;
        
        try
        {
            String filename = url.substring(start, extPosition + 4);
            
            Integer index = filenameIndexMap.put(filename, 0);
            
            if (index != null)
            {
                filenameIndexMap.put(filename, ++index);
                filename = filename.replace(EXT, "_" + index + EXT);
            }
            
            URL download = new URL(url);

            ReadableByteChannel rbc = 
                Channels.newChannel(download.openStream());

            FileOutputStream thisFos = 
                new FileOutputStream(TARGET_DIR + filename);

            thisFos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            thisFos.close();
            
            frame.println(String.format("%04d - %s\n", ++count, url));
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
               
    }//downloadUrl()
   
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    private void downloadHrefs(final Matcher m) 
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
            
            downloadUrl(url);
        }
 
    }//downloadHrefs()
    
    /*[03]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    private void downloadHttps(final Matcher m) 
    {
               
        while (m.find())
        {
            String url = m.group().replace("\\/","/").replace("\"", "");
            
            downloadUrl(url);
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
                System.err.println("Erro ao criar pasta /" + TARGET_DIR);
                System.exit(1);
            }
        } 
                       
        String contentFile = FileTools.readTextFile(file);
             
      /*---------------------------------------------------------------------*/
            
        frame.setVisible(true);
        
        /*
        regex para localizar nomes de arquivos jpg em tags img
        */
        Pattern href = 
            Pattern.compile("href=\"https.+?[.]" + TARGET_DIR + ".+?\"");
                  
        downloadHrefs(href.matcher(contentFile));//baixa os arquivos 
        
        /*
        regex para localizar nomes de arquivos jpg em scripts
        */
        Pattern http = 
            Pattern.compile
            (
                "https:\\\\/\\\\/scontent.+?([\"\\],{}]|[.]" + TARGET_DIR + ")"
                + ".+?[\"\\],}]"
            );
                  
        downloadHttps(http.matcher(contentFile));//baixa os arquivos 
                     
        frame.setTitle("");
        
        frame.println("Arquivos baixados para a pasta " + TARGET_DIR);
        
        fos.close();
               
        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//downloadJpgs()
    
}//classe DownloadJpgs

