package br.com.hkp.whatsappwebfix.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
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
public class DownloadPngs
{
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    private static void download(final Matcher m, final String toDelete) 
        throws IOException
    {
        int count = 0;
        
        while (m.find())
        {
            String url = m.group().replace(toDelete, "");
            
            String filename = 
                url.substring(url.lastIndexOf('/') + 1, url.length());
            
            URL download = new URL(url);
            
            ReadableByteChannel rbc = 
                Channels.newChannel(download.openStream());
            FileOutputStream fos = new FileOutputStream("png/" + filename);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            
            System.out.printf("%04d - %s\n", ++count, filename);
        }
        
        System.out.println("");
        
    }//download()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa os arquivos listados na pagina de WhatsApp emojis da Emojipedia.
     * 
     * @throws IOException Em caso de erro de IO
     */
    public static void downloadPngs() throws IOException
    {
        /*------------------------------------------------------------------
        Le o arquivo whatsapp-emojis.html para um array de chars e ao final
        converte este array para String
        ------------------------------------------------------------------*/
               
        File emojipediaFile = new File("whatsapp-emojis.html");
        
        if (!emojipediaFile.exists()) 
        {
            System.err.println("Faltando arquivo whatsapp-emojis.html");
            System.exit(1);
        }
        
        File downloaDir = new File("png");
        
        if (!downloaDir.exists())
        {
            if (!downloaDir.mkdirs())
            {
                System.err.println("Erro ao criar pasta /png");
                System.exit(1);
            }
        }
        
        
        int length = (int)emojipediaFile.length();
        
        char[] buffer = new char[length];
        
        BufferedReader bf =
            new BufferedReader
            (
                new FileReader(emojipediaFile, StandardCharsets.UTF_8),
                length
            );
        
        bf.read(buffer);
        
        bf.close();
       
        String contentFile = String.valueOf(buffer);
        
      /*---------------------------------------------------------------------*/
        
        
        /*
        regex para localizar nomes de arquivos PNG no atributo srcset
        */
        Pattern srcSet = Pattern.compile(" srcset=\"https.+?\\.png\\b");
        
        Matcher m = srcSet.matcher(contentFile);
        
        download(m, " srcset=\"");//baixa todos os arquivos que regex localizar
        
        /*
        regex para localizar nomes de arquivos PNG no atributo data-src
        */
        Pattern dataSrc = Pattern.compile("data-src=\"https.+?\\.png\\b");
        
        m = dataSrc.matcher(contentFile);
        
        download(m, "data-src=\"" );//baixa todos os arqs. que regex localizar
   
    }//downloadPngs()
    
}//classe DownloadPngs
