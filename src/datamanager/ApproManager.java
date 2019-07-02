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
import java.util.logging.Level;
import java.util.logging.Logger;
import spirit.entites.Approvisionnement;

/**
 *
 * @author genius
 */
public class ApproManager extends AbstractManager<Approvisionnement>{

    public ApproManager(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Approvisionnement object) {
        
        try {
            PreparedStatement state = getConnection().prepareStatement(
            "INSERT INTO approvisionnement(produit_code,quantite,date) VALUES(?,?,NOW())"
            );
            
            state.setString(1, object.getProduit().getCode());
            state.setInt(2, object.getQuantite());
            
            state.execute();
            
            
            state.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return 1;
    }

    
    public boolean delete(int code) {
        try {
            PreparedStatement state = getConnection().prepareStatement(
            "DELETE FROM approvisionnement WHERE id = ?"
            );
            
            state.setInt(1, code);
            
            state.executeUpdate();
            
            state.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;}

    
    public boolean update(float q,int id) {
        
        try {
            
            PreparedStatement prepare = getConnection().prepareStatement(
                    "UPDATE approvisionnement SET quantite = ? WHERE id = ?");
            
            prepare.setFloat(1, q);
            prepare.setInt(2, id);
            
            prepare.executeUpdate();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return  false;
        }
        
        return true;
    }

    public Approvisionnement find(int id) {
        Approvisionnement app = null;
        
        try {
           PreparedStatement st = getConnection().prepareStatement(
                   "SELECT * FROM approvisionnement WHERE id = ?"
           );
           
           st.setInt(1, id);
           st.execute();
           
            ResultSet result = st.getResultSet();
            ProduitManager ma = new ProduitManager(getConnection());
            if (result.next()) {
                app = new Approvisionnement(result.getInt("id"),
                        ma.find(result.getString("produit_code")), result.getDate("date"), result.getInt("quantite"));
            }
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return app;
    }

    @Override
    public ArrayList<Approvisionnement> findAll() {
        ArrayList<Approvisionnement> liste = new ArrayList<>();
        
        try {
            
            Statement state = getConnection().createStatement();
            
            ResultSet result = state.executeQuery(
                    "SELECT * FROM approvisionnement"
            );
            
            ProduitManager ma = new ProduitManager(getConnection());
            
            while (result.next()) {                
                liste.add(new Approvisionnement(result.getInt("id"), ma.find(result.getString("produit_code")),
                        result.getDate("date"), result.getInt("quantite")));
            }
            
            result.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return liste;
    }
    
   public boolean updateCodeProduit(String code,int id){
       try {
            
            PreparedStatement prepare = getConnection().prepareStatement(
                    "UPDATE approvisionnement SET produit_code = ? WHERE id = ?");
            
            prepare.setString(1, code);
            prepare.setInt(2, id);
            
            prepare.executeUpdate();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return  false;
        }
        
        return true;
   }
   
   public ArrayList<Approvisionnement> findByMonth(int mois,int annee){
       
       ArrayList<Approvisionnement> liste = new ArrayList<>();
       
       String query = "SELECT * FROM approvisionnement WHERE MONTH(date) = ? AND YEAR(date) = ?";
       
        try {
            
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, mois);
            statement.setInt(2, annee);
            
            ResultSet res = statement.executeQuery();
            ProduitManager pm = new ProduitManager(getConnection());
            while (res.next()) {                
                liste.add(new Approvisionnement(res.getInt("id"), pm.find(res.getString("produit_code")), res.getDate("date"), res.getInt("quantite")));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ApproManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return liste;
        
   }

    
}
