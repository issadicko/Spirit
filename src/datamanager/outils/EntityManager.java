/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager.outils;

import datamanager.ApproManager;
import datamanager.BilanManager;
import datamanager.ClientManager;
import datamanager.LigneVenteManager;
import datamanager.PaiementManager;
import datamanager.ProduitManager;
import datamanager.VenteManager;

/**
 *
 * @author DICKO
 */
public class EntityManager {
    
    private static ProduitManager produitManager;
    private static VenteManager venteManager;
    private static BilanManager bilanManager;
    private static ApproManager approManager;
    private static LigneVenteManager ligneVenteManager;
    private static ClientManager clientManager;
    private static PaiementManager paiementManager;
    
    public static ProduitManager getProduitManager(){
        if (produitManager == null) {
            produitManager = new ProduitManager(SConnection.getConnection());
        }
        
        return produitManager;
    }
    
    public static VenteManager getVenteManager(){
        if (venteManager == null) {
            venteManager = new VenteManager(SConnection.getConnection());
        }
        
        return venteManager;
    }
    
    public static BilanManager getBilanManager(){
        if (bilanManager == null) {
            bilanManager = new BilanManager();
        }
        
        return bilanManager;
    }
    
    public static ApproManager getApproManager(){
        if (approManager == null) {
            approManager = new ApproManager(SConnection.getConnection());
        }
        
        return approManager;
    }
    
    public static LigneVenteManager getLigneVenteManager(){
        if(ligneVenteManager == null){
            ligneVenteManager = new LigneVenteManager(SConnection.getConnection());
        }
        
        return ligneVenteManager;
    }
    
    public static ClientManager getClientManager(){
        if(clientManager == null){
            clientManager = new ClientManager(SConnection.getConnection());
        }
        
        return clientManager;
    }
    
    public static PaiementManager getPaiementManager(){
        if (paiementManager == null) {
            paiementManager = new PaiementManager(SConnection.getConnection());
        }
        
        return paiementManager;
    }
}
