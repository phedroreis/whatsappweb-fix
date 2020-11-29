package br.com.hkp.whatsappwebfix.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/******************************************************************************
 * A classe fornece metodos utilitarios para ler ou gravar arquivos.
 * 
 * @since 27 de novembro de 2020 v1.0
 * @version 1.0
 * @author "Pedro Reis"
 *****************************************************************************/
public final class FileTools
{
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static String readTextFile(final File file) throws IOException
    {
        return 
            new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
       
    }//readTextFile()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static String readTextFile(final String filename) throws IOException
    {
        return readTextFile(new File(filename));
    }//readTextFile()
     
    /*[03]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static void downloadUrl(final String url, final String path)
        throws IOException
    {
         String filename = 
                url.substring(url.lastIndexOf('/') + 1, url.length());
         
        URL download = new URL(url);

        ReadableByteChannel rbc = Channels.newChannel(download.openStream());
        
        FileOutputStream fos = new FileOutputStream(path + '/' + filename);
        
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        
        fos.close();
        
    }//downloadUrl()
    
}//classe FileTools
