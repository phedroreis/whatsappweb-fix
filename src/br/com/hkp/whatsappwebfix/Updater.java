package br.com.hkp.whatsappwebfix;

import br.com.hkp.whatsappwebfix.global.Global;
import static br.com.hkp.whatsappwebfix.global.Global.EMOJIS_DIRNAME;
import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import br.com.hkp.whatsappwebfix.util.Normalizer;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;

/*****************************************************************************
 * Baixa os arquivos PNG com figuras de Emojis estilo WhatsApp no site da 
 * Emojipedia. Somente aqueles que ainda nao foram baixados.
 * 
 * @since 4 de janeiro de 2021 
 * @version 1.0
 * @author "Pedro Reis"
 ****************************************************************************/
public final class Updater
{
    private final ProgressFrame frame;
    
    private final String emojisAbsoluteDirName;
   
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe
     * 
     * @param pastaBase PastaBase com arquivos comuns
     * 
     * @param closeOperation Indica o que fazer ao fechar a janela
     * 
     * @throws IOException Se o diretorio para baixar os emojis nao puder ser 
     * criado
     */
    public Updater(final File pastaBase, final int closeOperation) 
        throws IOException
    {
        frame = 
            new ProgressFrame("Pesquisando...", 800, 450, closeOperation);
        
        emojisAbsoluteDirName = FileTools.makeSubDir(pastaBase, EMOJIS_DIRNAME);
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Baixa os arquivos listados na pagina de WhatsApp emojis da Emojipedia que
     * a pasta emoji-images ainda nao tenha.
     * 
     * 
     * @throws IOException Em caso de erro de IO
     */
    public void downloadPngs() throws IOException
    {
        frame.setVisible(true);
        
        frame.println
        (
            "\nProcurando por novos emojis na Emojipedia. Aguarde..."
        );
        
        FileTools.downloadUrl
        (
            "https://emojipedia.org/whatsapp", emojisAbsoluteDirName
        );
        
        File emojipediaFile = new File(emojisAbsoluteDirName + "/whatsapp");
        
        String emojipediaPage = FileTools.readTextFile(emojipediaFile);
        
        emojipediaFile.delete();
                         
        Matcher m = Global.PNG_PATTERN.matcher(emojipediaPage);
        
        TreeMap<String, String> mapUrl2filename = new TreeMap<>();
       
        while (m.find())
        {
            String url = m.group();
            
            String pathname = 
                emojisAbsoluteDirName + '/' +
                Normalizer.
                filenameToCodepoints(FileTools.extractFilenameFromUrl(url)) +
                ".png";
            
            if (!(new File(pathname).exists()))
                mapUrl2filename.put(url, pathname);
           
        }//while
        
        int newEmojis = mapUrl2filename.size();
        
        if (newEmojis == 0)
        {
            frame.setTitle("Nada a Fazer Desta Vez");
            
            frame.println
            (
                "Nenhum novo emoji para baixar. Biblioteca atualizada."
            );
        }
        else
        {
            frame.setTitle("Baixando...");
        
            frame.setProgressBarVisible(newEmojis);
            
            String s = (newEmojis > 1) ? "s" : "";   
            
            frame.println
            (
                String.format
                (
                    "%s%s %d %s%s %s%s.", 
                    "Encontrado", s, newEmojis, "novo", s, "emoji", s
                )
            );

            int count = 0;

            for(String url: mapUrl2filename.keySet())
            {
                FileTools.downloadUrl2Pathname(url, mapUrl2filename.get(url));

                frame.println(String.format("%04d - %s\n", ++count, url));

                frame.setProgressBarValue(count);
            }//for
              
            frame.setTitle("");

            frame.println
            (
                 String.format
                 (
                     "%s%s %s%s %s %s.", 
                     "Arquivo", s, "baixado", s, "para a pasta", 
                     emojisAbsoluteDirName
                 )
            );
            
            FileTools.writeLogFile(frame, mapUrl2filename, emojisAbsoluteDirName);
            
        }//if-else
                 
        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//downloadPngs()
    
}//classe Updater

