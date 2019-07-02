/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import datamanager.BilanManager;
import datamanager.outils.BilanObservateur;
import datamanager.outils.SConnection;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import spirit.ExportDialog;

/**
 * @author genius
 */
public class BilanControler{
    
    
    //Donnée pour le reporting des bilans
    private int annee = 0,mois = 0,jour = 0;
    
    BilanManager bManager;
    private static BilanObservateur observer;
    
    
    private static ArrayList<Bilan> liste = new ArrayList();

    public BilanControler() {
        bManager = new BilanManager();
    }

    
   
    public void setObserteur(BilanObservateur os) {
        observer = os;
        Date date = new Date();
        int anMax = date.getYear()+1900,
        j = date.getDate(),
        m = date.getMonth()+1;
        findByDay(j, m, anMax);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    
                    try {
                        Thread.sleep(60000);
                        findByDay(j, m, anMax);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                    
                }
            }
        }).start();
        
        
    }
    
    public void findByDay(int j,int m,int a){
        liste = bManager.BilanDay(j, m, a);
        observer.upadateBilan(liste);
        
        annee = a;
        mois = m;
        jour = j;
    }
    
    public void findByMonth(int mois,int annee){
        liste = bManager.BilanMonth(mois, annee);
        observer.upadateBilan(liste);
        
        this.mois = mois;
        this.annee = annee;
        this.jour = 0;
    }
    
    public void findByYear(int annee){
        liste = bManager.BilanYear(annee);
        observer.upadateBilan(liste);
        
        this.annee = annee;
        mois = 0;
        jour = 0;
    }
    
    
    public static ArrayList<Bilan> generateCollection(){
        return liste;
    }
    
    public static double getTotale(){
        double total = 0;
        for (Bilan bilan : liste) {
            total += bilan.getTotale();
        }
        
        return total;
    }
    
    public void exportBilan(){
        try {
            String file;
            
            Map<String,Object> parameters = new HashMap<>();
            
            if (mois != 0) {
                if (jour != 0) {
                    file = "report/jourBilan.jasper";
                    parameters.put("annee", annee);
                    parameters.put("mois", mois);
                    parameters.put("jour", jour);
                }
                else{
                   file = "report/MonthBilan.jasper"; 
                    parameters.put("annee", annee);
                    parameters.put("mois", mois);
                }
            }else{
                file = "report/YearBilan.jasper";
                parameters.put("annee", annee);
            }
            
            JasperPrint print = JasperFillManager.fillReport(file, parameters, SConnection.getConnection());
            
            if (print != null) {
                
                ExportDialog dialog = new ExportDialog(null, "Export", true);
                int reponse = dialog.showMe();
                
                if(reponse == ExportDialog.PDF)
                {
                    JFileChooser jfc = new JFileChooser();
                    jfc.showSaveDialog(null);
                    File selected = jfc.getSelectedFile();
                    
                    if(selected != null){
                        String result = selected.getPath()+".pdf";
                        JasperExportManager.exportReportToPdfFile(print, result);
                        
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(new File(result));
                        } catch (IOException ex) {
                            Logger.getLogger(BilanControler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                        
                    
                }else if(reponse == ExportDialog.PRINT){
                    JasperPrintManager.printReport(print, true);
                }
            }
            
        } catch (JRException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
