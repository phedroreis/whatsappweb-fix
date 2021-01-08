package br.com.hkp.whatsappwebfix;

import br.com.hkp.whatsappwebfix.global.Global;
import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import javax.swing.JFrame;

/*****************************************************************************
 * Baixa os arquivos PNG com figuras de Emojis estilo WhatsApp no site da 
 * Emojipedia.
 * 
 * @since 27 de novembro de 2020 
 * @version 1.0
 * @author "Pedro Reis"
 ****************************************************************************/
public final class DownloadPngs
{
    private final ProgressFrame frame;
    
    private static final String TARGET_DIR = "png";
   
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe
     */
    public DownloadPngs()
    {
        frame = new ProgressFrame("Baixando...", 800, 450, JFrame.EXIT_ON_CLOSE);
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa os arquivos listados na pagina de WhatsApp emojis da Emojipedia.
     * 
     * @param emojipediaFile Arquivo com o fonte da pagina HTML da Emojipedia 
     * que lista os emojis do whatsapp
     * 
     * @throws IOException Em caso de erro de IO
     */
    public void downloadPngs(final File emojipediaFile) throws IOException
    {
      
        File downloaDir = new File(TARGET_DIR);
        
        if ((!downloaDir.exists()) && (!downloaDir.mkdirs()))
            throw new IOException("Erro ao criar pasta " + TARGET_DIR); 
               
        String emojipediaPage = FileTools.readTextFile(emojipediaFile);
                 
        Matcher m = Global.PNG_PATTERN.matcher(emojipediaPage);
        
        frame.setVisible(true);
        
        int count = 0;
        
        while (m.find())
        {
            String url = m.group();
                       
            FileTools.downloadUrl(url, TARGET_DIR);
            
            frame.println(String.format("%04d - %s\n", ++count, url));
        }
                  
        frame.setTitle("");
        
        frame.println("Arquivos baixados para a pasta " + TARGET_DIR);
               
        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//downloadPngs()
    
}//classe DownloadPngs
