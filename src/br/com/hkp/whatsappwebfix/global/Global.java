package br.com.hkp.whatsappwebfix.global;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/******************************************************************************
 * A funcao desta classe eh fornecer metodos e campos que sejam de acesso para 
 * todas as outras classes do projeto.
 * 
 * @author "Pedro Reis"
 * @since 27 de novembro de 2020 v1.0
 * @version v1.0
 *****************************************************************************/

public final class Global
{
   /**
    * Diretorio comum para acesso de todos os arquivos salvos do WhatsApp Web.
    * Todos os arquivos compartilhados pelas paginas salvas do app devem ficar 
    * aqui
    */
    public static final String PASTA_BASE = "PastaBase";
    
    /**
     * Eh o nome do diretorio para onde serao movidos os arquivos PNG de imagem
     * de emojis.
     */
    public static final String EMOJIS_DIRNAME = "emoji-images";
        
    /**
     * Esta string eh acrescentada a um arquivo que seja copia do original.
     * Se o original for, por exemplo, index.html, sua copia editada serah
     * nomeada como index.fix.html
     */
    public static final String FILENAME_DIFF = ".fix";
       
            
    /*[01]---------------------------------------------------------------------
                     Configura a classe FileChooser 
    -------------------------------------------------------------------------*/
    /**
     * 
     * 
     * @param title Um titulo para a janela
     */
    private static void fileChooserSettings(final String title)
    {
        UIManager.put("FileChooser.openDialogTitleText", title);
        UIManager.put("FileChooser.lookInLabelText", "Selecionar"); 
        UIManager.put("FileChooser.openButtonText", "Abrir"); 
        UIManager.put("FileChooser.cancelButtonText", "Cancelar");
        UIManager.put("FileChooser.fileNameLabelText", "Nome do Arquivo"); 
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo de Arquivo"); 
        UIManager.put("FileChooser.folderNameLabelText", "Selecionado"); 
        UIManager.put
        (
            "FileChooser.openButtonToolTipText", "Abrir o Arquivo Selecionado"
        ); 
        UIManager.put("FileChooser.cancelButtonToolTipText","Cancelar"); 
        UIManager.put("FileChooser.fileNameHeaderText","Nome"); 
        UIManager.put("FileChooser.upFolderToolTipText", "Acima");
        UIManager.put
        (
            "FileChooser.homeFolderToolTipText",
            "\u00c1rea de Trabalho"
        ); 
        UIManager.put("FileChooser.newFolderToolTipText","Nova Pasta");
        UIManager.put("FileChooser.listViewButtonToolTipText","Lista"); 
        UIManager.put("FileChooser.newFolderButtonText","Nova Pasta"); 
        UIManager.put("FileChooser.renameFileButtonText", "Renomear");
        UIManager.put("FileChooser.deleteFileButtonText", "Eliminar");
        UIManager.put("FileChooser.filterLabelText", "Tipo");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalhes");
        UIManager.put("FileChooser.fileSizeHeaderText","Tamanho"); 
        UIManager.put
        (
            "FileChooser.fileDateHeaderText", 
            "Data de Altera\u00e7\u00e3o."
        );
    }//FileChooserSettings()
    
    /*[02]---------------------------------------------------------------------
       
    -------------------------------------------------------------------------*/
    /**
     * Permite que o usuario selecione um diretorio ou um arquivo
     * 
     * @param title Um titulo para a janela
     * 
     * @param filter Um filtro que determina que tipo de arquivo pode ser 
     * selecionado
     * 
     * @param chooseDir Se true so seleciona diretorios. Se false, arquivos.
     * 
     * @return O diretorio ou o arquivo selecionado
     */
    public static File choose
    (
        final String title,
        final FileNameExtensionFilter filter, 
        final boolean chooseDir
    )
    {
        fileChooserSettings(title);
        
        JFileChooser fc = new JFileChooser();
        
        fc.setFileFilter(filter);
        
        if (chooseDir)
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        else
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int res = fc.showOpenDialog(null);

        if(res == JFileChooser.APPROVE_OPTION)
            return fc.getSelectedFile();
        else
            return null;
        
    }//choose()
    
}//classe Global
