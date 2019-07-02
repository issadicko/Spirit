/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit.customisation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author DICKO
 */
public class MyTableCellRender extends JTextField implements TableCellRenderer{

    public MyTableCellRender() {
        super.setFont(new Font("Serif", Font.PLAIN, 20));
        super.setHorizontalAlignment(JTextField.CENTER);
        super.setBorder(BorderFactory.createEmptyBorder());
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        setText(NumberFormat.getInstance().format(Integer.parseInt(value.toString())));
        
        if (isSelected) {
            setBackground(Color.blue);
        }else{
            setBackground(Color.white);
        }
        
        return this;
    }
    
}
