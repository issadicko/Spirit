/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager.outils;

import java.util.ArrayList;
import spirit.entites.Approvisionnement;

/**
 *
 * @author genius
 */
public interface StockObservateur {
    public void updateAppr(ArrayList<Approvisionnement> liste);
}
