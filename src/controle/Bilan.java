/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

/**
 *
 * @author genius
 */
public class Bilan {
        String designation;
        float prix;
        int nbvente;
        double totale;
        static String description = "";

        public Bilan(String designation, float prix, int nbvente, double totale) {
            this.designation = designation;
            this.prix = prix;
            this.nbvente = nbvente;
            this.totale = totale;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public float getPrix() {
            return prix;
        }

        public void setPrix(float prix) {
            this.prix = prix;
        }

        public int getNbvente() {
            return nbvente;
        }

        public void setNbvente(int nbvente) {
            this.nbvente = nbvente;
        }

        public double getTotale() {
            return totale;
        }

        public void setTotale(double totale) {
            this.totale = totale;
        }

        public static String getDescription() {
            return description;
        }

        public static void setDescription(String description) {
            Bilan.description = description;
        }
        
        
        
        
    
}
