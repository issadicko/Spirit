/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import datamanager.ApproManager;
import datamanager.ClientManager;
import datamanager.LigneVenteManager;
import datamanager.ProduitManager;
import datamanager.VenteManager;
import datamanager.outils.EntityManager;
import datamanager.outils.Observable;
import datamanager.outils.Observateur;
import datamanager.outils.SConnection;
import java.awt.Toolkit;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import spirit.Fenetre;
import spirit.entites.Approvisionnement;
import spirit.entites.Client;
import spirit.entites.DetailVente;
import spirit.entites.Produit;
import spirit.entites.Vente;

/**
 *
 * @author genius
 */
public class SmartControler implements Observable {

    ClientManager clmanager;
    ProduitManager pmanager;
    VenteManager vmanager;
    LigneVenteManager lvmanager;
    ApproManager appmanager;
    Observateur observateur;
    ArrayList<DetailVente> panier;
    private static ArrayList<Bilan> parsePanier = new ArrayList<>();

    public ArrayList<DetailVente> getPanier() {
        return panier;
    }

    public void addToPanier(DetailVente p) {

        int test = existe(p.getProduit().getCode());

        if (test < 0) {
            panier.add(p);
            observateur.updatePanier(panier);
            Toolkit.getDefaultToolkit().beep();
        } else {

            int new_q = panier.get(test).getQuantite() + 1;

            if (panier.get(test).setQuantite(new_q)) {
                Toolkit.getDefaultToolkit().beep();
                observateur.updatePanier(panier);
            } else {
                JOptionPane.showMessageDialog(null, "Stock insuffisant !");
            }

        }
    }

    public void removeFromPanier(DetailVente dt) {
        panier.remove(dt);
        Toolkit.getDefaultToolkit().beep();
        observateur.updatePanier(panier);
    }
    Connection cn = SConnection.getConnection();

    public SmartControler() {
        clmanager = new ClientManager(cn);
        pmanager = new ProduitManager(cn);
        vmanager = new VenteManager(cn);
        lvmanager = new LigneVenteManager(cn);
        appmanager = new ApproManager(cn);
        panier = new ArrayList<>();
    }

    public VenteManager getVmanager() {
        return vmanager;
    }

    public LigneVenteManager getLvmanager() {
        return lvmanager;
    }

    public ApproManager getAppmanager() {
        return appmanager;
    }

    public ClientManager getClmanager() {
        return clmanager;
    }

    public ProduitManager getPmanager() {
        return pmanager;
    }

    public void addClient(Client cl) {
        clmanager.create(cl);
        if (observateur != null) {
            observateur.updateClient(clmanager.findAll());
        } else {
            System.out.println("controle.SmartControler.addClient() : Observateur non configurer");
        }

    }

    public void updateClient(Client client) {
        clmanager.update(client);
        observateur.updateClient(clmanager.findAll());
    }

    public void addProduit(Produit p) {
        pmanager.create(p);
        appmanager.create(new Approvisionnement(0, p, new Date(), p.getStock()));
        if (observateur != null) {
            observateur.updateProduit(pmanager.findAll());
            observateur.updateVenteUIproduit(pmanager.findAll());
        } else {
            System.out.println("controle.SmartControler.addProduit() : Observateur non configurer");
        }
    }

    @Override
    public void setObserteur(Observateur os) {
        this.observateur = os;
        observateur.updateClient(clmanager.findAll());
        observateur.updateProduit(pmanager.findAll());
        observateur.updateVentesProduits(vmanager.findAll());
        observateur.updateVenteUIproduit(pmanager.findAll());
    }

    public void removeClient(String code) {
        if (clmanager.find(code) != null) {
            int reponse = JOptionPane.showConfirmDialog((Fenetre) observateur, "Voulez-vous vraiment supprimer ce produit ?", "Attention !", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (reponse == JOptionPane.YES_OPTION) {
                clmanager.delete(code);
                observateur.updateClient(clmanager.findAll());
            }

        }
    }

    public void removeProduit(String code) {
        if (pmanager.find(code) != null) {
            int reponse = JOptionPane.showConfirmDialog((Fenetre) observateur, "Voulez-vous vraiment supprimer ce produit ?", "Attention !", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (reponse == JOptionPane.YES_OPTION) {
                pmanager.delete(code);
                observateur.updateProduit(pmanager.findAll());
                observateur.updateVenteUIproduit(pmanager.findAll());
            }

        } else {
            JOptionPane.showMessageDialog((JFrame) observateur, "Produit introuvable dans la base de données !", "Attention", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void search(String mot) {
        if (mot.length() > 0) {
            observateur.updateProduit(pmanager.search(mot));
        } else {
            observateur.updateProduit(pmanager.findAll());
        }
    }

    public void searchClient(String mot) {
        if (mot.length() > 0) {
            observateur.updateClient(clmanager.search(mot));
        } else {
            observateur.updateClient(clmanager.findAll());
        }
    }

    public void searchVente(Date date, String userCode) {
        if (date != null && userCode.length() > 0) {
            observateur.updateVentesProduits(vmanager.search(date, userCode));
        }
    }

    //Recherche pour la liste des produits de l'interface vendre
    public void searchProduit(String mot) {

        System.out.println(mot);

        if (mot.length() > 0) {
            observateur.updateVenteUIproduit(pmanager.search(mot));
        } else {
            observateur.updateVenteUIproduit(pmanager.findAll());
        }
    }

    public void wrapDetailVenteListe(int venteid) {
        observateur.updateDetailVente(vmanager.find(venteid).getLignes());
    }

    public int createVente(Vente v) {
        int id = vmanager.create(v);
        return id;
    }

    //Voire si un produit est déja présent dans le panier
    public int existe(String codeP) {
        if (panier.size() > 0) {
            int i = 0;
            while (i < panier.size() && !panier.get(i).getProduit().getCode().equals(codeP)) {
                i++;
            }

            return (i < panier.size()) ? i : -1;
        } else {
            return -1;
        }
    }

    public boolean verifierPanier() {

        boolean valide = true;

        if (panier.isEmpty()) {
            return false;
        }

        for (DetailVente dtv : panier) {
            if (dtv.getQuantite() <= 0 || dtv.getQuantite() > dtv.getProduit().getStock()) {
                valide = false;
            }
        }

        return valide;
    }

    public void vendre(Vente v, long espece) {

        vmanager.create(v);

        //Demande si le client veut un ticket de caisse
        int reponse = JOptionPane.showConfirmDialog(null, "Voulez-vous une facture de caisse ?", "Question", JOptionPane.YES_OPTION);
        if (reponse == JOptionPane.YES_OPTION) {
            printTicket(v.getId());
        }
        
        ArrayList<Produit> liste = pmanager.findAll();
        panier.removeAll(panier);
        observateur.updatePanier(panier);
        observateur.updateProduit(liste);
        observateur.updateVenteUIproduit(liste);
        observateur.updateVentesProduits(vmanager.findAll());
    }

    /**
     * @param idVente
     * @return
     */
    public boolean annulerVente(int idVente) {

        boolean ok = vmanager.delete(idVente);

        if (ok) {
            ArrayList<Produit> liste = pmanager.findAll();
            observateur.updateVentesProduits(vmanager.findAll());
            observateur.updateProduit(liste);
            observateur.updateVenteUIproduit(liste);
        }

        return ok;
    }

    public void modifierProduit(Produit pr) {
        pmanager.updateDesignation(pr.getDesignation(), pr.getCode());
        pmanager.updatePrix(pr.getPrix(), pr.getCode());

        observateur.updateProduit(pmanager.findAll());
    }

    public void updateStock(int st, String code) {
        Produit prd = pmanager.find(code);
        if (prd != null) {
            if (st > 0) {
                pmanager.updateStock(st + prd.getStock(), code);

                new Thread(() -> {
                    appmanager.create(new Approvisionnement(0, prd, new Date(), st));
                }).start();

                observateur.updateProduit(pmanager.findAll());
            } else {
                JOptionPane.showMessageDialog(null, "Valeur invalide !");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Le produit est introuvable !");
        }
    }


    public double getPanierTotal() {
        double total = 0;

        for (Bilan dv : parsePanier) {
            total += dv.getTotale();
        }

        return total;
    }

    public void printTicket(int vente) {
    
        try {
            String file = "report/ticket.jasper";

            Map<String, Object> param = new HashMap<>();
            param.put("total", NumberFormat.getInstance().format(Math.round(getPanierTotal())));
            param.put("client", "Dicko Issa Hamadou");

            System.out.println("Impression du ticket en cours ...");;

            param.put("number", vente + "");

            String result = JasperFillManager.fillReportToFile(file, param,
                    SConnection.getConnection());

            if (result != null) {
//                JasperPrintManager.printReport(result, true);
//                Toolkit.getDefaultToolkit().beep();
                JasperExportManager.exportReportToPdfFile(result, "ticket.pdf");
                System.out.println("Impression achever avec success !!!");
            }

        } catch (JRException ex) {
            System.out.println(ex.getMessage());
        }
        parsePanier.removeAll(parsePanier);
    }

    public void addToPanier(int quantieProduit, DetailVente detail) {
        int position = existe(detail.getProduit().getCode());

        if (position < 0) {
            if (quantieProduit > 0) {
                if (detail.setQuantite(quantieProduit)) {
                    panier.add(detail);
                    observateur.updatePanier(panier);
                } else {
                    JOptionPane.showMessageDialog(null, "Pas assez de stock !");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Valeur invalide !");
            }
        } else {

            int new_qte = quantieProduit + panier.get(position).getQuantite();

            if (new_qte >= 0) {
                if (panier.get(position).setQuantite(new_qte)) {
                    observateur.updatePanier(panier);
                } else {
                    JOptionPane.showMessageDialog(null, "Pas assez de stock !");
                }

            } else {
                JOptionPane.showMessageDialog(null, "La quantité du produit dans le panier ne peut être négative !");
            }
        }
    }

}
