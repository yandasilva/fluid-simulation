package ic.apps.gui;

//Imports
import ic.apps.sph.SPHApp;
import ic.mlibs.util.Application;

//LWJGL
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

//Static LWJGL
import static org.lwjgl.opengl.GL11.*;

//Java
import java.nio.DoubleBuffer;
import javax.swing.JFrame;
import org.lwjgl.util.glu.GLU;

public class GuiApp extends GuiSettings implements Application {
   
    // Editor  
    private GuiEditor myEditor = null; // Settings file editor
    // Application
    private Application myApp  = null; // Application to run
    // Boolean
    private boolean canRun     = false;// Is the application ready to run?

    public GuiApp( String confFile ) {
        // Loads interface settings
        super(confFile);
    
        // Editor setup
        myEditor = new GuiEditor(getPosX(), getPosY(), getWidth(), getHeight());
        myEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myEditor.setVisible(true);

        // Welcome message
        greetings();
    }

    @Override
    public void create() throws LWJGLException {
        
        //Display
        Display.setDisplayMode(new DisplayMode(getWidth(), getHeight()));
        Display.setFullscreen(false);
        Display.setTitle("Polygon mehs processing");
        Display.setLocation(getPosX(), getPosY());
        Display.create();

        //Keyboard
        Keyboard.create();

        //ArcBall
        GuiController.create();

        //OpenGL
        initGL();
    }

    @Override
    public void destroy() {
        //Destroys the arcball
        GuiController.destroy();
        //Destroys the keyboard
        Keyboard.destroy();
        //Destroys the renderer
        Display.destroy();
        //Clear the settings
        clearSettings();
       
        //Clears application and editor
        myApp    = null;
        myEditor = null;
        canRun   = false;

        //Exits
        System.exit(0);
    }

    @Override
    public void run() {
        //If there wasn't a request to close the application
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            //Run the application
            if (Display.isVisible()) {
                processKeyboard();
                processMouse();
                render();
            }
            
            Display.update();
        }
    }

    @Override
    public void render() {
        //OpenGL clears
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        LightsOn();
        
        //ArcBall
        GuiController.transform();
        
        //MyApp
        if(myApp != null) myApp.render();
        
        if(isShowBox() ) drawBox();
        if(isShowAxis()) drawAxis();
        
        LightsOff();
    }
    
    @Override
    public void reset() {
        //Reloads the settings file
        loadSettings();
        
        //Editor setup
        myEditor = new GuiEditor(getPosX(), getPosY(), getWidth(), getHeight());
        myEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myEditor.setVisible(true);

        //Welcome message
        greetings();
    }
     
    @Override
    public void reload() {
        //Reloads the settings file
        loadSettings();
    }
   //-----
    
    public static void greetings() {
        //Welcome message
        System.out.println("-------------------------------------------------");
        System.out.println("Welcome!");
        System.out.println("Polygon mesh processing");
        System.out.println("-------------------------------------------------");
        System.out.println();

        System.out.println("-------------------------------------------------");
        System.out.println("To run a file, choose File -> Open from the menu.");
        System.out.println("-------------------------------------------------");
        System.out.println("Space bar: create aplication.");
        System.out.println("Enter: run application.");
        System.out.println("Down arrow: reload settings.");
        //Repositions observer
        System.out.println("Up arrow: reset view.");
        System.out.println("Backspace (Windows) or Delete (OSX): delete application.");
        System.out.println("Esc: close.");
        System.out.println("-------------------------------------------------");
        System.out.println();

        System.out.println("-------------------------------------------------");
        System.out.println("LOG:");
        System.out.println("-------------------------------------------------");
        System.out.println();    
    }

    //--- Private methods
    
    private boolean initApp() {
        //App settings
        String appSettings = myEditor.getCurPath();

        //To avoid errors
        if( appSettings == null ) return false;
        if(    myApp    != null ) myApp = null;
        
        //Creates application
        if (appSettings.contains("SPH")) {
            myApp = new SPHApp(appSettings);
            
        } else {
            System.out.println("You must open a settings file first.");
            return false;
        }
        
        return true;
    }
    
    private void initGL() {
        //Projection
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        //Camera settings
        GLU.gluPerspective(45.0f, 1.0f, 1.0f, 200.0f);
        GLU.gluLookAt(0, 0, 150, 0, 0, 0, 0, 1, 0);
        glMatrixMode(GL_MODELVIEW);

        //Depth test
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        glClearDepth(100.0);
        glClear(GL_DEPTH_BUFFER_BIT);

    }    

    private void processKeyboard() {
        
        //Creates application when the space bar is pressed
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            try {
                if( initApp() ) {
                    myApp.create();
                    canRun = true;
                }
            } catch (Exception ex) {
                //Frees resources
                myApp.destroy();
                canRun = false;
                //LOG
                System.out.println("Error on Gui::processKeyboard.");
            } 
        }
        
        //Resets the application when the delete key is pressed
        if(Keyboard.isKeyDown(Keyboard.KEY_BACK)){
            if( myApp != null ) { 
                myApp.reset(); 
                canRun = false;
           }
        }
        
        //Runs the application when the enter key is pressed
        if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
            if( myApp != null && canRun) myApp.run();
            GuiController.isRunning();
        }      
        
        //Reloads settings when the down arrow is pressed
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            if( myApp != null && canRun) myApp.reload();
        }
        
        //Repositions camera when the up arrow is pressed
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            GuiController.reset();
        }
    }

    private void processMouse() {
        //Processes mosue input
        GuiController.readInput();
    }

    private void LightsOn() {
        //Enables lighting
        glEnable(GL_LIGHTING);
        glEnable(GL_NORMALIZE);
        glShadeModel(GL_SMOOTH);
        
        //Sets first light
        glEnable(GL_LIGHT0);
        glLight(GL_LIGHT0, GL_AMBIENT,  getLight0Amb() );
        glLight(GL_LIGHT0, GL_DIFFUSE,  getLight0Dif() );
        glLight(GL_LIGHT0, GL_SPECULAR, getLight0Esp() );
        glLight(GL_LIGHT0, GL_POSITION, getLight0Pos() );

        //Sets second light
        glEnable(GL_LIGHT1);
        glLight(GL_LIGHT1, GL_AMBIENT,  getLight1Amb() );
        glLight(GL_LIGHT1, GL_DIFFUSE,  getLight1Dif() );
        glLight(GL_LIGHT1, GL_SPECULAR, getLight1Esp() );
        glLight(GL_LIGHT1, GL_POSITION, getLight1Pos() );
        
        //Enables ambient and diffuse lighting for the front faces
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT,GL_AMBIENT_AND_DIFFUSE);
        
        //Sets material
        glMaterialf(GL_FRONT, GL_SHININESS, getMaterialShi() );

        //Sets polygon offset for coincident faces
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(1,1);    
    }

    private void LightsOff() {
        //Disables lights
        glDisable(GL_COLOR_MATERIAL);
        glDisable(GL_POLYGON_OFFSET_FILL);
        glDisable(GL_LIGHT1);
        glDisable(GL_LIGHT0);
        glDisable(GL_NORMALIZE);
        glDisable(GL_LIGHTING);
        glFlush();    
    }
    
    private void drawAxis() {
        
        //Draws coordinate axes for reference
        glLineWidth(1.5f);
        glColor3d (0.5, 0.5, 0.5);
        glBegin (GL_LINES);
        {
            glColor3d ( 1.0, 0.0, 0.0f );
            glVertex3d(-0.2, 0.0, 0.0f );
            glVertex3d( 0.6, 0.0, 0.0f );

            glColor3d ( 0.0, 1.0, 0.0f );
            glVertex3d( 0.0, 0.6, 0.0f );
            glVertex3d( 0.0,-0.2, 0.0f );


            glColor3d ( 0.0, 0.0, 1.0 );
            glVertex3d( 0.0, 0.0,-0.2f );
            glVertex3d( 0.0, 0.0, 0.6f );
        }
        glEnd ();
    }
    
    private void drawBox() {

      //Draws view box
      DoubleBuffer Modelview = BufferUtils.createDoubleBuffer(16);
      glGetDouble(GL_MODELVIEW_MATRIX, Modelview );

      Double [] viewer = new Double[3];
      viewer[0] = Modelview.get(0*4+2);
      viewer[1] = Modelview.get(1*4+2);
      viewer[2] = Modelview.get(2*4+2);

      Double col = 0.0;

      glLineWidth(1);
      glBegin(GL_LINES);
      {
        col = (viewer[1]>0 && viewer[2]>0)? 0.4 : 1.0 ;
        glColor3d(col,0,0);
        glVertex3d(-1.0,-1.0,-1.0); // e0
        glVertex3d( 1.1,-1.0,-1.0); // e1

        col = (viewer[1]<0 && viewer[2]>0)? 0.4 : 1.0 ;
        glColor3d(col,0,0);
        glVertex3d(-1.0, 1.0,-1.0); // e2
        glVertex3d( 1.0, 1.0,-1.0); // e3

        col = (viewer[1]<0 && viewer[2]<0)? 0.4 : 1.0 ;
        glColor3d(col,0,0);
        glVertex3d(-1.0, 1.0, 1.0); // e6
        glVertex3d( 1.0, 1.0, 1.0); // e7

        col = (viewer[1]>0 && viewer[2]<0)? 0.4 : 1.0 ;
        glColor3d(col,0,0);
        glVertex3d(-1.0,-1.0, 1.0); // e4
        glVertex3d( 1.0,-1.0, 1.0); // e5

        /*---------------------------------------------------------------*/

        col = (viewer[0]>0 && viewer[2]>0)? 0.4 : 1.0 ;
        glColor3d(0,col,0);
        glVertex3d(-1.0,-1.0,-1.0); // e0
        glVertex3d(-1.0, 1.1,-1.0); // e2

        col = (viewer[0]<0 && viewer[2]>0)? 0.4 : 1.0 ;
        glColor3d(0,col,0);
        glVertex3d(1,-1,-1); // e1
        glVertex3d(1, 1,-1); // e3

        col = (viewer[0]<0 && viewer[2]<0)? 0.4 : 1.0 ;
        glColor3d(0,col,0);
        glVertex3d(1,-1,1); // e5
        glVertex3d(1, 1,1); // e7

        col = (viewer[0]>0 && viewer[2]<0)? 0.4 : 1.0 ;
        glColor3d(0,col,0);
        glVertex3d(-1,-1,1); // e4
        glVertex3d(-1, 1,1); // e6

        /*---------------------------------------------------------------*/

        col = (viewer[0]>0 && viewer[1]>0)? 0.4 : 1.0 ;
        glColor3d(0,0,col);
        glVertex3d(-1,-1, -1 ); // e0
        glVertex3d(-1,-1,1.1); // e4

        col = (viewer[0]<0 && viewer[1]>0)? 0.4 : 1.0 ;
        glColor3d(0,0,col);
        glVertex3d(1,-1,-1); // e1
        glVertex3d(1,-1, 1); // e5

        col = (viewer[0]<0 && viewer[1]<0)? 0.4 : 1.0 ;
        glColor3d(0,0,col);
        glVertex3d(1,1,-1); // e3
        glVertex3d(1,1, 1); // e7

        col = (viewer[0]>0 && viewer[1]<0)? 0.4 : 1.0 ;
        glColor3d(0,0,col);
        glVertex3d(-1,1,-1); // e2
        glVertex3d(-1,1, 1); // e6
      }
      glEnd();
    }
}
