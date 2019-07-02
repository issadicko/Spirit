/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package preference;

import java.util.prefs.Preferences;

/**
 *
 * @author DICKO
 */
public class PrefMan {
    private static Preferences preferences;
    private static final String CONFIG_NODE_PATH = "/app_config";
    private static final String THEME_CLASS_NAME = "theme",
                         DATABASE_NAME = "database",
                         DATA_BASE_PASS = "password",
                         DATA_BASE_PORT = "port";
    
    private PrefMan(){
        preferences = Preferences.userRoot().node(CONFIG_NODE_PATH);
    }
    
    public static Preferences getPreferences(){
        if (preferences == null) {
            new PrefMan();
        }
        
        return preferences;
    }
    
    public static String getTheme(){
        if (preferences == null) {
            new PrefMan();
        }
        return preferences.get(THEME_CLASS_NAME, "javax.swing.plaf.nimbus.NimbusLookAndFeel");
    }
}
