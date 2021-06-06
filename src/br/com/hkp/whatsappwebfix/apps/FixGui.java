package br.com.hkp.whatsappwebfix.apps;

import br.com.hkp.whatsappwebfix.global.Global;
import br.com.hkp.whatsappwebfix.gui.Error;
import br.com.hkp.whatsappwebfix.gui.SelectFrame;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/******************************************************************************
 * Cria copias de paginas HTML WhatsApp Web com os emojis renderizados.
 * 
 * @author "Pedro Reis"
 * @since 7 de janeiro de 2021 v1.0
 * @version v1.0
 *****************************************************************************/
public final class FixGui
{
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static void main(String[] args) 
    {
        /*---------------------------------------------------------------------
             Obtem o diretorio onde estao os arquivo HTML das pags. do zap  
        ----------------------------------------------------------------------*/
        FileNameExtensionFilter filter = 
            new FileNameExtensionFilter("Diret\u00f3rio", "x");
        
        File dir = 
            Global.choose
            (
                "Selecione a Pasta com os Arquivos a Serem Corrigidos",
                filter,
                JFileChooser.DIRECTORIES_ONLY
            );
        /*--------------------------------------------------------------------*/
            
        try
        {
            SelectFrame f = new SelectFrame(dir);
            f.setVisible(true);
        }
        catch (IOException e)
        {
            Error.showErrorMsg(e, true);
        }
       
    }//main()
    
      
}//app FixGui