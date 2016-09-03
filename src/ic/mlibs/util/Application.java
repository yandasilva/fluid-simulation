package ic.mlibs.util;

public interface Application {
    
    //Creates the app
    void create() throws Exception;
    
    //Frees resources
    void destroy();
    
    //Runs app
    void run();
    
    //Renders app
    void render();

    //Resets app
    void reset();
    
    //Reloads app settigns
    void reload();
}
