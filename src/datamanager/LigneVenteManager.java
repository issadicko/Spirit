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
import spirit.entites.DetailVente;
import spirit.entites.Produit;

/**
 *
 * @author genius
 */
public class LigneVenteManager extends AbstractManager<DetailVente> {

    private final String FIND = "SELECT * FROM detail_vente WHERE vente_id = ? AND produit_code = ?",
            DELETE = "DELETE FROM detail_vente WHERE vente_id = ? AND produit_code = ?",
            CREATE = "INSERT INTO detail_vente VALUES(?,?,?,?)",
            FINDALL = "SELECT * FROM detail_vente ORDER BY quantite",
            FINDBYVENTE = "SELECT * FROM detail_vente WHERE vente_id = ?";

    public LigneVenteManager(Connection connection) {
        super(connection);
    }

    @Override
    public int create(DetailVente object) {

        try {

            PreparedStatement prepare = getConnection().prepareStatement(CREATE);
            prepare.setString(1, object.getProduit().getCode());
            prepare.setInt(2, object.getVente().getId());
            prepare.setInt(3, object.getQuantite());
            prepare.setDouble(4, object.getPrixnegocier());

            prepare.execute();
            prepare.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }

        return 1;
    }

    public boolean delete(int vente_id, String produit_code) {

        try {

            PreparedStatement state = getConnection().prepareStatement(DELETE);
            state.setInt(1, vente_id);
            state.setString(2, produit_code);

            state.execute();
            state.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public boolean update(DetailVente object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DetailVente find(int v_id, String code_pro) {
        DetailVente dv = null;

        try {

            PreparedStatement prep = getConnection().prepareStatement(FIND);
            prep.setInt(1, v_id);
            prep.setString(2, code_pro);

            prep.execute();
            ResultSet result = prep.getResultSet();

            if (result.next()) {
               dv = buildLigne(result);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return dv;
    }

    @Override
    public ArrayList<DetailVente> findAll() {

        ArrayList<DetailVente> liste = new ArrayList<>();

        try {

            Statement state = getConnection().createStatement();

            ResultSet result = state.executeQuery(FINDALL);

            while (result.next()) {
                liste.add(buildLigne(result));
            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return liste;

    }

    public ArrayList<DetailVente> findByVente(int id_vente) {
        ArrayList<DetailVente> liste = new ArrayList<>();

        try {

            PreparedStatement state = getConnection().prepareStatement(FINDBYVENTE);
            state.setInt(1, id_vente);

            state.execute();
            ResultSet result = state.getResultSet();

            while (result.next()) {
                liste.add(buildLigne(result));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return liste;

    }

    private DetailVente buildLigne(ResultSet result) throws SQLException {
        Produit prod = EntityManager.getProduitManager().find(result.getString("produit_code"));

        DetailVente detail = new DetailVente(result.getInt("quantite"),
                result.getLong("prix_negocier"),
                null,
                prod);

        return detail;
    }

}
