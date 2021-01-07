package br.com.hkp.whatsappwebfix.util;

import java.awt.Color;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

/**
 *
 * @author "Pedro Reis"
 */
public final class NodeList
{
    private final JCheckBox jCheckBox;
    private boolean fixed;
    private final File file;
    private static int index = 0;
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
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
    public boolean isSelected()
    {
        return jCheckBox.isSelected();
    }//isSelected()
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public void setSelected(final boolean selected)
    {
        SwingUtilities.invokeLater
        (() ->
        {
            jCheckBox.setSelected(selected);
        });
        
    }//setSelected()
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public void invert()
    {
        SwingUtilities.invokeLater
        (() ->
        {
            if (jCheckBox.isSelected())
                jCheckBox.setSelected(false);
            else
                jCheckBox.setSelected(true); 
        });
    }//invert()
        
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public boolean isFixed()
    {
        return fixed;
    }//isFixed()
    
    /*[03]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public void setFixed(final boolean f)
    {        
        fixed = f;
        SwingUtilities.invokeLater
        (() ->
        {
            jCheckBox.setSelected(!fixed);
            if (fixed)
                jCheckBox.setForeground(Color.LIGHT_GRAY);
            else
                jCheckBox.setForeground(Color.BLACK);
        });
    }//serFixed()
    
    /*[04]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public File getFile()
    {
        return file;
    }//getFile()
    
    /*[05]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public JCheckBox getCheckBox()
    {
        return jCheckBox;
    }//getCheckBox()
     
}//classe NodeList
