
package br.com.hkp.whatsappwebfix.apps;

import br.com.hkp.whatsappwebfix.util.DownloadPngs;
import java.io.File;
import java.io.IOException;

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
    /**
     * @param args n/a
     */
    public static void main(String[] args)
    {
        DownloadPngs downloadPngs = new DownloadPngs();
        
         /*
        Obtem o diretorio onde estao os arquivo PNG cujos nomes serao
        normalizados
        */
        File file = downloadPngs.getFile();
                
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
