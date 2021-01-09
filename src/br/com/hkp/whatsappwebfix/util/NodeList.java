package br.com.hkp.whatsappwebfix.util;

import java.awt.Color;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

/******************************************************************************
 * Um noh da lista de arquivos exibida na janela da interface.
 * 
 * @author "Pedro Reis"
 * @since 7 de janeiro de 2021 v1.0
 * @version v1.0
 *****************************************************************************/
public final class NodeList
{
    private final JCheckBox jCheckBox;
    private boolean fixed;
    private final File file;
    private static int index = 0;
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Cria um no.
     * 
     * @param fixed Informa se o arquivo jah foi corrigido anteriormente.
     * 
     * @param f O arquivo.
     */
    public NodeList(final boolean fixed, final File f)
    {
        file = f;
        
        jCheckBox = new JCheckBox(file.getName());
            
        setFixed(fixed); 
        
        if ((++index % 2) == 0)
            jCheckBox.setBackground(Color.WHITE);
        else
             jCheckBox.setBackground(new Color(250, 250, 200));
        
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Informa se o checkbox deste noh estah selecionado.
     * 
     * @return Estado do checkBox.
     */
    public boolean isSelected()
    {
        return jCheckBox.isSelected();
    }//isSelected()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Seleciona o estado do checkbox do noh.
     * 
     * @param selected true ou false.
     */
    public void setSelected(final boolean selected)
    {
        /*
        
        -------Codigo sugerido pelo NetBeans-----
        
        SwingUtilities.invokeLater
        (() ->
        {
            jCheckBox.setSelected(selected);
        });*/
        
        SwingUtilities.invokeLater
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                   jCheckBox.setSelected(selected);
                }
            }
        );
        
    }//setSelected()
    
    /*[03]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Alterna estado da selecao do noh.
     */
    public void toggle()
    {
        /*
        
        ------Codigo sugerido pelo NetBeans-------
        
        SwingUtilities.invokeLater
        (() ->
        {
            if (jCheckBox.isSelected())
                jCheckBox.setSelected(false);
            else
                jCheckBox.setSelected(true); 
        });*/
        
        SwingUtilities.invokeLater
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                   if (jCheckBox.isSelected())
                       jCheckBox.setSelected(false);
                   else
                       jCheckBox.setSelected(true); 
                }
            }
        );
        
    }//toggle()
        
    /*[04]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Retorna se o arquivo jah foi corrigido anteriormente. Arquivos que jah
     * foram corrigidos possuem no diretorio uma copia com a versao corrigida.
     * 
     * @return true se jah foi corridigo ou false se nao.
     */
    public boolean isFixed()
    {
        return fixed;
    }//isFixed()
    
    /*[05]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Indica o arquivo como jah corrigido ou nao.
     * 
     * @param f true se corrigido, false se nao.
     */
    public void setFixed(final boolean f)
    {        
        fixed = f;
        
        /*
        
        -------Codigo sugerido pelo NetBeans---------
        
        SwingUtilities.invokeLater
        (() ->
        {
            jCheckBox.setSelected(!fixed);
            if (fixed)
                jCheckBox.setForeground(Color.LIGHT_GRAY);
            else
                jCheckBox.setForeground(Color.BLACK);
        });*/
        
        SwingUtilities.invokeLater
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    jCheckBox.setSelected(!fixed);
                    if (fixed)
                        jCheckBox.setForeground(Color.LIGHT_GRAY);
                    else
                        jCheckBox.setForeground(Color.BLACK);

                }
            }
        );
      
    }//serFixed()
    
    /*[06]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Retorna um objeto File apontando para o arquivo que esse noh na lista
     * indica.
     * 
     * @return O arquivo referente a este noh.
     */
    public File getFile()
    {
        return file;
    }//getFile()
    
    /*[07]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * O estado do checkBox deste noh.
     * 
     * @return true ou false.
     */
    public JCheckBox getCheckBox()
    {
        return jCheckBox;
    }//getCheckBox()
     
}//classe NodeList
