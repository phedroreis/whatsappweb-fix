package br.com.hkp.whatsappwebfix.gui;

import br.com.hkp.whatsappwebfix.util.FileList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/******************************************************************************
 * A funcao desta classe eh fornecer um listener para o teclado. 
 * 
 * @author "Pedro Reis"
 * @since 7 de janeiro de 2021 v1.0
 * @version v1.0
 *****************************************************************************/
public final class KeyListen implements KeyListener
{
    private final FileList fileList;
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
  /**
   * O construtor recebe o objeto que mantem a lista de arquivos na janela de
   * interface. Assim pode executar seus metodos quando determinada tecla for
   * pressionada.
   * 
   * @param f A lista de arquivos na janela de interface
   */
    public KeyListen(final FileList f)
    {
        fileList = f;
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Eh executado toda vez que alguma tecla eh pressionada. Se for S, 
     * seleciona todos os arquivos da lista. Se d, deseleciona. Se I, inverte
     * as selecoes feitas.
     * 
     * @param e O objeto com informacoes sobre o evento disparado pelo 
     * pressionar de uma tecla.
     */
    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyChar())
        {
            case 's':
            case 'S':
                fileList.seletcAll(); break;
            case 'd':
            case 'D':
                fileList.deselectAll(); break;
            case 'i':
            case 'I':
                fileList.toggleAll();
        }
    }//keyPressed()
    
    /*[02]---------------------------------------------------------------------
                     Metodos nao implementados da interface.
    -------------------------------------------------------------------------*/
    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyReleased(KeyEvent e){}

}//classe KeyListen
    
   
