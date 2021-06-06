package br.com.hkp.whatsappwebfix;

import static br.com.hkp.whatsappwebfix.global.Global.EMOJIS_DIRNAME;
import static br.com.hkp.whatsappwebfix.global.Global.FILENAME_DIFF;
import static br.com.hkp.whatsappwebfix.global.Global.PASTA_BASE;
import static br.com.hkp.whatsappwebfix.global.Global.TARGET_ABSOLUTE_PATHNAME;
import br.com.hkp.whatsappwebfix.util.FileTools;
import static br.com.hkp.whatsappwebfix.util.FileTools.writeTextFile;
import br.com.hkp.whatsappwebfix.util.Normalizer;
import java.io.File;
import java.io.IOException;
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
    Usado para localizar tags que inserem emojis
    */
    private static final Pattern EMOJI_TAG_PATTERN = 
        Pattern.compile
        (
            "<img src=\""
            + PASTA_BASE + "\\/"
            + "d5fceb6532643d0d84ffe09c40c481ecdf59e15a\\.gif.+?>"
        );
    
    /*
    Serah usado para localizar o atributo alt na tag que insere emojis. Do valor
    deste atribuido eh extraido o emoji codificado em UTF8.
    */
    private static final Pattern ALT_ATTR_PATTERN = 
        Pattern.compile("alt\\s*=\\s*\".+?\"");
    
    /*
    Aramazena todo o conteudo de um arquivo HTML que eh uma pagina zap salva
    */
    private String htmlContent;
  
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
        
        /*
        TARGET_ABSOLUTE_PATHNAME eh a string com o caminho absoluto do diretorio
        onde estao os arquivos HTML a serem corrigidos. O caminho eh absoluto
        porque essa aplicacao pode ser executada em qualquer diretorio, portanto
        este caminho nao pode ser relativo ao diretorio da aplicacao.
        */   
        String absoluteFileName = 
            TARGET_ABSOLUTE_PATHNAME + '/' + inputFile.getName();
             
        htmlContent = FileTools.readTextFile(file);//Le o arquivo a ser corrigido.
        
        /*
        O arquivo de saida - com a correcao da pag. zap - tem o nome do original
        acrescido do sufixo .fix, que eh informado pela constante FILENAME_DIFF
        */
        outputFile = 
            new File
                (
                    absoluteFileName.replace(".html", FILENAME_DIFF + ".html")
                );
        
        /*
        Cria objeto para arquivo de relatorio.
        */
        reportFile = new File(absoluteFileName.replace(".html",".report"));
        
        /*
        As linhas do arq. de relatorio inicialmente serao inseridas neste Hash
        */
        emojisReport = new HashMap<>(1000);
    }//construtor
           
    /*[01]---------------------------------------------------------------------
      
    -------------------------------------------------------------------------*/
    private String utf8EmojiToFilename(final String utf8Emoji)
    {
        /*
        O metodo recebe a string UTF8 do emoji na forma como ela aparece na pag.
        HTML do zap e retorna os codepoints do emoji no formato normalizado. 
        */
        String codepoints = Normalizer.utf8ToCodepoints(utf8Emoji);
        
        /*
        Este eh o nome do arquivo com o PNG do emoji. Este nome serah gravado
        na tag da pagina zap editada. O nome inclui a pasta onde o programa 
        espera encontrar o arquivo.
        */
        String filename = 
            PASTA_BASE + '/' + EMOJIS_DIRNAME + '/' + codepoints + ".png";
        
        /*
        Um objeto File para verificar se o arquivo existe ou nao no diretorio
        EMOJIS_DIRNAME
        */
        File test = new File(TARGET_ABSOLUTE_PATHNAME + '/' +filename);
               
        if (test.exists())//Se o arq. PNG existe o emoji pode ser renderizado
        {
            emojisReport.put
            (
                utf8Emoji,
                "Renderizado                : " + codepoints
            );
        }
        else//Se nao existe, tenta encontrar PNG do emoji raiz. Ou usa tag span.
        {
            filename = filename.replaceAll("-[-a-f0-9]+[.]png", ".png");
                               
            test = new File(TARGET_ABSOLUTE_PATHNAME + '/' + filename);
            
            if (test.exists())//Existe PNG do emoji raiz
            {
                emojisReport.put
                (
                    utf8Emoji,
                    "Renderizado com emoji raiz : " + codepoints
                );
                return ("!" + filename);//A exclamacao informa que eh emoji raiz
            }
            else//Terah que exibir o emoji como caractere com uma tag span
            {
                emojisReport.put
                (
                    utf8Emoji, 
                    "Faltando arquivo PNG       : " + codepoints
                );
                return null;
            }
        }//if-else
              
        return filename;
        
    }//utf8EmojiToFilename()
    
    /*[02]---------------------------------------------------------------------
    A partir da tag que insere emojis no arquivo original, este metodo retorna
    uma outra tag que irah inserir o emoji na copia alterada deste arquivo.
    -------------------------------------------------------------------------*/
    private String getNewTag(final String oldTag)
    {
        Matcher m = ALT_ATTR_PATTERN.matcher(oldTag);
        
        if (m.find())//Localiza o atributo alt na tag que insere emoji 
        {
            String utf8Emoji = m.group();
            
            /*
            Extrai o valor do atributo alt. Eh uma string com o emoji codificado
            em UTF-8
            */
            utf8Emoji = utf8Emoji.substring
                        (
                            utf8Emoji.indexOf('"') + 1, utf8Emoji.length() - 1
                        ).trim();
            
            String filename = utf8EmojiToFilename(utf8Emoji);
            
            if (filename == null)//Nao ha PNG para este emoji
            {
                return "<span>" + utf8Emoji + "</span>";
            }
            else if (filename.charAt(0) == '!')//So tem PNG para emoji raiz
            {
                return 
                    "<img src=\"" + filename.substring(1, filename.length()) +
                    "\" width=\"20px\" height=\"20px\"" +
                    "style=\"border: solid 1px red;\">";
            }
            else//Existe o PNG com a figura deste emoji
            {
                return 
                    "<img src=\"" + filename + "\" alt=\"" + utf8Emoji +
                    "\" width=\"20px\" height=\"20px\">";
            }
       
        } 
        else
            return oldTag;//O metodo falhou ao tentar construir a nova tag.
  
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
        HashMap<String, String> tagMap = getMap();
        
        /*
        Edita o conteudo do arquivo original.
        */
        for(String oldTag: tagMap.keySet())
            htmlContent = htmlContent.replace(oldTag, tagMap.get(oldTag));
        
        writeTextFile(outputFile, htmlContent);//Grava string editada no novo arq.
        
        /*---------------------------------------------------------------------
        Monta uma StringBuilder com todo o conteudo do arquivo de relatorio.
        As linhas deste arq. de relatorio sao obtidas das entradas previamente 
        inseridas no HashMap emojisReport
        ---------------------------------------------------------------------*/
        StringBuilder sb = new StringBuilder(emojisReport.size() * 100);
        
        for (String utf8Emoji: emojisReport.keySet())
        {
            sb.
            append(utf8Emoji).
            append(" : ").
            append(emojisReport.get(utf8Emoji)).
            append("\n");
        }
        /*--------------------------------------------------------------------*/
        
        writeTextFile(reportFile, sb.toString());//Grava relatorio
                
    }//createNewFile()
    
}//classe WhatsAppEditor