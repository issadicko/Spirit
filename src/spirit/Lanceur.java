/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spirit;

import datamanager.outils.MPoste;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.UIManager;
import preference.PrefMan;

/**
 * @author genius
 */
public class Lanceur {

    public static void main(String args[]) {
        
        try {
            UIManager.setLookAndFeel(PrefMan.getTheme());
        } catch (Exception e) {
        }
        
        JWindow splashscreen = new JWindow();
        SplashPanel panel = new SplashPanel();
        splashscreen.setLayout(new BorderLayout());

        splashscreen.setAlwaysOnTop(true);
        splashscreen.setSize(401, 287);
        splashscreen.getContentPane().add(panel, BorderLayout.CENTER);

        splashscreen.setLocationRelativeTo(null);
        splashscreen.setVisible(true);

        Fenetre fen = new Fenetre(panel.getSplashprogress());

        splashscreen.dispose();
        
        Login login = new Login(fen, "Connexion", true);
        login.transferFocus();
        MPoste user = login.getUser();
        fen.userAdapder(user);
        fen.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fen.setVisible(true);


    }

}
