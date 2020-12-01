
package br.com.hkp.whatsappwebfix.apps;

import br.com.hkp.whatsappwebfix.WhatsAppEditor;
import br.com.hkp.whatsappwebfix.global.Global;
import static br.com.hkp.whatsappwebfix.global.Global.FILENAME_DIFF;
import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

/******************************************************************************
 * Cria copias de paginas HTML WhatsApp Web com os emojis renderizados.
 * 
 * @author "Pedro Reis"
 * @since 29 de novembro de 2020 v1.0
 * @version v1.0
 *****************************************************************************/
public final class Fix
{
    /*[01]---------------------------------------------------------------------
         Corrige todos os arquivos HTML encontrados no diretorio dir
    -------------------------------------------------------------------------*/
    private static void fixAll(File dir) throws IOException
    {
        ProgressFrame frame = 
            new ProgressFrame("Gerando arquivos corrigidos...", 700, 450);

        File[] listFiles = dir.listFiles(new HtmlFilter());

        frame.setVisible(true);

        if (listFiles.length == 0)
        {
            frame.setTitle("Arquivos n\u00e3o encontrados");
            frame.setSize(375, 120);
            frame.println
            (
                "\n\n             Nenhum novo arquivo HTML encontrado!"
            );

            return;
        }

        frame.setProgressBarVisible(listFiles.length);

        int barValue = 0;

        for (File file: listFiles)
        {
            WhatsAppEditor w = new WhatsAppEditor(file);
            
            w.createNewFile();
      
            frame.println(file.getName());

            frame.setProgressBarValue(++barValue);

        }//for

        frame.println("FIM!");

        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//fixAll()
  
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static void main(String[] args)
    {
        /*
        Obtem o diretorio onde estao os arquivo HTML
        */
        FileNameExtensionFilter filter = 
            new FileNameExtensionFilter("Diret\u00f3rio", "x");
        
        File dir = 
            Global.choose
            (
                "Selecione o Diret\u00f3rio",
                filter,
                true
            );
        
        if (dir == null) System.exit(0);
        
        try
        {
            fixAll(dir);
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
           
    }//main()
    
   /*=========================================================================
                              Classe interna
    =========================================================================*/
    private static final class HtmlFilter implements FilenameFilter
    {
        
        @Override
        public boolean accept(File dir, String filename)
        {
            if (filename.endsWith(FILENAME_DIFF + ".html")) return false;
                             
            if (filename.endsWith(".html"))
            {
                filename = 
                    filename.substring(0, filename.length() - 5) +
                    FILENAME_DIFF + ".html"; 
                               
                File test = new File(dir.getAbsolutePath() + "/" + filename);
                
                return !(test.exists()); 
            }
            else
                return false;
        }//accept()

    }//classe HtmlFilter
    
}//app Fix
