package br.com.hkp.whatsappwebfix.gui;

import static br.com.hkp.whatsappwebfix.global.Global.PARENT;
import javax.swing.JOptionPane;

/******************************************************************************
 * A funcao desta classe eh fornecer uma janela para mensagens de erro.
 * 
 * @author "Pedro Reis"
 * @since 27 de novembro de 2020 v1.0
 * @version v1.0
 *****************************************************************************/
public final class Error
{
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Uma janela para exibir as mensagens de erro. 
     * 
     * @param e A excecao que causou o problema.
     * 
     * @param fatal Se for true encerra a aplicacao.
     * Se false apenas exibe o erro.
     */
    public static void showErrorMsg(final Exception e, final boolean fatal)
    {
        e.printStackTrace();
        
        JOptionPane.showMessageDialog
        (
            PARENT,
            e.getMessage(),
           (fatal) ? "Erro Fatal!" : "Erro!",
            JOptionPane.ERROR_MESSAGE
        );
        
        if (fatal) System.exit(1);
    }//showErrorMsg()

}//classe Error
