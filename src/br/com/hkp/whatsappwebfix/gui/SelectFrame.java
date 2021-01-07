
package br.com.hkp.whatsappwebfix.gui;

import br.com.hkp.whatsappwebfix.global.Global;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.BorderFactory;

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
    private final JProgressBar jProgressBar;
    
   
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
        //panel.setBorder(BorderFactory.createEtchedBorder());
       
        scroll.setViewportView(panel);
        scroll.setBorder(BorderFactory.createEtchedBorder());
        
        JPanel panelButtons = new JPanel(new BorderLayout());
        panelButtons.setBorder(BorderFactory.createEtchedBorder());
        
        exitButton = new JButton("Sair");
        exitButton.addActionListener(new ExitButtonHandler());
        fixButton = new JButton("Pau na M\u00e1quina!");
        fixButton.addActionListener(new FixButtonHandler());
        
        panelButtons.add(exitButton, BorderLayout.WEST);
        panelButtons.add(fixButton, BorderLayout.CENTER);
        
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
    }
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public void addCheckBox(JCheckBox jc)
    {
        panel.add(jc);
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

   /*=========================================================================
    *                              Classes internas
    ==========================================================================*/
    private class FixButtonHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (!Global.buttonHandlerCanExecute.get()) return;
                    
            Global.lock.lock();
            try
            {
                Global.fixAwait.signal();
            }           
            finally
            {
                Global.lock.unlock();
            }
          
        }
    }//classe FixButtonHandler
    
    /*************************************************************************/
    private class ExitButtonHandler implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (!Global.buttonHandlerCanExecute.get()) return;
            System.exit(0);
        }
    }//classe ExitButtonHandler
    
   
}//classe SelectFrame
