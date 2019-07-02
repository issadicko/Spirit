/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit.customisation;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import spirit.JListerow;
import spirit.entites.Produit;

/**
 *
 * @author DICKO
 */
public class MyJListeCellRender extends JListerow implements ListCellRenderer<Produit>{

    public MyJListeCellRender() {
        
    }

    
    
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Produit> list, Produit value, int index, boolean isSelected, boolean cellHasFocus) {
        
        getLabel().setText(" "+value.getDesignation());
        getStock_label().setText(" Quantité en Stock : "+NumberFormat.getInstance().format(value.getStock()));
        getPrix_label().setText("Prix Unitaire : "+NumberFormat.getInstance().format(value.getPrix())+" Fcfa");
        if (isSelected) {
            //TODO #44AA05
            setBackground(Color.decode("#ee5e00"));
            getStock_label().setForeground(Color.white);
            getPrix_label().setForeground(Color.WHITE);
            getLabel().setForeground(Color.WHITE);
            
        }else{
            setBackground(Color.WHITE);
            setDefaultColor();
        }
       
        return this;
    }
    
}
