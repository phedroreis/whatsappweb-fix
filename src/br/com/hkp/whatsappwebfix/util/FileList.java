package br.com.hkp.whatsappwebfix.util;

import br.com.hkp.whatsappwebfix.gui.SelectFrame;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author "Pedro Reis"
 */
public final class FileList
{
    private final List<NodeList> fileList; 
    private final SelectFrame selectFrame;
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public FileList(final SelectFrame sf)
    {
       fileList = new LinkedList<>();
       selectFrame = sf;
    }//construtor
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public void addNode(NodeList node)
    {
        fileList.add(node);
        selectFrame.addCheckBox(node.getCheckBox());
    }//addNode()
    
    /*[00]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    public LinkedList<NodeList> getList()
    {
        LinkedList<NodeList> list = new LinkedList<>();
        
        for (NodeList node : fileList) if (node.isSelected()) list.add(node);
        
        return list;
    }//getList()
    
    public void seletcAll()
    {
        for(NodeList node: fileList) node.setSelected(true);
    }
    
    public void deselectAll()
    {
        for(NodeList node: fileList) node.setSelected(false);
    }
    
    public void invert()
    {
        for(NodeList node: fileList) node.invert();
    }
    
    
}//classe FileList
