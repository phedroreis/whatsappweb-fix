package br.com.hkp.whatsappwebfix.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/******************************************************************************
 * Eh a janela da interface grafica dos apps que processam arquivos em lote.
 * 
 * @author "Pedro Reis"
 * @since 28 de novembro de 2020 v1.0
 * @version v1.0
 *****************************************************************************/

public final class ProgressFrame extends JFrame
{
    private final JTextArea jTextArea;
    private final JScrollPane jScrollPane;
    private final JProgressBar jProgressBar;
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Configura a janela.
     * 
     * @param title Um titulo para a janela
     * 
     * @param width Largura da janela
     * 
     * @param height Altura da janela
     */
    public ProgressFrame(final String title, final int width, final int height)
    {
        super(title);
        
        setSize(width, height);
        
        setLocationRelativeTo(null);
       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        setLayout(new BorderLayout());
        
        jTextArea = new JTextArea();
        
        jTextArea.setEditable(false);
        
        jScrollPane = new JScrollPane(jTextArea);
        
        jScrollPane.setVerticalScrollBarPolicy
        (
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        
        jScrollPane.setHorizontalScrollBarPolicy
        (
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        
        jProgressBar = new JProgressBar();
        jProgressBar.setVisible(false);
        
        add(jScrollPane, BorderLayout.CENTER);
        add(jProgressBar, BorderLayout.SOUTH);
        
        setVisible(false);
        
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
        jProgressBar.setString(value + " arquivos");
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
    
    -------------------------------------------------------------------------*/
    /**
     * Imprime uma string na area de texto da janela e salta duas linhas. 
     * 
     * @param s A string.
     */
    public void println(final String s)
    {
        jTextArea.append(s + "\n\n");
        jTextArea.setCaretPosition(jTextArea.getText().length()); 
    }//println()
      
    
}//classe ProgressFrame