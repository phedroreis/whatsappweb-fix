package br.com.hkp.whatsappwebfix.util;

import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.TreeMap;

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
    public static void writeTextFile(final File file, final String content)
        throws IOException
    {
        FileWriter  fw = new FileWriter(file, StandardCharsets.UTF_8);
               
        fw.write(content);
        
        fw.close();
  
    }//writeTextFile()
    
    /*[04]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static void writeTextFile(final String filename, final String content)
        throws IOException
    {
        writeTextFile(new File(filename), content);
    }//writeTextFile()
    
    /*[05]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static void downloadUrl2Pathname
    (
        final String url, 
        final String pathname
    )
        throws IOException
    {
        URL download = new URL(url);

        ReadableByteChannel rbc = Channels.newChannel(download.openStream());
        
        FileOutputStream fos = new FileOutputStream(pathname);
        
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        
        fos.close();
        
    }//downloadUrlWithPathname()
    
    /*[06]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static String extractFilenameFromUrl(final String url)
    {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }//extractFilenameFromUrl()
     
    /*[07]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static void downloadUrl(final String url, final String path)
        throws IOException
    {
        String pathname = path + '/' + extractFilenameFromUrl(url);
        
        downloadUrl2Pathname(url, pathname);
               
    }//downloadUrl()
    
    /*[08]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static String makeSubDir(final File dir, final String dirName)
        throws IOException
    {
        File subDir = new File(dir.getAbsolutePath()+ '/' + dirName);
        
        boolean sucess = true;
        
        if (!subDir.exists()) sucess = subDir.mkdirs();
       
        if (sucess)
           return subDir.getAbsolutePath();
        else
           throw new IOException("Erro ao criar pasta " + dirName); 
        
    }//makeSubDir()
    
    /*-------------------------------------------------------------------------
    O arquivo de log eh no formato HTML. HEAD e FOOTER sao gravadas 
    respectivamente no inicio e no final desse arquivo
    -------------------------------------------------------------------------*/
    private static final String STYLE =
"  <style>\n" +
"    th\n" +
"    {\n" +    
"      padding:10px\n" +   
"    }\n" +    
"    td, th\n" + 
"    {\n" +
"      text-align: left;\n" +
"    }\n" + 
"    tr:nth-child(even)\n" +
"    {\n" +
"      background-color: #dddddd;\n" +
"    }\n" +
"  </style>\n";    
    
    public static final String HEAD = 
"<!DOCTYPE html>\n" +
"<html lang=\"pt-br\">\n" +
"<head>\n" +
"  <meta charset=\"UTF-8\">\n" +
"  <title>Log</title>\n" + STYLE +    
"</head>\n" +
"<body>\n" +
"  <table>\n" +
"    <tr>\n" +
"      <th>Emoji</th>\n" +    
"      <th>Nome Original</th>\n" +
"      <th>Normalizado</th>\n" +
"    </tr>\n" +
"    <tbody>\n";

    
    public static final String FOOTER = 
"    </tbody>\n" +    
"  </table>\n" +   
"</body>\n" +
"</html>";
    
    /*[09]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static void writeLogFile
    (
        final ProgressFrame frame, 
        final TreeMap<String, String> map,
        final String path
    ) 
        throws IOException
    {
        frame.setTitle("Gravando arquivo de log...");
        
        int barValue = 0;
           
        StringBuilder sb = new StringBuilder(map.size() * 400);
        sb.append(HEAD).append("\n");
            
        for(String filename: map.keySet())
        {
            String normalizedFilename = extractFilenameFromUrl(map.get(filename));
            filename = extractFilenameFromUrl(filename);
            sb.append("    <tr>\n");
            sb.append("        <td><img src=\"").append(normalizedFilename).
            append("\"></td>\n");
            sb.append("        <td>").append(filename).append("</td>\n");
            sb.append("        <td>").append(normalizedFilename).
            append("</td>\n");
            sb.append("    </tr>\n");
            frame.setProgressBarValue(++barValue);
        }//for 
        
        sb.append(FOOTER).append("\n");
        
        String filename = path + "/_emoji-list.html";
        
        int count = 0;
        while (new File(filename).exists())
            filename = path + "/_emoji-list_" + ++count + ".html";
    
        
        writeTextFile(filename, sb.toString());
        
        frame.setTitle("");
        
    }//writeLogFile()
    
}//classe FileTools
