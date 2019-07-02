/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit.entites;

import java.util.Date;

/**
 *
 * @author DICKO
 */
public class Paiement {
    private int id;
    private Date date;
    private long montant;
    private Vente vente;

    public Paiement(int id, Date date, long montant, Vente vente) {
        this.id = id;
        this.date = date;
        this.montant = montant;
        this.vente = vente;
    }

    public Paiement() {}
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getMontant() {
        return montant;
    }

    public void setMontant(long montant) {
        this.montant = montant;
    }

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }
    
    
    
}
