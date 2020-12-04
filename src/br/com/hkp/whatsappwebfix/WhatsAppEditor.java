package br.com.hkp.whatsappwebfix;

import static br.com.hkp.whatsappwebfix.global.Global.EMOJIS_DIRNAME;
import static br.com.hkp.whatsappwebfix.global.Global.FILENAME_DIFF;
import static br.com.hkp.whatsappwebfix.global.Global.PASTA_BASE;
import br.com.hkp.whatsappwebfix.util.FileTools;
import br.com.hkp.whatsappwebfix.util.Normalizer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/******************************************************************************
 * Esta classe tem todos os metodos para criar uma copia editada de uma pagina
 * salva com conversas no app WhatsApp Web.
 * <p>
 * Esta copia deve ser capaz de exibir os emojis inseridos pelo usuario. Ao 
 * contrario do que ocorrem com as paginas originalmente salvas.
 * 
 * @author "Pedro Reis"
 * @since 29 de novembro de 2020 v1.0
 * @version v1.0
 *****************************************************************************/
public final class WhatsAppEditor
{
    /*
    Localiza o atributo alt na tag que insere emojis
    */
    private static final Pattern ALT_ATTR_PATTERN = 
        Pattern.compile("alt\\s*=\\s*\".+?\"");
    
    /*
    Localiza tags que inserem emojis
    */
    private static final Pattern EMOJI_TAG_PATTERN = 
        Pattern.compile
        (
            "<img src=\""
            + PASTA_BASE + "\\/"
            + "d5fceb6532643d0d84ffe09c40c481ecdf59e15a\\.gif.+?>"
        );
    
    /*
    Aramazena todo o conteudo de um arquivo HTML
    */
    private String htmlContent;
    
    /*
    buffer para otimizar processos de leitura e gravacao
    */
    private final int buffer;
    
    /*
    O diretorio onde estao os arquivos HTML
    */
    private static String absolutePath;
   
    /*
    Arquivo que serah lido e arquivo que sera gravado e arquivo de relatorio
    */
    private final File inputFile;
    private final File outputFile;
    private final File reportFile;
    
    /*
    Um HashMap para conter entradas que associam um emoji UTF-8 a sua string de
    codepoints juntamente com a informacao sobre como o emoji foi renderizado
    pelo programa. As entradas desse HashMap serao gravadas em um arquivo texto.
    */
    private final HashMap<String, String> emojisReport;
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param file O arquivo que deve ser processado para gerar uma copia com as
     * tags que inserem emojis editadas.
     * 
     * @throws java.io.IOException Em caso de erro de IO
     */
    public WhatsAppEditor(final File file) throws IOException
    {
        inputFile = file;
            
        absolutePath = inputFile.getParent() + '/';
        
        String absoluteFileName = absolutePath + inputFile.getName();
             
        buffer = (int)inputFile.length();
        
        htmlContent = FileTools.readTextFile(file);
        
        outputFile = 
            new File
                (
                    absoluteFileName.replace(".html", FILENAME_DIFF + ".html")
                );
        
        reportFile = new File(absoluteFileName.replace(".html",".report"));
        
        emojisReport = new HashMap<>(1000);
    }//construtor
           
    /*[01]---------------------------------------------------------------------
      
    -------------------------------------------------------------------------*/
    /**
     * Recebe uma string codificada em utf8 que eh o emoji a ser exibido e que
     * foi lida do valor do atributo alt. Na tag que insere emojis na pagina.
     *
     * Esta string eh utilizada para encontrar o nome do arquivo que tem a 
     * figura correspondente a este emoji.
     * 
     * @param utf8Emoji Um emoji codificado em utf8
     * 
     * @return O nome que deve ter o arquivo PNG com a imagem deste emoji
     */
    public String utf8EmojiToFilename(final String utf8Emoji)
    {
        String codepoints = Normalizer.utf8ToCodepoints(utf8Emoji);
               
        String filename = 
            PASTA_BASE + '/' + EMOJIS_DIRNAME + '/' + codepoints + ".png";
        
        File test = new File(absolutePath + filename);
               
        if (!test.exists())
        {
            filename = filename.replaceAll("-[-a-f0-9]+[.]png", ".png");
                               
            test = new File(absolutePath + filename);
            
            if (!test.exists())
            {
                emojisReport.put
                (
                    utf8Emoji, 
                    codepoints + " : Faltando arquivo PNG"
                );
                return null;
            }
            else
            {
                emojisReport.put
                (
                    utf8Emoji,
                    codepoints + " : Renderizado com emoji raiz"
                );
                return ("!" + filename);
            }
        }
        else
            emojisReport.put(utf8Emoji, codepoints + " : Renderizado");
        
        return filename;
        
    }//utf8EmojiToFilename()
    
    /*[02]---------------------------------------------------------------------
    A partir da tag que insere emojis no arquivo original, este metodo retorna
    uma outra tag que irah inserir o emoji na copia alterada deste arquivo.
    -------------------------------------------------------------------------*/
    private String getNewTag(final String oldTag)
    {
        Matcher m = ALT_ATTR_PATTERN.matcher(oldTag);
        
        if (m.find()) 
        {
            String utf8Emoji = m.group();
            
            utf8Emoji = utf8Emoji.substring
                        (
                            utf8Emoji.indexOf('"') + 1, utf8Emoji.length() - 1
                        ).trim();
            
            String filename = utf8EmojiToFilename(utf8Emoji);
            
            if (filename == null)
            {
                return "<span>" + utf8Emoji + "</span>";
            }
            else if (filename.charAt(0) == '!')
            {
                return 
                    "<img src=\"" + filename.substring(1, filename.length()) +
                    "\" width=\"20px\" height=\"20px\"" +
                    "style=\"border: solid 1px red;\">";
            }
            else
            {
                return 
                    "<img src=\"" + filename + "\" alt=\"" + utf8Emoji +
                    "\" width=\"20px\" height=\"20px\">";
            }
       
        } 
        else
            return oldTag;
  
    }//getNewTag()
    
    /*[03]---------------------------------------------------------------------
      Retorna um HashMap associando cada tag que foi encontrada no arquivo
      original a tag que deve substitui-la na copia editada deste arquivo.
    -------------------------------------------------------------------------*/
    private HashMap<String, String> getMap() throws IOException
    {
        Matcher m = EMOJI_TAG_PATTERN.matcher(htmlContent);
              
        HashMap<String, String> map = new HashMap<>(1000);
        
        while (m.find()) map.put(m.group(), getNewTag(m.group()));
       
        return map;
   
    }//getMap()
      
    /*[04]---------------------------------------------------------------------
        
    -------------------------------------------------------------------------*/
    /**
     * Grava um novo arquivo. Neste arquivo os emojis sao renderizados.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public void createNewFile() throws IOException
    {
        BufferedWriter htmlFile = 
            new BufferedWriter
            (
                new FileWriter(outputFile, StandardCharsets.UTF_8), buffer
            );
                        
        HashMap<String, String> tagMap = getMap();
        
        for(String oldTag: tagMap.keySet())
            htmlContent = htmlContent.replace(oldTag, tagMap.get(oldTag));
       
        htmlFile.write(htmlContent);
        
        htmlFile.close();
        
        BufferedWriter report = 
            new BufferedWriter
            (
                new FileWriter(reportFile, StandardCharsets.UTF_8), buffer
            );
        
        StringBuilder sb = new StringBuilder(emojisReport.size() * 100);
        
        for (String utf8Emoji: emojisReport.keySet())
        {
            sb.
            append(utf8Emoji).
            append(" : ").
            append(emojisReport.get(utf8Emoji)).
            append("\n");
        }
        
        report.write(sb.toString());
        
        report.close();
        
    }//createNewFile()
    
}//classe WhatsAppEditor