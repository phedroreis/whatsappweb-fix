package br.com.hkp.whatsappwebfix.util;

/******************************************************************************
 * Fornece os metodos para converter de UTF-8 string para codepoint string. E
 * para converter o nome original do arquivo baixado da Emojipedia em uma 
 * string de codepoints referente a figura do emoji contida no respectivo 
 * arquivo PNG
 * 
 * @since 26 de novembro de 2020 v1.0
 * @version 1.0
 * @author "Pedro Reis"
 *****************************************************************************/
public final class Normalizer
{
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Recebe uma String representando um emoji codificado em UTF-8 e retorna
     * a string de codepoints deste emoji.
     * <p>
     * Mas se esta string terminar com um codepoint VARIATION SELECTOR-16 este 
     * sera excluido da String retornada.
     * 
     * @param utf8Emoji Um emoji codificado em UTF-8
     * 
     * @return Uma string de codepoints que representa o emoji
     */
    public static String utf8ToCodepoints(final String utf8Emoji)
    {
        StringBuilder codePoints = new StringBuilder(64);
        
        /*
        Le caractere por caractere UTF-8 e os converte para codepoint
        */        
        for (int position = 0; position < utf8Emoji.length(); position++)
        {
            char c = utf8Emoji.charAt(position);
            
            /*
            VARIATION SELECTOR-16 e LOW SURROGATES caracteres nao sao inclusos
            na representacao normalizada do emoji
            */
            if ((c == '\ufe0f') || (Character.isLowSurrogate(c))) continue;
            
            /*
            Obtem o codepoint do UTF-8, converte para hexadecimal e insere na
            string que serah retornada pelo metodo
            */            
            codePoints.append
            (
                String.format
                (
                    position == 0 ? "%x" : "-%x", 
                    Character.codePointAt(utf8Emoji, position)
                )
            );
            
        }//for
        
       
        return codePoints.toString();
        
    }//utf8ToCodepoints()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Recebe uma string com o nome de um arquivo PNG baixado do site Emojipedia
     * que contem a figura de um detemrinado emoji e retorna a string de 
     * codepoints deste emoji.
     * 
     * @param filename O nome do arquivo
     * 
     * @return Uma string de codepoints que representa o emoji
     */
    public static String filenameToCodepoints(final String filename)
    {
                    
        /*
        Extrai do nome do arquivo a parte que eh o nome do emoji
        */
        String emojiName = filename.replaceFirst("_[0-9a-f_\\-]+\\.png", "");

        /*
        Deleta do nome do arquivo:
        
        - A parte que eh nome do emoji
        - Qualquer codepoint repetido precedido por _
        - Qualquer codepoint VARIATION SELECTOR
        - A extensao .png
        
        Sobra apenas a string de codepoints que define o emoji
        */
        return
        (
            filename.replaceFirst(emojiName + "_", "").
            replaceAll("_[0-9a-f]+","").
            replaceAll("-fe0f","").
            replace(".png", "")
        );
        
    }//filenameToCodepoints()
 
}//classe Normalizer
