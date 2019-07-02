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
import javax.swing.JOptionPane;
import spirit.entites.Client;

/**
 *
 * @author genius
 */
public class ClientManager extends AbstractManager<Client> {

    private final String FINDQUERY = "SELECT * FROM client WHERE id = ?",
            DELETEQUERY = "DELETE FROM client WHERE id = ?",
            FINDALL = "SELECT * FROM client ORDER BY nom, prenom",
            UPDATE = "UPDATE client SET nom = ?, prenom = ?,adresse = ?,contact = ? WHERE id = ?",
            FINDBYNAME = "SELECT * FROM client WHERE UPPER(nom) LIKE UPPER(?)",
            SEARCH = "SELECT * FROM client WHERE nom LIKE ? or id LIKE ? or prenom LIKE ?",
            CREATE = "INSERT INTO client VALUES(?,?,?,?,?)";

    public ClientManager(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Client object) {

        try {
            PreparedStatement prepare = getConnection().prepareStatement(CREATE);

            prepare.setString(1, object.getId());
            prepare.setString(2, object.getNom());
            prepare.setString(3, object.getPrenom());
            prepare.setString(4, object.getContact());
            prepare.setString(5, object.getAdresse());

            prepare.execute();
            prepare.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }

        return 1;
    }

    public boolean delete(String idcl) {
        try {
            PreparedStatement st = getConnection().prepareStatement(DELETEQUERY);
            st.setString(1, idcl);

            st.executeUpdate();

            st.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(null, "La suppression a echoué : \nIl est possible que ce client ai déja fait des achats\nEn le supprimant cela peut fausser les bilans en supprimant ses Achats");

            return false;
        }

        return true;
    }

    public boolean update(Client object) {

        try {

            PreparedStatement st = getConnection().prepareStatement(UPDATE);
            st.setString(1, object.getNom());
            st.setString(2, object.getPrenom());
            st.setString(3, object.getAdresse());
            st.setString(4, object.getContact());
            st.setString(5, object.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public Client find(String code) {

        Client client = null;

        try {
            PreparedStatement prepare = getConnection().prepareStatement(FINDQUERY);

            prepare.setString(1, code);

            prepare.execute();
            ResultSet result = prepare.getResultSet();

            if (result.next()) {
                client = buildClient(result);
            }

            prepare.close();
            result.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }

        return client;
    }

    @Override
    public ArrayList<Client> findAll() {
        ArrayList<Client> liste = new ArrayList<>();

        try {
            Statement state = getConnection().createStatement();

            ResultSet result = state.executeQuery(FINDALL);

            while (result.next()) {
                liste.add(new Client(result.getString("id"), result.getString("nom"),
                        result.getString("prenom"), result.getString("contact"),
                        result.getString("adresse")));
            }

            state.close();
            result.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return liste;
    }

    public ArrayList<Client> findByName(String name) {
        ArrayList<Client> list = new ArrayList<>();

        try {

            PreparedStatement prepare = getConnection().prepareStatement(FINDBYNAME);

            prepare.setString(1, name);

            prepare.execute();

            ResultSet result = prepare.getResultSet();

            while (result.next()) {
                list.add(this.find(result.getString("id")));
            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return list;
    }

    public ArrayList<Client> search(String mot) {
        ArrayList<Client> list = new ArrayList<>();

        try {

            mot = "%" + mot + "%";

            PreparedStatement prepare = getConnection().prepareStatement(SEARCH);

            prepare.setString(1, mot);
            prepare.setString(2, mot);
            prepare.setString(3, mot);

            prepare.execute();

            ResultSet result = prepare.getResultSet();

            while (result.next()) {
                list.add(buildClient(result));
            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return list;
    }

    public Client buildClient(ResultSet result) throws SQLException {
        Client client;
        String id = result.getString("id"),
                nom = result.getString("nom"),
                prenom = result.getString("prenom"),
                adresse = result.getString("adresse"),
                contact = result.getString("contact");
        
        client = new Client(id, nom, prenom, contact, adresse);

        return client;
    }

}
