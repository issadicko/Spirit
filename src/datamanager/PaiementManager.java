/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import datamanager.outils.AbstractManager;
import datamanager.outils.EntityManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import spirit.entites.Paiement;
import spirit.entites.Vente;

/**
 *
 * @author DICKO
 */
public class PaiementManager extends AbstractManager<Paiement>{
    
    private final String FINDBYVENTE = "SELECT * FROM paiement WHERE vente_id = ?",
           UPDATEMONTANT = "UPDATE paiement SET montant = ? WHERE id = ? LIMIT 1",
           DELETE = "DELETE FROM paiement WHERE id = ?",
           FINDBYID = "SELECT * FROM paiement WHERE id = ?",
           FINDALL = "SELECT * FROM paiement",
           CREATE = "INSERT INTO paiement(vente_id, montant, date) VALUES(?, ?, NOW())";
    /**
     *
     * @param con
     */
    public PaiementManager(Connection con) {
        super(con);
    }

    @Override
    public int create(Paiement object) {
        
        try {
            
            PreparedStatement state = getConnection().prepareStatement(CREATE);
            state.setInt(1, object.getVente().getId());
            state.setDouble(2, object.getMontant());
            state.execute();
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return 0;
    }

    @Override
    public ArrayList<Paiement> findAll() {
        ArrayList<Paiement> liste = new ArrayList<>();
        
        try {
            
            Statement state = getConnection().createStatement();
            ResultSet set = state.executeQuery(FINDALL);
            while (set.next()) {                
                liste.add(buildPaiement(set));
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return liste;
    }
    
    public boolean delete(int id){
        try {
            
            PreparedStatement state = getConnection().prepareStatement(DELETE);
            state.setInt(1, id);
            state.execute();
            
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private Paiement buildPaiement(ResultSet set) throws SQLException {
        Paiement paie = new Paiement(set.getInt("id"), set.getDate("date"), set.getLong("montant"), null);
        
        return paie;
    }
    
    public Paiement findById(int id){
        Paiement paie = null;
        try {
            PreparedStatement state = getConnection().prepareStatement(FINDBYID);
            state.setInt(1, id);
            state.execute();
            ResultSet set = state.getResultSet();
            
            if (set.next()) {
                paie = buildPaiement(set);
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return paie;
    }
    /**
     * 
     * @param id_vente
     * @return liste de tout les paiement de la vente
     */
    public ArrayList<Paiement> findByVente(int id_vente){
        ArrayList<Paiement> liste = new ArrayList<>();
        
        try {
            
            PreparedStatement state = getConnection().prepareStatement(FINDBYVENTE);
            state.setInt(1, id_vente);
            state.execute();
            ResultSet set = state.getResultSet();
            
            while (set.next()) {                
                liste.add(buildPaiement(set));
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return liste;
    }
    
    public boolean updateMontant(int id, long new_montant){
        
        try {
            
            PreparedStatement state = getConnection().prepareStatement(UPDATEMONTANT);
            state.setLong(1, new_montant);
            state.setInt(2, id);
            
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return false;
    }
}
