/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit.entites;

import java.util.ArrayList;
import java.util.Date;

/**
 *Classe metier permettant la gestion 
 * des ventes 
 * @author genius
 */
public class Vente {
    private int id;
    private Date date;
    private ArrayList<DetailVente> lignes;
    private ArrayList<Paiement> paiements;
    private Client client;

    public Vente(int id, Date date, ArrayList<DetailVente> lignes, Client client) {
        this.id = id;
        this.date = date;
        this.lignes = lignes;
        this.client = client;
        
        bindDetails();
    }
    
    public Vente(){
        lignes = new ArrayList<>();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
    

    public void setLignes(ArrayList<DetailVente> liste) {
        this.lignes = liste;
        bindDetails();
    }
    
    public void setDate(Date date){
        this.date = date;
    }
    
    public int getId(){
        return this.id;
    }
    
    public Date getDate(){
        return this.date;
    }
    
    public void addLigne(DetailVente dv){
        this.lignes.add(dv);
        dv.setVente(this);
    }
    
    public void removeLigne(DetailVente dv){
        this.lignes.remove(dv);
    }
    
    public ArrayList<DetailVente> getLignes(){
        return this.lignes;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void addPaiement(Paiement paie){
        paiements.add(paie);
        paie.setVente(this);
    }
    
    public void removePaiement(Paiement paie){
        paiements.remove(paie);
    }

    public ArrayList<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(ArrayList<Paiement> paiements) {
        this.paiements = paiements;
        bindPaiements();
    }
    
    private void bindDetails(){
        for (DetailVente ligne : lignes) {
            ligne.setVente(this);
        }
    }
    
    private void bindPaiements(){
        for (Paiement paie : paiements) {
            paie.setVente(this);
        }
    }
    
    public long getTotal(){
        long total = 0;
        
        for (DetailVente ligne : lignes) {
            total += ligne.getTotal();
        }
        
        return total;
    }
    
    public long getTotalVerser(){
        long verser = 0;
        
        for (Paiement paie : paiements) {
            verser += paie.getMontant();
        }
        
        return verser;
    }
}
