
package br.com.hkp.whatsappwebfix.apps;

import br.com.hkp.whatsappwebfix.WhatsAppEditor;
import br.com.hkp.whatsappwebfix.global.Global;
import static br.com.hkp.whatsappwebfix.global.Global.FILENAME_DIFF;
import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.swing.JFileChooser;

/******************************************************************************
 * Cria copias de paginas HTML WhatsApp Web com os emojis renderizados.
 * 
 * @author "Pedro Reis"
 * @since 29 de novembro de 2020 v1.0
 * @version v1.0
 *****************************************************************************/
public final class Fix
{
  
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public static void main(String[] args)
    {
        Global.fileChooserSettings("Selecione o Diret\u00f3rio");
    
        JFileChooser fc = new JFileChooser();

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int res = fc.showOpenDialog(null);

        if(res == JFileChooser.APPROVE_OPTION)
        {
            ProgressFrame frame = new ProgressFrame("");
            
            File dir = fc.getSelectedFile();
            
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
                
                
                try
                {
                    WhatsAppEditor w = new WhatsAppEditor(file);
                    w.createNewFile();
                }
                catch (IOException ex)
                {
                    System.err.println(ex);
                }
                
                frame.println(file.getName());


                frame.setProgressBarValue(++barValue);
                
            }//for
            
            frame.println("FIM!");
            
            java.awt.Toolkit.getDefaultToolkit().beep();
            
        }//if
       
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

    }//classe EmojiFileFilter
    
}//app Fix
