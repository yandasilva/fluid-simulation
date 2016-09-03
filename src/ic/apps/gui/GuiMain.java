package ic.apps.gui;

import org.lwjgl.LWJGLException;
public class GuiMain {

    public static void main(String[] args) {
       //GUI
       GuiApp main = null;
       //GUI settings
       String guiSettings = "./src/ic/apps/gui/GuiApp.settings"; 
  
        try {            
            //Allocation
            main = new GuiApp(guiSettings);
            //Creates GUI
            main.create();
            //Runs GUI
            main.run();
            
        } catch (LWJGLException ex) {
            System.out.println("Error on GuiMain::main");
        } finally {
            // Libera os recursos
            if (main != null) main.destroy();
        }
    }
}
