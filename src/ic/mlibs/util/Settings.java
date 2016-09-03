package ic.mlibs.util;

//Java
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.Properties;

public abstract class Settings {

    protected String settingsFile;
    protected Properties settings = new Properties();
    
    public Settings(String arq){
        settingsFile = arq;
        
        loadSettings();
    }

    public final void loadSettings() {
        
      System.out.print("Settings::loadSettings (" + settingsFile + ") --> ");

      File file = new File(settingsFile);

        FileInputStream fis = null;
        settings.clear();

        //Opens .settings file
        try {
            fis = new FileInputStream(file);
            settings.load(fis);
            fis.close();
        } catch (IOException ex) {
            System.out.println("Erro em Settings::load.");
        }

        //Reads settings
        loadStrings();
        
        System.out.println("Sucesso!");
    }

    public void saveSettings() {
                
        String newFile = settingsFile.replace(".settings", "_saved.settings");  
        System.out.print("Settings::saveSettings ("+ newFile +")--> ");

        File file = new File(newFile);

        FileOutputStream fos = null;
        settings.clear();
        
        //Writes settings strings
        saveStrings();

        //Opens settings file
        try {
            fos = new FileOutputStream(file);
            settings.store(fos, "Configurações salvas pelo software.");
            fos.close();
            
        } catch (IOException ex) {
            System.out.println( "Erro em Settings::write." );
        } 
        
        System.out.println("Sucesso!");
    }
    
    //Clear settings
    protected void clearSettings() {
        settings.clear();
    }

    protected abstract void loadStrings();

    protected abstract void saveStrings();
}
