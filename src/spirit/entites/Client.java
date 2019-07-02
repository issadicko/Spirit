/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit.entites;

import datamanager.outils.EntityManager;
import java.util.ArrayList;

/**
 *
 * @author genius
 */
public class Client {
    
    private String id,nom, prenom,adresse,contact;
    
    //Liste des ventes qui concerne le client
    ArrayList<Vente> listeAchats;
    
    public Client(){
        listeAchats = new ArrayList<>();
    }
    
    public Client(String i,String nom, String prenom,String contact,String adresse){
        this.nom = nom;
        this.id = i;
        this.adresse = adresse;
        this.prenom = prenom;
        this.contact = contact;
        listeAchats = new ArrayList<>();
    }
    
    public void setNom(String n){
        nom = n;
    }
    
    public void setId(String i){
        this.id = i;
    }
    
    public void setPrenom(String prenom){
        this.prenom = prenom;
    }
    
    public void setAdresse(String adresse){
        this.adresse = adresse;
    }
    
    public String getId(){
        return id;
    }
    
    public String getAdresse(){
        return this.adresse;
    }
    
    public String getNom(){
        return nom;
    }
    
    public String getContact(){
        return contact;
    }
    
    public void setContact(String c){
        contact = c;
    }
    
    public String getPrenom(){
        return this.prenom;
    }
    
    public void addAchat(Vente v){
        listeAchats.add(v);
    }
    
    public void removeAchat(Vente v){
        listeAchats.remove(v);
    }
    
    public void setListeAchat(ArrayList<Vente> liste){
        this.listeAchats = liste;
    }
    
    public ArrayList<Vente> getListeAchat(){
        
        if (listeAchats.size() == 0) {
            setListeAchat(EntityManager.getVenteManager().findByClient(this.getId()));
        }
        
        return this.listeAchats;
    }
    
    @Override
    public String toString(){
        return id;
    }
}
