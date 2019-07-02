/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager.outils;

import controle.Bilan;
import java.util.ArrayList;

/**
 *
 * @author genius
 */
public interface BilanObservateur{
    public void upadateBilan(ArrayList<Bilan> pList);
}
