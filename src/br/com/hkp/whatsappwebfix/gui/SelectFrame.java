
package br.com.hkp.whatsappwebfix.gui;

import br.com.hkp.whatsappwebfix.Updater;
import br.com.hkp.whatsappwebfix.global.Global;
import static br.com.hkp.whatsappwebfix.global.Global.PASTA_BASE;
import static br.com.hkp.whatsappwebfix.global.Global.TARGET_ABSOLUTE_PATHNAME;
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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

public final class SelectFrame extends JFrame
{
    private final JPanel contentPane;
    private final JPanel panel;
    private final JButton fixButton;
    private final JButton exitButton;
    private final JButton updateButton;
    private final JProgressBar jProgressBar;
    private KeyListen keyListen;
    
   
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public SelectFrame() 
    {
        super("Corrige os Selecionados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        
        Font buttonFont = new Font(Font.MONOSPACED, Font.BOLD, 12);
        
        exitButton = new JButton("Sair    ");
        exitButton.setMnemonic('s');
        exitButton.setFont(buttonFont);
        exitButton.addActionListener(new ExitButtonHandler());
        exitButton.setIcon(new ImageIcon(getClass().getResource("exit.png")));
           
        fixButton = new JButton("Pau na M\u00e1quina!");
        fixButton.setMnemonic('p');
        fixButton.setFont(buttonFont);
        fixButton.addActionListener(new FixButtonHandler());
        fixButton.setIcon(new ImageIcon(getClass().getResource("gear.png")));
        
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
        
        setLocationRelativeTo(null);
        
        //Insere o icone na janela
        try
        {
            URL url = getClass().getResource("favicon.png");
            setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public void addKeyListener(KeyListen k)
    {
       keyListen = k;
       exitButton.addKeyListener(keyListen);
       fixButton.addKeyListener(keyListen);
       updateButton.addKeyListener(keyListen);
    }//addKeyListener()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public void addCheckBox(JCheckBox jc)
    {
        panel.add(jc);
        jc.addKeyListener(keyListen);
    }//addCheckBox()
    
    /*[03]---------------------------------------------------------------------
    
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
    
    /*[04]---------------------------------------------------------------------
    
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

   /*=========================================================================
    *                              Classes internas
    ==========================================================================*/
    private class FixButtonHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (!Global.BUTTON_HANDLERS_ACTIVE.get()) return;
                    
            Global.LOCK.lock();
            try
            {
                Global.FIX_AWAIT.signal();
            }           
            finally
            {
                Global.LOCK.unlock();
            }
          
        }
    }//classe FixButtonHandler
    
    /*************************************************************************/
    private class ExitButtonHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (!Global.BUTTON_HANDLERS_ACTIVE.get()) return;
            System.exit(0);
        }
    }//classe ExitButtonHandler
    
    /*************************************************************************/
    private class UpdateButtonHandler implements ActionListener
    {
        private final File pastaBase;
        private final ExecutorService executorService;
        
        /*[00]-----------------------------------------------------------------
        
        ---------------------------------------------------------------------*/
        public UpdateButtonHandler(final File dir)
        {
            pastaBase = dir;
            executorService = Executors.newFixedThreadPool(1);
        }//construtor
        
        /*[01]-----------------------------------------------------------------
        
        ---------------------------------------------------------------------*/
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (!Global.BUTTON_HANDLERS_ACTIVE.get()) return;
                  
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
                                new Updater(pastaBase, JFrame.DISPOSE_ON_CLOSE);
                            updater.downloadPngs();
                        }
                        catch (IOException e)
                        {
                            Error.showErrorMsg(e, false);
                        }
                    }//run()
                }//classe Runnable
            );
            
        }//actionPerformed()
        
    }//classe UpdateButtonHandler
    
  
}//classe SelectFrame