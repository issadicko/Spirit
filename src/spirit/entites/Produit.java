/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit.entites;

import java.util.ArrayList;

/**
 *
 * @author genius
 */
public class Produit {
    String code, designation;
    long prix;
    int stock;
    
    ArrayList<Approvisionnement> approvisionnements;
    
    public Produit(){
        prix = 0;
        prix = 0;
        approvisionnements = new ArrayList<>();}
    
    public Produit(String code, String designation,long prix_unitaire,int stock){
        this.code = code;
        prix = prix_unitaire;
        this.designation = designation;
        this.stock = stock; 
        approvisionnements = new ArrayList<>();
    }
    
    public void setStock(int st){
        this.stock = stock + st;
    }
    
    public void setPrix(long prix_unitaire){
        prix = prix_unitaire;
    }
    
    public void setDesignation(String designation){
        this.designation = designation;
    }
    
    public void setCode(String code){
        this.code = code;
    }
    
    public String getCode(){
        return code;
    }
    
    public String getDesignation(){
        return this.designation;
    }
    
    public long getPrix(){
        return this.prix;
    }
    
    public int getStock(){
        return this.stock;
    }
    
    public void addApprovisionnement(Approvisionnement ap){
        approvisionnements.add(ap);
        ap.setProduit(this);
    }
    
    public void removeApprovisionnement(Approvisionnement ap){
        approvisionnements.remove(ap);
    }
    
    @Override
    public String toString(){
        return designation+"  ("+Math.round(prix)+" FCFA)";
    }
    
    public void binDetails(){
        for (Approvisionnement app : approvisionnements) {
            app.setProduit(this);
        }
    }
}
