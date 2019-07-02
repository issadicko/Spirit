/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager.outils;

/**
 *
 * @author genius
 */
public class MPoste{
    
    private String pseudo,passe;
    Poste poste;

    public MPoste(String pseudo, String passe, Poste poste) {
        this.pseudo = pseudo;
        this.passe = passe;
        this.poste = poste;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPasse() {
        return passe;
    }

    public void setPasse(String passe) {
        this.passe = passe;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }
    
    
    public enum Poste {

    ADMIN("ADMIN"),
    VENDEUR("VENDEUR");

    String name = "";
    private Poste(String str) {
        name = str;
    }
    
    
    @Override
    public String toString(){
        return name;
    }
    
   }
    
}
