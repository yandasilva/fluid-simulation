package ic.apps.gui;

//LWJGL
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Quaternion;

//Static LWJGL
import static org.lwjgl.opengl.GL11.*;

public class GuiController {
    //TrackBall
    private static Quaternion tball; 

    //Euler angles
    private static double rotX = 0;
    private static double rotY = 0;
    private static double rotZ = 0;
    
    //Zoom
    private static double zoom = 1;
    
    //Translation
    private static double trsX = 0;
    private static double trsY = 0;
    
    //Mouse sensibility
    private static final double ROT_FACTOR =  0.1f;
    private static final double TRS_FACTOR =  0.01f;
    private static final double ZOM_FACTOR =  0.001f;
    
    //Is the application running?
    private static boolean running = false;
     
    static void create() throws LWJGLException{
        
      //Mouse
      Mouse.create();
      Mouse.setGrabbed(false);
           
      //Quaternion
      tball = new Quaternion();
       
    }
    
    static void destroy(){
        //Mouse
        Mouse.destroy();
        //Quaternion
        tball = null;
    }
    
    static void readInput() {
        
        //Reads input from left mouse button - rotation
        if( Mouse.isButtonDown(0) ){
            tball.set(Mouse.getDY(),Mouse.getDX());
            
            rotX += ROT_FACTOR*tball.x;
            rotY += ROT_FACTOR*tball.y;
            rotZ += ROT_FACTOR*tball.z;       
        } 
        //Reads input from right mouse button - translation
        else if( Mouse.isButtonDown(1) ){
            trsX += TRS_FACTOR*Mouse.getDX(); 
            trsY += TRS_FACTOR*Mouse.getDY();         
        }
        //Reads input from mouse wheel - zoom
        else {
            zoom += ZOM_FACTOR*Mouse.getDWheel();
        }
    }
    
    static void transform(){

        //Clears transform matrix
        glLoadIdentity();
               
        //Translation
        glTranslated(trsX, trsY, 0);
        
        //Zoom
        glScaled(zoom,zoom,zoom);

        //Rotation 
        glRotatef((float)rotX,-1, 0, 0);
        glRotatef((float)rotY, 0, 1, 0);
        glRotatef((float)rotZ, 0, 0, 1);
        
    }
    
    static void reset(){
        
        //Rotations
        rotX = rotY = rotZ = 0.0f;
        
        //Translations
        trsX = trsY = 0;
        
        //Scale
        zoom = 1;        
    }
    
    static void isRunning(){
        running = true;
    }
}


