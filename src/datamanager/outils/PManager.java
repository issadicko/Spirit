/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager.outils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author genius
 */
public class PManager {

    private final String FIND = "SELECT * FROM users WHERE pseudo = ? AND passe = ?";

    public PManager() {

    }

    public MPoste findMPoste(String pseudo, String passe) {
        MPoste poste = null;

        try {

            PreparedStatement prepare = SConnection.getConnection().prepareStatement(FIND);
            prepare.setString(1, pseudo);
            prepare.setString(2, passe);

            ResultSet res = prepare.executeQuery();

            if (res.next()) {
                poste = new MPoste(res.getString("pseudo"), res.getString("passe"), MPoste.Poste.valueOf(res.getString("poste")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return poste;
    }
    
    public void create(MPoste poste){
        String INSERT = "INSERT INTO users VALUES(?,?,?)";
        
        try {
            
            PreparedStatement state = SConnection.getConnection().prepareStatement(INSERT);
            state.setString(1, poste.getPseudo());
            state.setString(2, poste.getPoste().toString());
            state.setString(3, poste.getPasse());
            
            state.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
