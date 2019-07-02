/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import controle.Bilan;
import datamanager.outils.SConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author genius
 */
public class BilanManager{
    
    private final String BYDAY = "SELECT " +
                                 "    produit.*," +
                                 "    SUM(detail_vente.quantite) AS NBVENTE," +
                                 "    SUM(prix_negocier*detail_vente.quantite) AS TOTAL" +
                                 " FROM" +
                                 "    produit," +
                                 "    vente," +
                                 "    detail_vente" +
                                 " WHERE" +
                                 "    code = produit_code" +
                                 "        AND vente.id = vente_id"+
                                 " AND DAY(date) = ? "+
                                 " AND MONTH(date) = ? "+
                                 " AND YEAR(date) = ?" +
                                 " GROUP BY code" +
                                 " ORDER BY NBVENTE DESC",
            BYMONTH = "SELECT " +
                                 "    produit.*," +
                                 "    SUM(detail_vente.quantite) AS NBVENTE," +
                                 "    SUM(prix_negocier * detail_vente.quantite) AS TOTAL" +
                                 " FROM" +
                                 "    produit," +
                                 "    vente," +
                                 "    detail_vente" +
                                 " WHERE" +
                                 "    code = produit_code" +
                                 "        AND vente.id = vente_id"+
                                 " AND MONTH(date) = ? "+
                                 " AND YEAR(date) = ?" +
                                 " GROUP BY code" +
                                 " ORDER BY NBVENTE DESC",
            BYYEAR = "SELECT " +
                                 "    produit.*," +
                                 "    SUM(detail_vente.quantite) AS NBVENTE," +
                                 "    SUM(prix_negocier * detail_vente.quantite) AS TOTAL" +
                                 " FROM" +
                                 "    produit," +
                                 "    vente," +
                                 "    detail_vente" +
                                 " WHERE" +
                                 "    code = produit_code" +
                                 "    AND vente.id = vente_id"+
                                 " AND YEAR(date) = ?" +
                                 " GROUP BY code" +
                                 " ORDER BY NBVENTE DESC";;
    
    
    
    private Connection  pont = SConnection.getConnection();

    
    public ArrayList<Bilan> BilanDay(int d,int m,int y){
        ArrayList<Bilan> bilan = new ArrayList<>();
        
        try {
            
            PreparedStatement state = pont.prepareStatement(BYDAY);
            state.setInt(1, d);
            state.setInt(2, m);
            state.setInt(3, y);
            
            ResultSet res = state.executeQuery();
            
                System.out.println(d);
            
            while(res.next()){
               bilan.add(new Bilan(res.getString("designation"), res.getFloat("prix"), res.getInt("NBVENTE"), res.getDouble("TOTAL")));
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return bilan;
    }
    
    public ArrayList<Bilan> BilanMonth(int m,int y){
        ArrayList<Bilan> bilan = new ArrayList<>();
        
        try {
            
            PreparedStatement state = pont.prepareStatement(BYMONTH);
            state.setInt(1, m);
            state.setInt(2, y);
            
            ResultSet res = state.executeQuery();
            
            while(res.next()){
               bilan.add(new Bilan(res.getString("designation"), res.getFloat("prix"), res.getInt("NBVENTE"), res.getDouble("TOTAL")));
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return bilan;
    }
    
    
    public ArrayList<Bilan> BilanYear(int y){
        ArrayList<Bilan> bilan = new ArrayList<>();
        
        try {
            
            PreparedStatement state = pont.prepareStatement(BYYEAR);
            state.setInt(1, y);
            
            ResultSet res = state.executeQuery();
            
            while(res.next()){
               bilan.add(new Bilan(res.getString("designation"), res.getFloat("prix"), res.getInt("NBVENTE"), res.getDouble("TOTAL")));
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return bilan;
    }
    
   
    
}
