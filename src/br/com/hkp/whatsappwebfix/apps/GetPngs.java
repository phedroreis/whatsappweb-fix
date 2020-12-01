
package br.com.hkp.whatsappwebfix.apps;

import br.com.hkp.whatsappwebfix.DownloadPngs;
import br.com.hkp.whatsappwebfix.global.Global;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

/*****************************************************************************
 * Baixa os arquivos PNG com figuras de Emojis estilo WhatsApp no site da 
 * Emojipedia.
 * <p>
 * Esse programa precisa encontrar o arquivo whatsapp-emojis.html no diretorio
 * corrente para executar. Neste arquivo ele encontra as URLs dos PNGs de 
 * emojis que serao baixados.
 * <p>
 * O arquivo whatsapp-emojis.html eh o codigo fonte da pagina 
 * https://emojipedia.org/whatsapp/
 * 
 * @since 27 de novembro de 2020 
 * @version 1.0
 * @author "Pedro Reis"
 ****************************************************************************/
public class GetPngs
{
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * @param args n/a
     */
    public static void main(String[] args)
    {
        DownloadPngs downloadPngs = new DownloadPngs();
        
        /*
        Obtem o arquivo whatsapp-emojis.html
        */
        FileNameExtensionFilter filter = 
            new FileNameExtensionFilter("HTML", "html", "htm");
        
        File file = 
            Global.choose
            (
                "Selecione o arquivo whatsapp-emojis.html",
                filter,
                false
            );
                
        if (file == null) System.exit(0);
        
        try
        {
            downloadPngs.downloadPngs(file);
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }
    
}//App GetPngs
