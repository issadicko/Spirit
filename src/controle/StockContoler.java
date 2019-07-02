/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import datamanager.ApproManager;
import datamanager.outils.StockObservateur;
import java.sql.Connection;

/**
 *
 * @author genius
 */
public class StockContoler{

    StockObservateur observateur;
    ApproManager manager;
    
    public StockContoler(Connection connection) {
        manager = new ApproManager(connection);
    }
    
    public void setObservateur(StockObservateur observateur) {
        this.observateur = observateur;
        observateur.updateAppr(manager.findAll());
    }

    public ApproManager getManager() {
        return manager;
    }

    public void setManager(ApproManager manager) {
        this.manager = manager;
    }
    
    public void remove(int id){
        manager.delete(id);
        observateur.updateAppr(manager.findAll());
    }
    
    public void filtre(int month,int annee){
        observateur.updateAppr(manager.findByMonth(month, annee));
    }
    
}
