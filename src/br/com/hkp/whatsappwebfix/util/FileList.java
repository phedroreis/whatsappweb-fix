package br.com.hkp.whatsappwebfix.util;

import br.com.hkp.whatsappwebfix.gui.SelectFrame;
import java.util.List;
import java.util.LinkedList;

/******************************************************************************
 * Implementa uma lista com todos os arquivos exibidos na janela de interface
 * da aplicacao FixGui.
 * 
 * @author "Pedro Reis"
 * @since 7 de janeiro de 2021 v1.0
 * @version v1.0
 *****************************************************************************/
public final class FileList
{
    private final List<NodeList> fileList; // Lista com os dados dos arquivos
    private final SelectFrame selectFrame; // A janela onde a lista eh exibida
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param sf A janela onde a lista sera exibida.
     */
    public FileList(final SelectFrame sf)
    {
       fileList = new LinkedList<>();
       selectFrame = sf;
    }//construtor
    
    /*[01]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Adiciona um no (dados do arquivo e seu checkbox) na lista.
     * 
     * @param node O noh.
     */
    public void addNode(NodeList node)
    {
        fileList.add(node);
        /*
        Adiciona o checkBox relativo ao arquivo na janela de interface.
        */
        selectFrame.addCheckBox(node.getCheckBox());
    }//addNode()
    
    /*[02]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Retorna uma sublista contendo apenas os nodes com arquivos cujos 
     * checkboxes estao selecionados.
     * 
     * @return Sublista com arquivos selecionados.
     */
    public LinkedList<NodeList> getList()
    {
        LinkedList<NodeList> list = new LinkedList<>();
        
        for (NodeList node : fileList) if (node.isSelected()) list.add(node);
        
        return list;
    }//getList()
    
    /*[03]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Marca todos os checkboxes da lista.
     */
    public void seletcAll()
    {
        for(NodeList node: fileList) node.setSelected(true);
    }//selectAll()
    
    /*[04]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Desmarca todos os checkboxes da lista.
     */
    public void deselectAll()
    {
        for(NodeList node: fileList) node.setSelected(false);
    }// deselectAll()
    
    /*[05]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Inverte as selecoes.
     */
    public void toggleAll()
    {
        for(NodeList node: fileList) node.toggle();
    }//toggleAll()
    
    
}//classe FileList
