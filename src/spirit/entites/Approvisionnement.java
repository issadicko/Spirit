/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit.entites;

import java.util.Date;

/**
 *
 * @author genius
 */
public class Approvisionnement {
    
    private int id;
    private Produit produit;
    private Date date;
    private int quantite;
    
    public Approvisionnement(){
        id = 0;
        quantite = 0;
    }
    
    public Approvisionnement(int id,Produit produit,Date date_appro, int quantite){
        this.id = id;
        this.date = date_appro;
        this.quantite = quantite;
        this.produit = produit;
    }
    
    public Date getDate(){
       return this.date;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    
    
    public int getQuantite(){
        return this.quantite;
    }
    
    public void setDate(Date date){
        this.date = date;
    }
    
    public void setQuantite(int q){
        this.quantite = q;
    }
    
    public int getId(){
        return this.id;
    }
    
    @Override
    public String toString(){
        return date.toString();
    }
}
