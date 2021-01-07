package br.com.hkp.whatsappwebfix.gui;

import javax.swing.JOptionPane;

/**
 *
 * @author Pedro Reis
 */
public final class Error
{
    public static void showErrorMsg(final Exception e)
    {
        e.printStackTrace();
        
        JOptionPane.showMessageDialog
        (
            null,
            e.getMessage(),
           "Erro Fatal!",
            JOptionPane.ERROR_MESSAGE
        );
        
        System.exit(1);
    }//showErrorMsg()

}//classe Error
