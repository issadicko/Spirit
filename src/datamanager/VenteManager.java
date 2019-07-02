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
import java.util.Date;
import spirit.entites.Client;
import spirit.entites.DetailVente;
import spirit.entites.Paiement;
import spirit.entites.Vente;

/**
 *
 * @author genius
 */
public class VenteManager extends AbstractManager<Vente> {

    private final String FIND = "SELECT vente.*, client.nom, client.prenom FROM vente,client WHERE client_id = client.id AND vente.id = ?",
            DELETE = "DELETE FROM vente WHERE id = ?",
            CREATE = "INSERT INTO vente(client_id, date) VALUES(?,NOW())",
            UPDATE = "UPDATE vente SET client_id = ? WHERE id = ?",
            FINDALL = "SELECT vente.*, client.nom, client.prenom FROM vente,client WHERE client_id = client.id ORDER BY id DESC",
            FINDBYCLIENT = "SELECT * FROM vente WHERE client_id = ?";
    private final String SEARSH = "SELECT vente.*, client.nom, client.prenom FROM vente, client WHERE client_id = client.id AND client_id = ? AND date = ?";

    public VenteManager(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Vente object) {
        int id = 0;
        try {

            PreparedStatement prepare = getConnection().prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            prepare.setString(1, object.getClient().getId());

            prepare.executeUpdate();

            ResultSet rs = prepare.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
                object.setId(id);
                persisteLingnes(object);
            }

            prepare.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return id;
        }

        return id;
    }

    public boolean delete(int id_vente) {

        try {

            ArrayList<DetailVente> lignes = EntityManager.getVenteManager().find(id_vente).getLignes();

            PreparedStatement state = getConnection().prepareStatement(DELETE);
            state.setInt(1, id_vente);

            state.executeUpdate();

            for (DetailVente ligne : lignes) {
                int newstock = ligne.getQuantite() + ligne.getProduit().getStock();
                EntityManager.getProduitManager().updateStock(newstock, ligne.getProduit().getCode());
            }

            state.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public boolean updateClientId(int id, String code_client) {
        try {
            PreparedStatement prep = getConnection().prepareStatement(UPDATE);
            prep.setString(1, code_client);
            prep.setInt(2, id);

            prep.executeUpdate();

            prep.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Vente find(int id) {
        Vente v = null;

        try {

            PreparedStatement prepare = getConnection().prepareStatement(FIND);

            prepare.setInt(1, id);
            prepare.execute();

            ResultSet result = prepare.getResultSet();

            if (result.next()) {
                v = buildVente(result);
            }

            result.close();
            prepare.close();
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return v;
    }

    @Override
    public ArrayList<Vente> findAll() {

        ArrayList<Vente> liste = new ArrayList<>();

        try {

            Statement state = getConnection().createStatement();

            ResultSet result = state.executeQuery(FINDALL);

            while (result.next()) {
                liste.add(buildVente(result));
            }

            result.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return liste;

    }

    public ArrayList<Vente> search(Date date, String userCode) {
        ArrayList<Vente> liste = new ArrayList<>();

        try {

            PreparedStatement prepare = getConnection().prepareStatement(SEARSH);

            prepare.setString(1, userCode);
            prepare.setDate(2, new java.sql.Date(date.getTime()));

            prepare.execute();

            ResultSet result = prepare.getResultSet();
            liste = new ArrayList<>();
            while (result.next()) {
                liste.add(buildVente(result));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return liste;
    }

    public ArrayList<Vente> search(Date date) {

        ArrayList<Vente> liste = new ArrayList<>();

        String str = "SELECT vente.*, client.nom, client.prenom FROM vente, client WHERE client_id = client.id AND date = ?";

        try {

            PreparedStatement prepare = getConnection().prepareStatement(str);

            prepare.setDate(1, new java.sql.Date(date.getTime()));

            prepare.execute();

            ResultSet result = prepare.getResultSet();
            liste = new ArrayList<>();
            while (result.next()) {
                liste.add(buildVente(result));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return liste;

    }

    public ArrayList<Vente> search(String userCode) {

        ArrayList<Vente> liste = new ArrayList<>();

        String str = "SELECT vente.*, client.nom, client.prenom FROM vente, client WHERE client_id = client.id AND client_id = ?";

        try {

            PreparedStatement prepare = getConnection().prepareStatement(str);

            prepare.setString(1, userCode);

            prepare.execute();

            ResultSet result = prepare.getResultSet();
            liste = new ArrayList<>();
            while (result.next()) {

                liste.add(buildVente(result));

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return liste;

    }

    private Vente buildVente(ResultSet result) throws SQLException {
        ArrayList<DetailVente> lignes = EntityManager.getLigneVenteManager().findByVente(result.getInt("id"));
        ArrayList<Paiement> paiements = EntityManager.getPaiementManager().findByVente(result.getInt("id"));

        Client client = EntityManager.getClientManager().find(result.getString("client_id"));

        Vente vente = new Vente(result.getInt("id"), result.getDate("date"), lignes, client);
        vente.setPaiements(paiements);

        return vente;
    }

    private void persisteLingnes(Vente object) {
        ProduitManager pm = EntityManager.getProduitManager();
        LigneVenteManager lmanager = EntityManager.getLigneVenteManager();

        object.getLignes().forEach((ligne) -> {
            lmanager.create(ligne);

            int newstock = ligne.getProduit().getStock() - ligne.getQuantite();
            pm.updateStock(newstock, ligne.getProduit().getCode());

        });

    }

    public ArrayList<Vente> findByClient(String client) {
        ArrayList<Vente> liste = new ArrayList<>();

        try {

            PreparedStatement state = getConnection().prepareStatement(FINDBYCLIENT);
            state.setString(1, client);
            state.execute();
            ResultSet result = state.getResultSet();

            while (result.next()) {
                liste.add(buildVente(result));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return liste;
    }

}
