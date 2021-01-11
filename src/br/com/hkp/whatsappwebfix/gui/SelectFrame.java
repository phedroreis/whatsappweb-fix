package br.com.hkp.whatsappwebfix.gui;

import br.com.hkp.whatsappwebfix.Updater;
import br.com.hkp.whatsappwebfix.WhatsAppEditor;
import static br.com.hkp.whatsappwebfix.global.Global.FILENAME_DIFF;
import static br.com.hkp.whatsappwebfix.global.Global.PASTA_BASE;
import static br.com.hkp.whatsappwebfix.global.Global.TARGET_ABSOLUTE_PATHNAME;
import br.com.hkp.whatsappwebfix.util.FileList;
import br.com.hkp.whatsappwebfix.util.NodeList;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

/******************************************************************************
 * A janela de interface da aplicacao FixGui.
 * 
 * @author "Pedro Reis"
 * @since 10 de janeiro de 2021 v1.0
 * @version v1.0
 *****************************************************************************/
public final class SelectFrame extends JFrame
{
    private final JPanel contentPane;
    private final JPanel panel;
    private final JButton fixButton;
    private final JButton exitButton;
    private final JButton updateButton;
    private final JProgressBar jProgressBar;
    private final KeyListen keyListen;
    private final FileList fileList;
    private AtomicBoolean FixThreadRunning;
    private AtomicBoolean UpdateThreadRunning;
     
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da janela.
     * 
     * @param dir O diretorio onde devem estar os arquivos a serem corrigidos
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public SelectFrame(final File dir) throws IOException 
    {
        super("Corrige os Selecionados");
        
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
        Esta estrutura irah armazenar a lista de arquivos exibida pelo frame
        */
        fileList = new FileList(this);
        
        /*
        Um listener de teclas para a GUI (interface) responder a comandos pelo
        teclado. O objeto "fileList" eh passado ao construtor para que keyListen
        possa executar metodos deste objeto que selecionam ou deselecionam 
        arquivos listados
        */
        keyListen = new KeyListen(fileList);
        
        /*
        Este filtro tem a funcao de identificar quais arquivos listados jah 
        possuem copias corrigidas no diretorio
        */
        FixedFilter fixedFilter = new FixedFilter();
       
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(450, 400));
       
        contentPane = new JPanel();
        setContentPane(contentPane);
        
        JScrollPane scroll = new JScrollPane();
      
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 0, 0));
            
        scroll.setViewportView(panel);
        scroll.setBorder(BorderFactory.createEtchedBorder());
        
        JPanel panelButtons = new JPanel(new BorderLayout());
        panelButtons.setBorder(BorderFactory.createEtchedBorder());
        
        /*
        Fonte para os rotulos de botoes
        */
        Font buttonFont = new Font(Font.MONOSPACED, Font.BOLD, 12);
        
        /*---------------------------------------------------------------------
                                Configura botao Sair
        ---------------------------------------------------------------------*/
        exitButton = new JButton("Sair    ");
        exitButton.setMnemonic('s');
        exitButton.setFont(buttonFont);
        exitButton.addActionListener(new ExitButtonHandler());
        exitButton.setIcon(new ImageIcon(getClass().getResource("exit.png")));
        exitButton.addKeyListener(keyListen);
        /*---------------------------------------------------------------------
                           Configura botao Pau na Maquina
        ---------------------------------------------------------------------*/   
        fixButton = new JButton("Pau na M\u00e1quina!");
        fixButton.setMnemonic('p');
        fixButton.setFont(buttonFont);
        fixButton.addActionListener(new FixButtonHandler());
        fixButton.setIcon(new ImageIcon(getClass().getResource("gear.png")));
        fixButton.addKeyListener(keyListen);
        /*---------------------------------------------------------------------
                             Configura botao Atualizar
        ---------------------------------------------------------------------*/
        updateButton = new JButton("Atualizar");
        updateButton.setMnemonic('a');
        updateButton.setFont(buttonFont);
        updateButton.addActionListener
        (
            new UpdateButtonHandler
            (
                new File(TARGET_ABSOLUTE_PATHNAME + '/' + PASTA_BASE)
            )
        );
        updateButton.setIcon
        (
            new ImageIcon(getClass().getResource("update.png"))
        );
        updateButton.addKeyListener(keyListen);
        /*--------------------------------------------------------------------*/
             
        panelButtons.add(exitButton, BorderLayout.WEST);
        panelButtons.add(fixButton, BorderLayout.CENTER);
        panelButtons.add(updateButton, BorderLayout.EAST);
        
        jProgressBar = new JProgressBar();
        jProgressBar.setBorder(BorderFactory.createEtchedBorder());
        jProgressBar.setVisible(false);
        
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scroll, BorderLayout.CENTER);
        contentPane.add(panelButtons, BorderLayout.NORTH);
        contentPane.add(jProgressBar, BorderLayout.SOUTH);
                  
        pack();
           
        /*
        Insere o icone na janela
        */
        try
        {
            URL url = getClass().getResource("favicon.png");
            setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        
        setLocationRelativeTo(null);//Abre janela no centro da tela
        
        addWindowListener(new CloseWindowHandler());
        
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
        
    }//construtor
  
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Adiciona um checkBox a esta janela e repassa o listener de teclado a ele.
     * 
     * @param jc O checkBox.
     */
    public void addCheckBox(JCheckBox jc)
    {
        panel.add(jc);
        jc.addKeyListener(keyListen);
    }//addCheckBox()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Atualiza a barra de progresso exbindo o status do processo.
     * 
     * @param value O num. de arquivos jah processados
     */
    public void setProgressBarValue(final int value)
    {
        jProgressBar.setValue(value);
        jProgressBar.setString(String.valueOf(value));
    }//setProgressBarValue()
    
    /*[03]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Torna visivel a barra de progresso e a configura.
     * 
     * @param maximumValue O valor maximo que a barra de progresso pode assumir
     * que serah exatamente o numero de arquivos a serem processados.
     */
    public void setProgressBarVisible(final int maximumValue)
    {
        jProgressBar.setStringPainted(true);
        jProgressBar.setForeground(Color.BLACK);
        jProgressBar.setValue(0);
        jProgressBar.setMaximum(maximumValue);
        jProgressBar.setVisible(true);
    }//setProgressBarVisible()
    
    /*[04]---------------------------------------------------------------------
                  Corrige os arquivos que estiverem marcados
    -------------------------------------------------------------------------*/
    private void Fix() throws IOException
    {
        /*
        Obtem uma lista soh com os arquivos com o checkbox selecionado
        */
        LinkedList<NodeList> listOfFilesToFixed = fileList.getList();

        /*
        Configura a barra de progresso para a execucao
        */
        setProgressBarVisible(listOfFilesToFixed.size());

        int count = 0;

        setProgressBarValue(0);
        /*
        Corrige os arquivos da lista
        */
        for(NodeList node: listOfFilesToFixed)
        {
            WhatsAppEditor w = new WhatsAppEditor(node.getFile());
            w.createNewFile();
            node.setFixed(true);
            setProgressBarValue(++count);
        }

        java.awt.Toolkit.getDefaultToolkit().beep();//Beepa o termino
        
    }//Fix()
    
    /*[05]---------------------------------------------------------------------
              Encerra o programa se nenhuma thread estiver executando
    -------------------------------------------------------------------------*/
    private void exit()
    {
        if (FixThreadRunning.get() || UpdateThreadRunning.get()) return;
            
        System.exit(0);
        
    }//exit()
    
    /*=========================================================================
    *            Classes interna. Handler do fechamento da janela.
    ==========================================================================*/
    /*-------------------------------------------------------------------------
                Encerra o programa ao fechar a janela principal
    -------------------------------------------------------------------------*/
    private final class CloseWindowHandler implements WindowListener
    {
        /*[01]------------------------------------------------------------------
        
        ----------------------------------------------------------------------*/
        /**
         * Encerra o programa se nenhuma thread estiver executando.
         * 
         * @param e n/a
         */
        @Override
        public void windowClosing(WindowEvent e)
        {
            exit();
        }//windowClosing()
        
        /*[02]-----------------------------------------------------------------
                                 Nao implementados
        ----------------------------------------------------------------------*/
        @Override
        public void windowOpened(WindowEvent e){}
        @Override
        public void windowClosed(WindowEvent e){}
        @Override
        public void windowIconified(WindowEvent e){}
        @Override
        public void windowDeiconified(WindowEvent e){}
        @Override
        public void windowActivated(WindowEvent e){}
        @Override
        public void windowDeactivated(WindowEvent e){}
        
    }//classe CloseWindow

   /*=========================================================================
    *            Classes internas. Handlers dos botoes da janela.
    ==========================================================================*/
    /*------------------------------------------------------------------------
                               Encerra o programa
    ------------------------------------------------------------------------*/
    private final class ExitButtonHandler implements ActionListener
    {
        /*[01]------------------------------------------------------------------
        
        ----------------------------------------------------------------------*/
        /**
         * Encerra o programa se nenhuma thread estiver executando.
         * 
         * @param e n/a
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            exit();
        }
    }//classe ExitButtonHandler
    
    /*------------------------------------------------------------------------
                       Corrige os arquivos selecionados
    ------------------------------------------------------------------------*/
    private final class FixButtonHandler implements ActionListener
    { 
        private final ExecutorService executorService;
         
        /*[00]------------------------------------------------------------------
        
        ----------------------------------------------------------------------*/
        public FixButtonHandler()
        {
            executorService = Executors.newFixedThreadPool(1);
            
            FixThreadRunning = new AtomicBoolean(false);
        }//construtor
        
        /*[01]-----------------------------------------------------------------
        
        ----------------------------------------------------------------------*/
        /**
         * Corrige os arquivos que estiverem selecionados.
         * 
         * @param e Evento do botao Pau na Maquina clicado
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (FixThreadRunning.get()) return;
            
            FixThreadRunning.set(true);
            
            executorService.execute
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Fix();
                        }
                        catch (IOException e)
                        {
                            Error.showErrorMsg(e, false);
                        }
                        finally
                        {
                            FixThreadRunning.set(false); 
                        }
                    }//run()
                    
                }//classe Runnable
            );
        }//actionPerformed()
        
    }//classe FixButtonHandler
    
    /*------------------------------------------------------------------------
     Cria e executa uma thread que atualiza a biblioteca de emojis em segundo
     plano.
    ------------------------------------------------------------------------*/
    private final class UpdateButtonHandler implements ActionListener
    {
        private final File pastaBase;
        private final ExecutorService executorService;
          
        /*[00]-----------------------------------------------------------------
        
        ---------------------------------------------------------------------*/
        public UpdateButtonHandler(final File dir)
        {
            pastaBase = dir;
            /*
            O metodo estatico da classe Executors retorna um objeto 
            ExecutorService, capaz de gerenciar um pool de threads. Aqui eh 
            criado configurado para soh executar uma por vez. Impedindo que 
            varios downloads simultaneos possam ser disparados.
            */
            executorService = Executors.newFixedThreadPool(1);
            
            UpdateThreadRunning = new AtomicBoolean(false);
        }//construtor
        
        /*[01]-----------------------------------------------------------------
        
        ---------------------------------------------------------------------*/
        /**
         * Atualiza os PNGs com uma thread separada rodando em segundo plano.
         * 
         * @param e Evento do botao Atualizar clicado.
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (UpdateThreadRunning.get()) return;
            
            UpdateThreadRunning.set(true);
            
            executorService.execute
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Updater updater = 
                                new Updater
                                (
                                    pastaBase, 
                                    JFrame.DO_NOTHING_ON_CLOSE
                                );
                            updater.downloadPngs();
                        }
                        catch (IOException e)
                        {
                            Error.showErrorMsg(e, false);
                        }
                        finally
                        {
                            UpdateThreadRunning.set(false); 
                        }
                    }//run()
                }//classe Runnable
            );
            
        }//actionPerformed()
        
    }//classe UpdateButtonHandler
    
    /*=========================================================================
                       Classes internas. Filtros de Arquivos.
    =========================================================================*/
    private final String fixed = FILENAME_DIFF + ".html";
    /*-------------------------------------------------------------------------
           Este filtro retorna true para arquivos que jah foram corrigidos
    -------------------------------------------------------------------------*/
    private final class FixedFilter implements FilenameFilter
    {
        private final String path = TARGET_ABSOLUTE_PATHNAME + '/';
          
        @Override
        public boolean accept(File dir, String filename)
        {
            return new File(path + filename.replace(".html", fixed)).exists();
        }//accept()

    }//classe FixedFilter
    
    /*************************************************************************/
     /*-------------------------------------------------------------------------
        Este filtro retorna true para todos os arquivos HTML, exceto aqueles
        que tenham o sufixo .fix no nome.
    -------------------------------------------------------------------------*/
    private final class HtmlFilter implements FilenameFilter
    {
        @Override
        public boolean accept(File dir, String filename)
        {
           return (!filename.endsWith(fixed)) && (filename.endsWith(".html"));
        }//accept()
        
    }//classe HtmlFilter
    
  
}//classe SelectFrame