package br.com.hkp.whatsappwebfix;

import static br.com.hkp.whatsappwebfix.global.Global.EMOJIS_DIRNAME;
import br.com.hkp.whatsappwebfix.gui.ProgressFrame;
import br.com.hkp.whatsappwebfix.util.FileTools;
import br.com.hkp.whatsappwebfix.util.Normalizer;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @param pastaBase Caminho completo para a PastaBase com arquivos comuns
     * 
     * @throws java.io.IOException Se o diretorio para baixar os emojis nao 
     * puder ser criado
     */
    public Updater(final File pastaBase) throws IOException
    {
        frame = new ProgressFrame("Pesquisando...", 800, 450);
        
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
        
        frame.println("Procurando por novos emojis na Emojipedia. Aguarde...");
        
        String regex = 
            "https://emojipedia-us[.]s3[.]dualstack[.]us-west-1[.]"
            + "amazonaws[.]com/thumbs/72/whatsapp/.+?[.]png";
        
        FileTools.downloadUrl
        (
            "https://emojipedia.org/whatsapp", emojisAbsoluteDirName
        );
        
        File emojipediaFile = new File(emojisAbsoluteDirName + "/whatsapp");
        
        String emojipediaPage = FileTools.readTextFile(emojipediaFile);
        
        Pattern patternRegex = Pattern.compile(regex);
                 
        Matcher m = patternRegex.matcher(emojipediaPage);
        
        TreeMap<String, String> fileList = new TreeMap<>();
       
        while (m.find())
        {
            String url = m.group();
            
            String pathname = 
                emojisAbsoluteDirName + '/' +
                Normalizer.
                filenameToCodepoints(FileTools.extractFilenameFromUrl(url)) +
                ".png";
            
            if (!(new File(pathname).exists())) fileList.put(url, pathname);
           
        }
        
        int newEmojis = fileList.size();
        
        if (newEmojis == 0)
        {
            frame.setTitle("Nada a fazer desta vez");
            frame.println("Nenhum novo emoji para atualizar!");
        }
        else
        {
            frame.setTitle("Baixando...");
        
            frame.setProgressBarVisible(newEmojis);
            
            frame.println("Encontrados " + newEmojis + " novos emojis.");

            int count = 0;

            for(String url: fileList.keySet())
            {
                FileTools.downloadUrl2Pathname(url, fileList.get(url));

                frame.println(String.format("%04d - %s\n", ++count, url));

                frame.setProgressBarValue(count);

            }
              
            frame.setTitle("");

            frame.println
            (
                "Arquivos baixados para a pasta " + emojisAbsoluteDirName
            );
            
            emojipediaFile.delete();
            
            FileTools.writeLogFile(frame, fileList, emojisAbsoluteDirName);
        }
               
        java.awt.Toolkit.getDefaultToolkit().beep();
   
    }//downloadPngs()
    
}//classe Updater

