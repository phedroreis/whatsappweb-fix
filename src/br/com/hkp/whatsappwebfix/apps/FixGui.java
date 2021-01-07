package br.com.hkp.whatsappwebfix.apps;

import br.com.hkp.whatsappwebfix.WhatsAppEditor;
import br.com.hkp.whatsappwebfix.global.Global;
import static br.com.hkp.whatsappwebfix.global.Global.FILENAME_DIFF;
import br.com.hkp.whatsappwebfix.gui.Error;
import br.com.hkp.whatsappwebfix.gui.KeyListen;
import br.com.hkp.whatsappwebfix.gui.SelectFrame;
import br.com.hkp.whatsappwebfix.util.FileList;
import br.com.hkp.whatsappwebfix.util.NodeList;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.filechooser.FileNameExtensionFilter;

/******************************************************************************
 * Cria copias de paginas HTML WhatsApp Web com os emojis renderizados.
 * 
 * @author "Pedro Reis"
 * @since 29 de novembro de 2020 v1.0
 * @version v1.0
 *****************************************************************************/
public final class FixGui
{
    private static String absolutePath;
    
    /*[01]---------------------------------------------------------------------
         Corrige todos os arquivos HTML encontrados no diretorio dir
    -------------------------------------------------------------------------*/
    private void fix(File dir) throws IOException, InterruptedException
    {
        absolutePath = dir.getAbsolutePath() + "/";
        
        File[] listFiles = dir.listFiles(new HtmlFilter());
       
        if (listFiles.length == 0) 
            throw new IOException
            (
                "Nenhum arquivo que possa ser corrigido foi encontrado!"
            );
        
        SelectFrame frame = new SelectFrame();
             
        FileList fileList = new FileList(frame);
        
        KeyListen keyListen = new KeyListen(fileList);
        
        frame.addKeyListener(keyListen);
        
        FixedFilter fixedFilter = new FixedFilter();
 
        for (File file: listFiles)
        {
            fileList.addNode
            (
                new NodeList(fixedFilter.accept(null, file.getName()), file)
            );
        }//for
        
        frame.setVisible(true);
        
        while(true)
        {
            Global.LOCK.lock();
            Global.BUTTON_HANDLERS_ACTIVE.set(true);
            try
            {
                Global.FIX_AWAIT.await(); 
                Global.BUTTON_HANDLERS_ACTIVE.set(false);

                LinkedList<NodeList> listOfFilesToFixed = fileList.getList();
                
                frame.setProgressBarVisible(listOfFilesToFixed.size());
                
                int count = 0;
                
                frame.setProgressBarValue(0);

                for(NodeList node: listOfFilesToFixed)
                {
                    WhatsAppEditor w = new WhatsAppEditor(node.getFile());
                    w.createNewFile();
                    node.setFixed(true);
                    frame.setProgressBarValue(++count);
                }

                java.awt.Toolkit.getDefaultToolkit().beep();
            }
            finally
            {
                Global.LOCK.unlock();
            }
          
        }//while
  
    }//fix()
  
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
                "Selecione a Pasta com os Arquivos a Serem Corrigidos",
                filter,
                true
            );
        
        if (dir == null) System.exit(0);
        
        try
        {
            FixGui f = new FixGui();
            f.fix(dir);
        }
        catch (IOException | InterruptedException e)
        {
            Error.showErrorMsg(e);
        }
       
    }//main()
    
   /*=========================================================================
                              Classes internas
    =========================================================================*/
    private static final class FixedFilter implements FilenameFilter
    {
        @Override
        public boolean accept(File dir, String filename)
        {
            return new File
                   (
                       absolutePath +
                       filename.replace(".html", FILENAME_DIFF + ".html")
                   ).exists();
        }//accept()

    }//classe FixedFilter
    
    /*************************************************************************/
    private static final class HtmlFilter implements FilenameFilter
    {
        String fixed = FILENAME_DIFF + ".html";
        
        @Override
        public boolean accept(File dir, String filename)
        {
           return
           (
               !(filename.endsWith(fixed)) &&
               filename.endsWith(".html")
           );
        }//accept()
    }//classe HtmlFilter
    
}//app FixGui