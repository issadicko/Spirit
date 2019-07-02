/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author DICKO
 */
public class JListerow extends javax.swing.JPanel {
    private Color labelColor, otherColor;
    /**
     * Creates new form JListerow
     */
    public JListerow() {
        initComponents();
        labelColor = label.getForeground();
        otherColor = stock_label.getForeground();
    }
    
    protected void setDefaultColor(){
        label.setForeground(labelColor);
        prix_label.setForeground(otherColor);
        stock_label.setForeground(otherColor);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        prix_label = new javax.swing.JLabel();
        stock_label = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        label.setBackground(new java.awt.Color(255, 255, 255));
        label.setFont(new java.awt.Font("Serif", 0, 21)); // NOI18N
        label.setForeground(new java.awt.Color(0, 102, 255));
        label.setText(" HP Spectre");

        prix_label.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        prix_label.setForeground(new java.awt.Color(61, 68, 70));
        prix_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        prix_label.setText("Prix Unitaire : 100000 FCFA");
        prix_label.setToolTipText("");

        stock_label.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        stock_label.setForeground(new java.awt.Color(61, 68, 70));
        stock_label.setText("Stock : 8");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(prix_label, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(stock_label, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prix_label, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(stock_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel label;
    private javax.swing.JLabel prix_label;
    private javax.swing.JLabel stock_label;
    // End of variables declaration//GEN-END:variables

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public JLabel getPrix_label() {
        return prix_label;
    }

    public void setPrix_label(JLabel prix_label) {
        this.prix_label = prix_label;
    }

    public JLabel getStock_label() {
        return stock_label;
    }

    public void setStock_label(JLabel stock_label) {
        this.stock_label = stock_label;
    }

    
}
