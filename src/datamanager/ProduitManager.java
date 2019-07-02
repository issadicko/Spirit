/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;
import datamanager.outils.AbstractManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import spirit.entites.Approvisionnement;
import spirit.entites.Produit;

/**
 *
 * @author genius
 */
public class ProduitManager extends AbstractManager<Produit>{
    
    private final String SEARCH = "SELECT * FROM produit WHERE code LIKE ? OR designation LIKE ?",
                         FIND = "SELECT * FROM produit WHERE code = ?",
                         FINDALL = "SELECT * FROM produit ORDER BY designation",
                         UPDATE_DESIGNATION = "UPDATE produit SET designation = ? WHERE code = ?",
                         UPDATE_PU = "UPDATE produit SET prix = ? WHERE code = ?",
                         DELETE = "DELETE FROM produit WHERE code = ?",
                         UPDATE_CODE = "UPDATE produit SET code = ? WHERE code = UPPER(?)",
                         INSERT = "INSERT INTO produit VALUES(?,?,?,?)",
                         FINDAPPROVISIONNEMENT = "SELECT * FROM approvisionnement WHERE UPPER(produit_code) = UPPER(?)",
                         UPDATE_STOCK = "UPDATE produit SET quantite = ? WHERE code = ?";

    public ProduitManager(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Produit object) {
        
        try {
            PreparedStatement st = getConnection().prepareStatement(INSERT);
            
            st.setString(1, object.getCode());
            st.setString(2, object.getDesignation());
            st.setFloat(3, object.getStock());
            st.setFloat(4, object.getPrix());
            
            st.executeUpdate();
            
            
            st.close();
            
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            
            return 0;
        }
        
        return 1;
    }

    public boolean delete(String code) {
        try {
            PreparedStatement st = getConnection().prepareStatement(DELETE);
            st.setString(1, code);
            
            st.executeUpdate();
            
            st.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }

   
    public boolean updateDesignation(String designation,String code) {
        
        try {
                PreparedStatement state = getConnection().prepareStatement(UPDATE_DESIGNATION);
                state.setString(1, designation);
                state.setString(2, code);
                
                state.executeUpdate();
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }

    public Produit find(String code) {
        Produit produit = null;
        
        try {
            
            PreparedStatement state = getConnection().prepareStatement(FIND);
            state.setString(1, code);
            
            state.execute();
            
            ResultSet result = state.getResultSet();
            
            if(result.next()) {                
                produit = new Produit(result.getString("code"), 
                        result.getString("designation"), result.getLong("prix"), 
                        result.getInt("quantite"));
                
                
                /**
                 * Après avoir touver et construit le produit on cherche 
                 * la liste de ses approvisionnements
                 */
                state = getConnection().prepareStatement(FINDAPPROVISIONNEMENT);
                state.setString(1, code);
                
                state.execute();
                result = state.getResultSet();
                
                //On parcourt la liste des approvionnements du produit et on l'ajoute
                
                while (result.next()) {           
                    
                    produit.addApprovisionnement(
                    new Approvisionnement(result.getInt("id"),produit, result.getDate("date"),
                            result.getInt("quantite")));
                }
            }
            
            result.close();
            state.close();
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        return produit;
    }
    
    @Override
    public ArrayList<Produit> findAll(){
       
        ArrayList<Produit> liste = new ArrayList<>();
        
        try {
            
            Statement state = getConnection().createStatement();
            
            ResultSet result = state.executeQuery(FINDALL);
            
            while (result.next()) {                
                liste.add(new Produit(result.getString("code"), result.getString("designation"),
                        result.getLong("prix"), result.getInt("quantite")));
            }
            
            result.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return liste;
    }
    
        public boolean updatePrix(float pu,String code) {
        
        try {
                PreparedStatement state = getConnection().prepareStatement(UPDATE_PU);
                state.setFloat(1, pu);
                state.setString(2, code);
                
                state.executeUpdate();
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
        
        public ArrayList<Produit> search(String mot){
        ArrayList<Produit> list = new ArrayList<>();
        
        try {
            
            mot = "%"+mot+"%";
            
            PreparedStatement prepare = getConnection().prepareStatement(SEARCH);
            
            prepare.setString(1, mot);
            prepare.setString(2, mot);
            
            prepare.execute();
            
            ResultSet result = prepare.getResultSet();
            
            while(result.next()){
                
                //On fait une fait une recherche profonde de chaque produit trouver par la 
                //recherche
                
                list.add(this.find(result.getString("code")));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
        return list;
    }
        
   public boolean updateCode(String old,String new_) {
        
        try {
                PreparedStatement state = getConnection().prepareStatement(UPDATE_CODE);
                state.setString(1, new_);
                state.setString(2, old);
                
                state.executeUpdate();
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
            return false;
        }
        
        return true;
    }
   
   public boolean updateStock(int q,String code){
       
       try {
           
           PreparedStatement st = getConnection().prepareStatement(UPDATE_STOCK);
           st.setInt(1, q);
           st.setString(2, code);
           
           st.execute();
           
       } catch (Exception e) {
           System.out.println(e.getLocalizedMessage());
           return false;
       }
       
       return true;
   }
}
