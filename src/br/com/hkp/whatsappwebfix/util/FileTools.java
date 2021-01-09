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
    /**
     * Le um arquivo no sistema de arquivos para um objeto String e o retorna.
     * Este arquivo deve estar codificado em UTF-8.
     * 
     * @param file O arquivo a ser lido.
     * 
     * @return Uma String com o conteudo do arquivo texto.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static String readTextFile(final File file) throws IOException
    {
        return 
            new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
       
    }//readTextFile()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Le um arquivo no sistema de arquivos para um objeto String e o retorna.
     * Este arquivo deve estar codificado em UTF-8.
     * 
     * @param filename O nome do arquivo a ser lido.
     * 
     * @return Uma String com o conteudo do arquivo texto.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static String readTextFile(final String filename) throws IOException
    {
        return readTextFile(new File(filename));
    }//readTextFile()
    
    /*[03]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Escreve o conteudo de uma String em um arquivo texto. Se o arquivo jah
     * existir seu conteudo serah substituido por esta String, e se nao existir
     * serah criado. A String deve ser UTF0.
     * 
     * @param file O arquivo.
     * 
     * @param content A String codificada em UTF-8.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static void writeTextFile(final File file, final String content)
        throws IOException
    {
        FileWriter  fw = new FileWriter(file, StandardCharsets.UTF_8);
               
        fw.write(content);
        
        fw.close();
  
    }//writeTextFile()
    
    /*[04]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Escreve o conteudo de uma String em um arquivo texto. Se o arquivo jah
     * existir seu conteudo serah substituido por esta String, e se nao existir
     * serah criado. A String deve ser UTF0.
     * 
     * @param filename O arquivo.
     * 
     * @param content A String codificada em UTF-8.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static void writeTextFile(final String filename, final String content)
        throws IOException
    {
        writeTextFile(new File(filename), content);
    }//writeTextFile()
    
    /*[05]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa um arquivo apontado por uma URL para um diretorio especificado que
     * jah deve indicar tambem o nome do arquivo que serah gravado.
     * 
     * @param url A url do arquivo a ser baixado.
     * 
     * @param pathname Caminho absoluto ou relativo para onde gravar o arquivo,
     * incluindo tambem o nome que serah dado ao arquivo baixado.
     * 
     * @throws IOException Em caso de erro de IO.
     */
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
    /**
     * Obtem apenas o nome de um arquivo a partir da sua URL.
     * 
     * @param url A URL. (A URL deve indicar um arquivo e nao um site somente)
     * 
     * @return O nome do arquivo.
     */
    public static String extractFilenameFromUrl(final String url)
    {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }//extractFilenameFromUrl()
     
    /*[07]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa um arquivo para um diretorio especificado. O nome do arquivo serah
     * mantido como o mesmo desta URL.
     * Por exemplo: https://www.nomeDeDominio/caminho/nomeArquivo.txt este 
     * arquivo seria gravado com nome "nomeArquivo.txt" no diretorio
     * especificado.
     * 
     * @param url A URL do arquivo.
     * 
     * @param path O caminho do diretorio onde gravar o arquivo. Pode ser 
     * relativo ou absoluto.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static void downloadUrl(final String url, final String path)
        throws IOException
    {
        String pathname = path + '/' + extractFilenameFromUrl(url);
        
        downloadUrl2Pathname(url, pathname);
               
    }//downloadUrl()
    
    /*[08]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Cria um subdiretorio no diretorio passado como parametro.
     * 
     * @param dir O diretorio onde serah criado o subdiretorio.
     * 
     * @param dirName Nome do subdiretorio.
     * 
     * @return Uma String com o caminho ABSOLUTO do diretorio que foi criado.
     * 
     * @throws IOException Se nao for possivel criar o subdiretorio.
     * 
     */
    public static String makeSubDir(final File dir, final String dirName)
        throws IOException
    {
        File subDir = new File(dir.getAbsolutePath()+ '/' + dirName);
            
        if ((!subDir.exists()) && (!subDir.mkdirs()))
            throw new IOException("Erro ao criar pasta " + dirName); 
       
        return subDir.getAbsolutePath();
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
    /**
     * Grava um arquivo de log por solicitacao de um objeto da classe Normalize.
     * 
     * @param frame O frame que exibe o progresso da atividade de um objeto da
     * classe Normalize. 
     * 
     * @param map Um TreeMap cujas entradas serao os registros gravados no 
     * arquivo de log.
     * 
     * @param path O caminho do diretorio onde gravar o arquivo de log.
     * 
     * @throws IOException Em caso de erro de IO.
     */
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
        /*
        Preve quanto de memoria a StringBuilder vai necessitar.
        */  
        StringBuilder sb = new StringBuilder(map.size() * 400);
        
        /*---------------------------------------------------------------------
         Monta uma StringBuilder com o conteudo que sera gravado no arquivo. 
         Isso evita ter que gravar linha por linha, agilizando o processo.
        ---------------------------------------------------------------------*/
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
        /*-------------------------------------------------------------------
           Se jah existir um arquivo de log previamente gravado com esse nome,
           entao gera outro nome acrescentando indices como sufixo ao nome.
        ----------------------------------------------------------------------*/
        String filename = path + "/_emoji-list.html";
        
        
        int count = 0;
        while (new File(filename).exists())
            filename = path + "/_emoji-list_" + ++count + ".html";
        /*--------------------------------------------------------------------*/
        
        writeTextFile(filename, sb.toString());//Grava o arquivo.
        
        frame.setTitle("");//Apaga o titulo "Gravando Arquivo de Log.." no frame
        
    }//writeLogFile()
    
}//classe FileTools
