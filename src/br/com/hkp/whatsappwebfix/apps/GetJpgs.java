package br.com.hkp.whatsappwebfix.apps;

import br.com.hkp.whatsappwebfix.DownloadJpgs;
import br.com.hkp.whatsappwebfix.global.Global;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class GetJpgs
{
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * @param args n/a
     */
    public static void main(String[] args)
    {
        DownloadJpgs downloadJpgs = new DownloadJpgs();
        
        /*
        Obtem o arquivo whatsapp-emojis.html
        */
        FileNameExtensionFilter filter = 
            new FileNameExtensionFilter("HTML", "html", "htm");
        
        File file = 
            Global.choose
            (
                "Selecione o arquivo",
                filter,
                false
            );
                
        if (file == null) System.exit(0);
        
        try
        {
            downloadJpgs.downloadJpgs(file);
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }
    
}//App GetJpgs
