package br.com.hkp.whatsappwebfix.apps;

import br.com.hkp.whatsappwebfix.Updater;
import br.com.hkp.whatsappwebfix.global.Global;
import br.com.hkp.whatsappwebfix.gui.Error;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

/*****************************************************************************
 * Baixa os arquivos PNG com figuras de Emojis estilo WhatsApp no site da 
 * Emojipedia. Somente aqueles que ainda nao foram baixados.
 * 
 * @since 4 de janeiro de 2021 
 * @version 1.0
 * @author "Pedro Reis"
 ****************************************************************************/
public final class Update
{

   /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * 
     * @param args n/a
     */
    public static void main(String[] args)
    {
        
        /*
        Obtem o diretorio PastaBase onde sao gravados os arquivos comuns
        */
        FileNameExtensionFilter filter = 
            new FileNameExtensionFilter("Diret\u00f3rio", "x");
       
        File pastaBase = 
            Global.choose
            (
                "Localize e Selecione a " + Global.PASTA_BASE,
                filter,
                true
            );
                
        if (pastaBase == null) System.exit(0);
        
        try
        {
            Updater updater = new Updater(pastaBase);
            updater.downloadPngs();
        }
        catch (IOException e)
        {
            Error.showErrorMsg(e);
        }
         
    }//main()
    
}//app Update
