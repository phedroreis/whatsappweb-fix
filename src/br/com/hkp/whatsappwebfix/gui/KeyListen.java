package br.com.hkp.whatsappwebfix.gui;

import br.com.hkp.whatsappwebfix.util.FileList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class KeyListen implements KeyListener
{
    private final FileList fileList;
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public KeyListen(final FileList f)
    {
        fileList = f;
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyChar())
        {
            case 's', 'S' -> fileList.seletcAll();
            case 'd', 'D' -> fileList.deselectAll();
            case 'i', 'I' -> fileList.invert();
        }
    }//keyPressed()
    
    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyReleased(KeyEvent e){}

}//classe KeyListen
    
   
