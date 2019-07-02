/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager.outils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author genius
 */
public class SConnection {
    
    private static Connection connection;
    
    private SConnection(){
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            
            String url = "jdbc:mysql://localhost:3307/smarty";
            
            String user = "root";
            String pass = "";
            
            connection = DriverManager.getConnection(url, user, pass);
            
            System.out.println("Connection effective !!!");
            
        } catch (ClassNotFoundException e) {
            System.out.println(e.getCause());
            JOptionPane.showMessageDialog(null, "La connection a la base de donnée est impossible : \n"+e.toString());
            
        }catch (SQLException e){
            
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, e.toString());
            
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            
            System.exit(0);
        }
    }
    
    public static synchronized Connection getConnection(){
        if (connection==null) {
            new SConnection();
        }
        
        return connection;
    }
    
}
