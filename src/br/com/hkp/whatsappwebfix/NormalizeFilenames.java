package br.com.hkp.whatsappwebfix;

import static br.com.hkp.whatsappwebfix.global.Global.EMOJIS_DIRNAME;
import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import br.com.hkp.whatsappwebfix.util.Normalizer;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.TreeMap;
import javax.swing.JFrame;

/******************************************************************************
 * O conjunto de arquivos PNG que se obtem na pagina 
 * https://emojipedia.org/whatsapp/2.19.352 sao as figuras de emojis que serao
 * inseridas nas paginas salvas do app WhatsApp Web.
 * <p>
 * Nestas paginas ha tags <b>img</b> com funcao de inserir tais figuras onde um 
 * atributo <b>alt</b> tem por valor uma string de caracteres indicando o emoji 
 * a ser inserido. Representando este emoji pela string de codigos utf8 que o 
 * definem.
 * <p>
 * Contudo, nas paginas salvas, por alguma razao estas tags nao funcionam. 
 * Diferentemente do que ocorre quando estas mesmas paginas sao renderizadas 
 * online pelo navegador.
 * <p>
 * Portanto o objetivo deste projeto eh produzir uma ferramenta de software que 
 * edite estas tags, fazendo com que insiram as figuras dos emojis nas paginas 
 * salvas pelo usuario e abertas em modo offline. Emojis estes que sao 
 * referenciados nas tags por uma string de caracteres codificados em utf8.
 * <p>
 * Logo, a ferramenta deve localizar o arquivo com a figura relativa a este 
 * emoji por esta string do atributo alt. Para que a tag seja editada pela 
 * ferramenta fazendo-a inserir a figura no arquivo de imagem no disco.
 * Arquivo este que terah por nome uma string de codepoints analoga a string 
 * utf8 do atributo alt na tag que serah editada.
 * <p>
 * Entao a funcao deste programa eh renomear todos estes arquivos obtidos da 
 * pagina https://emojipedia.org/whatsapp/2.19.352 para que estes nomes sejam
 * uma string de codepoints que representem o emoji que corresponde a imagem no
 * arquivo.
 * <p>
 * Ou seja, ira converter o nome de um arquivo com a imagem de um emoji X 
 * qualquer para uma string de codepoints Y que codifica este emoji. Mas o 
 * padrao desta string Y de codepoints deve ser exatamente o mesmo que sera 
 * usado na conversao das strings utf8 nos valores do atributo alt nas tags que 
 * serao editadas. Estas conversoes devem produzir a mesma string Y para que o
 * arquivo de imagem correspondente ao emoji X seja localizado no disco.
 * <p>
 * Os arquivos de imagem PNG originalmente obtidos sao nomeados da forma como 
 * exemplificados em alguns nomes listados abaixo:
 * <ol>
 * <li>angry-face_1f620.png
 * <li>adult_emoji-modifier-fitzpatrick-type-1-2_1f9d1-1f3fb_1f3fb.png
 * <li>blonde-woman-type-1-2_1f471-1f3fb-200d-2640-fe0f
 * </ol>
 * <p>
 * No segundo caso o codepoint 1f3fb se repete e no terceiro o caractere de
 * VARIATION SELECTOR-16 (fe0f) eh terminal no nome do arquivo.
 * <p>
 * Mas a repeticao consecutiva de codepoints nao faz parte da representacao de 
 * emojis como strings de codepoints. Jah o caractere fe0f sempre que aparece,
 * eh no fim dessa string. No entanto alguns nomes e arquivos omitem esse 
 * caractere mesmo quando ele faz parte da string de codepoints do emoji.
 * <p>
 * Por outro lado uma string de codepoints identificando um emoji, muitas vezes,
 * inclui caracteres LOW SURROGATES. Tipos de caracteres estes que sempre sao
 * omitidos nestes nomes de arquivos.
 * <p>
 * Entao como o objetivo eh criar uma normalizacao que sempre converta tanto um
 * nome de arquivo, como uma string de codepoints de emoji, EXATAMENTE em uma 
 * mesma e identica string de codepoints, o que faremos eh eliminar codepoints
 * consecutivamente repetidos em nomes de arquivos. E eliminar LOW SURROGATES em
 * strings codepoints (obtidas a partir de strings utf8 nos arquivos HTML)
 * que representam emojis. E também eliminar caracteres (ef0f) em ambos: nas 
 * strings obtidas nos valores dos atributos alt das tags e nos sufixos de nomes
 * dos arquivos.
 * <p>
 * Para ficar mais claro pode-se observar o seguinte exemplo:
 * <p>
 * Para o emoji do polegar para baixo, o nome orignal do arquivo eh 
 * thumbs-down-sign_1f44e.png. 
 * <p>
 * Elimando o prefixo com a descricao do emoji no nome do arquivo e a extensao
 * fica apenas 1f44e.
 * <p>
 * E sua string no atributo alt da tag teria os  seguintes codepoints:
 * 1f44e-dc4e-fe0f
 * <p>
 * Onde cd4e eh um caractere LOW SURROGATE e fe0f eh o VARIATION SELECTOR. 
 * <p>Se eliminarmos os LOW SURROGATES na string lida no atributo alt, e os 
 * fe0f nesta e tambem nos nomes dos arquivos, obtemos de ambos a mesma string
 * 1f44e. E portanto o programa pode associar o valor do atributo alt (que
 * indica qual emoji deve ser exibido) com o arquivo PNG que tem a imagem deste
 * emoji.
 * <p>
 * Justamente o que queremos fazer para poder editar esta tag e faze-la exibir
 * a imagem do emoji e para isso localizando no disco o arquivo PNG que tem esta
 * imagem.
 * <p>
 * Portanto o que o metodo normalize() desta classe faz eh aplicar nos nomes dos
 * arquivos PNG catalogados na pagina  https://emojipedia.org/whatsapp/2.19.352,
 * a parte desse procedimento destinada a normalizar os nomes de todos estes
 * arquivos.
 * <p>
 * Este metodo recebe como argumento o diretorio onde devem estar todos estes 
 * arquivos e edita o nome de todos eles, normalizando-os, e movendo todos estes
 * arquivos para uma so pasta.
 * 
 * 
 * 
 * @author "Pedro Reis"
 * @since 28 de novembro de 2020 v1.0
 * @version v1.0
 *****************************************************************************/

public final class NormalizeFilenames
{
    /*[01]---------------------------------------------------------------------
       
    -------------------------------------------------------------------------*/
    /**
     * Normaliza os nomes dos arquivos PNG e os move para uma pasta propria.
     * 
     * @param dir O diretorio onde estao os arquivo a serem normalizados
     * 
     * @throws java.io.FileNotFoundException
     */
    public void normalize(final File dir) throws IOException
    {
        ProgressFrame normalizeFrame = 
            new ProgressFrame("Normalizando...", 700, 450, JFrame.EXIT_ON_CLOSE);
        
        File[] fileList = dir.listFiles(new EmojiFileFilter());
        
        normalizeFrame.setVisible(true);
        
        if (fileList.length == 0)
        {
            normalizeFrame.setTitle("Arquivos n\u00e3o encontrados");
            normalizeFrame.setSize(375, 120);
            normalizeFrame.println
            (
                "\n\n      Nenhum arquivo com imagem de emoji encontrado!"
            );
            
            return;
        }
        
        normalizeFrame.setProgressBarVisible(fileList.length);
         
        /*--------------------------------------------------------------------
        Cria um novo diretorio, de nome emoji-images, dentro do diretorio onde
        estao os arquivos PNG com as imagens do emojis.
        ---------------------------------------------------------------------*/
        String emojisDirName = FileTools.makeSubDir(dir, EMOJIS_DIRNAME);
       
        /*--------------------------------------------------------------------*/
      
        int barValue = 0; //contador de num. de arquivos jah normalizados
        
        TreeMap<String, String> map = new TreeMap<>();
        
        /*--------------------------------------------------------------------
         Normaliza os nomes dos arquivos PNG e os move para o diretorio recem-
         criado
        ---------------------------------------------------------------------*/ 
        for (File file: fileList)
        {
            String filename = file.getName();
            
            String normalizedFilename = 
                Normalizer.filenameToCodepoints(filename) + ".png";
            
            /*
            Insere nome original do arquivo e o normalizado em um TreeMap
            para que o arquivo de log exiba uma lista de todos os PNGs 
            normalizados ordenada alfabeticamente pelo nome original
            */
            map.put(filename, normalizedFilename);
                                
            /*-----------------------------------------------------------------
            O arquivo eh renomeado fazendo com que seja movido para o diretorio
            recem criado
            ------------------------------------------------------------------*/
            File newName = new File(emojisDirName + '/' + normalizedFilename);
            
            normalizeFrame.println
            (
                filename + " \u21e8 " + EMOJIS_DIRNAME + '/' + newName.getName() 
            );
        
                        
            file.renameTo(newName); 
            /*----------------------------------------------------------------*/
                                                                     
            normalizeFrame.setProgressBarValue(++barValue);
           
        }//fim do for 
        
        /*---------------------------------------------------------------------
                             Grava o arquivo de log
        ---------------------------------------------------------------------*/
        FileTools.writeLogFile(normalizeFrame, map, emojisDirName);
             
        java.awt.Toolkit.getDefaultToolkit().beep();
         
    }//normalize()
      
    /*=========================================================================
                             Classe interna
    =========================================================================*/
    private final class EmojiFileFilter implements FilenameFilter
    {
        /*
        Este filtro retorna true apenas para arquivos que sao imagens de emojis
        baixadas da pagina https://emojipedia.org/whatsapp/2.19.352
        */
        @Override
        public boolean accept(File dir, String filename)
        {
            return filename.matches(".+_[0-9a-f\\-_]+\\.png");
        }//accept()

    }//classe EmojiFileFilter
    
}//classe NormalizeFileNames