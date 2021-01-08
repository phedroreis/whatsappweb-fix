package br.com.hkp.whatsappwebfix.apps;

import br.com.hkp.whatsappwebfix.WhatsAppEditor;
import br.com.hkp.whatsappwebfix.global.Global;
import static br.com.hkp.whatsappwebfix.global.Global.FILENAME_DIFF;
import static br.com.hkp.whatsappwebfix.global.Global.TARGET_ABSOLUTE_PATHNAME;
import br.com.hkp.whatsappwebfix.gui.Error;
import br.com.hkp.whatsappwebfix.gui.KeyListen;
import br.com.hkp.whatsappwebfix.gui.SelectFrame;
import br.com.hkp.whatsappwebfix.util.FileList;
import br.com.hkp.whatsappwebfix.util.NodeList;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
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
         Corrige todos os arquivos HTML encontrados no diretorio dir
    -------------------------------------------------------------------------*/
    private void fix(File dir) throws IOException, InterruptedException
    {
        /*
        Obtem um array com todos os arquivos HTML do diretorio "dir" que nao 
        sejam arquivos corrigidos criados por esta propria aplicacao
        */
        File[] listFiles = dir.listFiles(new HtmlFilter());
       
        if (listFiles.length == 0) 
            throw new IOException
            (
                "Nenhum arquivo que possa ser corrigido foi encontrado!"
            );
        
        /*
        A janela de interface da aplicacao
        */
        SelectFrame frame = new SelectFrame();
        
        /*
        Esta estrutura irah armazenar a lista de arquivos exibida por frame
        */
        FileList fileList = new FileList(frame);
        
        /*
        Um listener de teclas para a GUI (interface) responder a comandos pelo
        teclado. O objeto "fileList" eh passado ao construtor para que keyListen
        possa executar metodos deste objeto que selecionam ou deselecionam 
        arquivos listados
        */
        KeyListen keyListen = new KeyListen(fileList);
        
        /*
        O listener de teclado eh passado a janela de interface para que seja
        repassado para cada botao da interface e cada CheckBox na interface. Ou
        seja, para cada elemento GUI da janela de interface. Assim os comandos 
        de teclado funcionarao quando qualquer elemento da GUI estiver com o
        foco.
        */
        frame.addKeyListener(keyListen);
        
        /*
        Este filtro tem a funcao de identificar quais arquivos listados jah 
        possuem copias corrigidas no diretorio
        */
        FixedFilter fixedFilter = new FixedFilter();
        
        /*
        Neste loop sao identificados quais arquivos da lista jah foram 
        corrigidos
        */
        for (File file: listFiles)
        {
            fileList.addNode
            (
                new NodeList(fixedFilter.accept(null, file.getName()), file)
            );
        }//for
        
        frame.setVisible(true);
        
        /*
        loop infinito. A aplicacao se encerra com o botao Sair.
        */
        while(true)
        {
            Global.LOCK.lock();
            /*
            Botoes Sair e Pau na Maquina sao ativados
            */
            Global.BUTTON_HANDLERS_ACTIVE.set(true);
            try
            {
                /*
                Essa thread dorme ate que botao Pau na Maquina seja clicado
                */
                Global.FIX_AWAIT.await(); 
                /*
                Botoes Sair e Pau na Maquina estao inativos enquanto a aplicacao
                corrige os arquivos selecionados na lista.
                */
                Global.BUTTON_HANDLERS_ACTIVE.set(false);
                /*
                Obtem uma lista soh com os arquivos com o checkbox selecionado
                */
                LinkedList<NodeList> listOfFilesToFixed = fileList.getList();
                /*
                Configura a barra de progresso para a execucao
                */
                frame.setProgressBarVisible(listOfFilesToFixed.size());
                
                int count = 0;
                
                frame.setProgressBarValue(0);
                /*
                Corrige os arquivos da lista
                */
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
                JFileChooser.DIRECTORIES_ONLY
            );
            
        try
        {
            FixGui f = new FixGui();
            f.fix(dir);
        }
        catch (IOException | InterruptedException e)
        {
            Error.showErrorMsg(e, true);
        }
       
    }//main()
    
   /*=========================================================================
                              Classes internas
    =========================================================================*/
    private static final String FIXED = FILENAME_DIFF + ".html";
    
    private static final class FixedFilter implements FilenameFilter
    {
        private final String path = TARGET_ABSOLUTE_PATHNAME + '/';
          
        @Override
        public boolean accept(File dir, String filename)
        {
            return new File(path + filename.replace(".html", FIXED)).exists();
        }//accept()

    }//classe FixedFilter
    
    /*************************************************************************/
    private static final class HtmlFilter implements FilenameFilter
    {
        @Override
        public boolean accept(File dir, String filename)
        {
           return (!filename.endsWith(FIXED)) && (filename.endsWith(".html"));
        }//accept()
    }//classe HtmlFilter
    
}//app FixGui