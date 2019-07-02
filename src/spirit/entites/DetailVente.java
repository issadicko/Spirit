/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit.entites;

/**
 *
 * @author genius
 */
public class DetailVente {
    
    private long prixnegocier;
    private int quantite;
    private Vente vente;
    private Produit produit;

    public DetailVente(int quantite, long prixnegocier, Vente vente, Produit prod) {
        this.quantite = quantite;
        this.prixnegocier = prixnegocier;
        this.vente = vente;
        this.produit = prod;
    }

    public int getQuantite() {
        return quantite;
    }

    public boolean setQuantite(int quantite) {
        if (this.getProduit().getStock() >= quantite) {
            this.quantite = quantite;
            return true;
        }
        
        return false;
    }

    public long getPrixnegocier() {
        return prixnegocier;
    }

    public void setPrixnegocier(long prixnegocier) {
        this.prixnegocier = prixnegocier;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit prod) {
        this.produit = prod;
    }
    
    public Vente getVente(){
        return vente;
    }
    
    public void setVente(Vente vente){
        this.vente = vente;
    }
    
    @Override
    public String toString(){
        return produit.getCode()+""+vente;
    }
    
    public long getTotal(){
        return (prixnegocier * quantite);
    }
}
