/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager.outils;

import java.util.ArrayList;
import spirit.entites.Client;
import spirit.entites.DetailVente;
import spirit.entites.Produit;
import spirit.entites.Vente;

/**
 *
 * @author genius
 */
public interface Observateur {
    
    /**
     * Met a jour la liste des clients au nivau du panneau Clients
     * @param list
     **/
    public void updateClient(ArrayList<Client> list);
    
    /**
     * Met a jour la liste des produits dans le panneau Produit
     *
     * @param liste */
    public void updateProduit(ArrayList<Produit> liste);
    
    /**
     * Met ajour la liste des ventes au niveau du panneau ventes
     *
     * @param listeList **/
    public void updateVentesProduits(ArrayList<Vente> listeList);
    
    /**
     * Met a jout la liste des ligne de vente dans le panier
     *
     * @param ps */
    public void updatePanier(ArrayList<DetailVente> ps);
    
    /**
     * Met ajour la liste des produits au niveau de la JListe du panneau vendre
     *
     * @param ps */
    public void updateVenteUIproduit(ArrayList<Produit> ps);
    
    /**
     * Permet de mettre a jour la liste des detailVente
     * @param liste
     */
    public void updateDetailVente(ArrayList<DetailVente> liste);
}
