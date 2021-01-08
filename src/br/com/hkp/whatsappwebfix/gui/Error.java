package br.com.hkp.whatsappwebfix.gui;

import static br.com.hkp.whatsappwebfix.global.Global.PARENT;
import javax.swing.JOptionPane;

/**
 *
 * @author Pedro Reis
 */
public final class Error
{
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
