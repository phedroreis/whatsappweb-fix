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
import javax.swing.SwingUtilities;

/******************************************************************************
 * Eh a janela da interface grafica dos apps que processam arquivos em lote.
 * 
 * @author "Pedro Reis"
 * @since 28 de novembro de 2020 v1.0
 * @version v1.0
 *****************************************************************************/

public final class ProgressFrame extends JFrame
{
    private final JTextArea jTextArea;//Exibe as msgs de texto
    private final JScrollPane jScrollPane;//Barra de rolagem da area de texto
    private final JProgressBar jProgressBar;//Barra de progresso
    private final int closeOperation;
    
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
     * 
     * @param closeOperation Indica o que fazer ao fechar a janela
     */
    public ProgressFrame
    (
        final String title,
        final int width, 
        final int height,
        final int closeOperation
    )
    {
        super(title);//Titulo para a janela.
        
        setSize(width, height);//Dimensoes iniciais da janela.
        
        this.closeOperation = closeOperation;
        
        /*
        Quando esta janela estiver sendo usada por uma thread rodando em segundo
        plano, nao encerra a aplicacao no fechamento desse frame e nem abre o 
        o frame no centro da tela. Porque nesse caso a aplicacao principal jah
        tem seu frame aberto no centro da tela.
        */
        if (closeOperation == JFrame.EXIT_ON_CLOSE) setLocationRelativeTo(null);
       
        /*
        Define o que ocorrerah quando o frame for fechado.
        */
        setDefaultCloseOperation(closeOperation);
       
        setLayout(new BorderLayout());//Gerencia o layout da janela
        
        jTextArea = new JTextArea();//Insere area para exibir texto
        
        jTextArea.setEditable(false);//Nao editavel pelo usuario
        
        jScrollPane = new JScrollPane(jTextArea);//Barras de rolagem na textarea
        
        /*---------------------------------------------------------------------
                  Barras de rolagem exibidas apenas quando necessario
        ----------------------------------------------------------------------*/
        jScrollPane.setVerticalScrollBarPolicy
        (
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        
        jScrollPane.setHorizontalScrollBarPolicy
        (
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        /*--------------------------------------------------------------------*/
        
        jProgressBar = new JProgressBar();
        jProgressBar.setVisible(false);
        
        add(jScrollPane, BorderLayout.CENTER);//Insere area de texto
        add(jProgressBar, BorderLayout.SOUTH);//Insere barra de progresso
        
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
        if (closeOperation == JFrame.EXIT_ON_CLOSE)
        {
            jProgressBar.setValue(value);
            jProgressBar.setString(value + " arquivos");
        }
        else
        {
            SwingUtilities.invokeLater
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        jProgressBar.setValue(value);
                        jProgressBar.setString(value + " arquivos");
                    }

                }
            );
        }
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
        if (closeOperation == JFrame.EXIT_ON_CLOSE)
        {
            jProgressBar.setStringPainted(true);
            jProgressBar.setForeground(Color.BLACK);
            jProgressBar.setValue(0);
            jProgressBar.setMaximum(maximumValue);
            jProgressBar.setVisible(true);
        }
        else
        {
            SwingUtilities.invokeLater
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        jProgressBar.setStringPainted(true);
                        jProgressBar.setForeground(Color.BLACK);
                        jProgressBar.setValue(0);
                        jProgressBar.setMaximum(maximumValue);
                        jProgressBar.setVisible(true);
                    }
                }
            );
        }
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
       
        if (closeOperation == JFrame.EXIT_ON_CLOSE)
        {
             jTextArea.append(s + "\n\n");
             jTextArea.setCaretPosition(jTextArea.getText().length()); 
        }
        else
        {
            SwingUtilities.invokeLater
            (
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                         jTextArea.append(s + "\n\n");
                         jTextArea.setCaretPosition(jTextArea.getText().
                         length()); 
                    }

                }
            );
        }
        
    }//println()
      
    
}//classe ProgressFrame