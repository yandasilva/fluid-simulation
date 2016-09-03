package ic.apps.gui;

//Imports
import ic.mlibs.util.Settings;
//LWJGL
import org.lwjgl.BufferUtils;
//Java
import java.nio.FloatBuffer;

public class GuiSettings extends Settings{
    
    //Window
    private int width ; //Window width
    private int height; //Window height
    private int posX  ; //Window x position
    private int posY  ; //Window y position
    
    //First light source
    private FloatBuffer light0Amb; //Ambient
    private FloatBuffer light0Dif; //Diffuse
    private FloatBuffer light0Esp; //Specular
    private FloatBuffer light0Pos; //Position
    
    //Second light source
    private FloatBuffer light1Amb; //Ambient
    private FloatBuffer light1Dif; //Diffuse
    private FloatBuffer light1Esp; //Specular
    private FloatBuffer light1Pos; //Position
    
    //Material
    private Float materialShi;
        
    //Render
    private boolean showAxis;
    private boolean showBox;

    //Constructor
    public GuiSettings(String fileName){
        super(fileName);
    }
    
    //Getters
    public FloatBuffer getLight0Amb() {
        return light0Amb;
    }

    public FloatBuffer getLight0Dif() {
        return light0Dif;
    }
    
    public FloatBuffer getLight0Esp() {
        return light0Esp;
    }
    
    public FloatBuffer getLight0Pos() {
        return light0Pos;
    }
    
    public FloatBuffer getLight1Amb() {
        return light1Amb;
    }

    public FloatBuffer getLight1Dif() {
        return light1Dif;
    }

    public FloatBuffer getLight1Esp() {
        return light1Esp;
    }

    public FloatBuffer getLight1Pos() {
        return light1Pos;
    }
        
    public Float getMaterialShi() {
        return materialShi;
    }
    
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
        
    public boolean isShowAxis() {
        return showAxis;
    }

    public boolean isShowBox() {
        return showBox;
    }

    //Overrides
    
    @Override
    protected void clearSettings() {
        //Zeroes window settigns
        width  = 0;
        height = 0;
        posX   = 0;
        posY   = 0;
        
        //Zeroes first light source settings
        light0Amb = null;
        light0Dif = null;
        light0Esp = null;
        light0Pos = null;
        
        //Zeroes second light source settings1
        light1Amb = null;
        light1Dif = null;
        light1Esp = null;
        light1Pos = null;
        
        //Zeroes material settings
        materialShi = 0.0f;
     }
    
    //Loads values to the settings file
    @Override
    protected void loadStrings() {
        
        width  = Integer.parseInt( settings.getProperty("width" )  );
        height = Integer.parseInt( settings.getProperty("height")  );
        posX   = Integer.parseInt( settings.getProperty("posX"  )  );
        posY   = Integer.parseInt( settings.getProperty("posY"  )  );
        
        light0Amb = BufferUtils.createFloatBuffer(4);
        light0Amb.put(0, Float.parseFloat( settings.getProperty("light0AmbR" ) ) );
        light0Amb.put(1, Float.parseFloat( settings.getProperty("light0AmbG" ) ) );
        light0Amb.put(2, Float.parseFloat( settings.getProperty("light0AmbB" ) ) );
        light0Amb.put(3, Float.parseFloat( settings.getProperty("light0AmbA" ) ) );
        
        light0Dif = BufferUtils.createFloatBuffer(4);
        light0Dif.put(0, Float.parseFloat( settings.getProperty("light0DifR" ) ) );
        light0Dif.put(1, Float.parseFloat( settings.getProperty("light0DifG" ) ) );
        light0Dif.put(2, Float.parseFloat( settings.getProperty("light0DifB" ) ) );
        light0Dif.put(3, Float.parseFloat( settings.getProperty("light0DifA" ) ) );
        
        light0Esp = BufferUtils.createFloatBuffer(4);
        light0Esp.put(0, Float.parseFloat( settings.getProperty("light0EspR" ) ) );
        light0Esp.put(1, Float.parseFloat( settings.getProperty("light0EspG" ) ) );
        light0Esp.put(2, Float.parseFloat( settings.getProperty("light0EspB" ) ) );
        light0Esp.put(3, Float.parseFloat( settings.getProperty("light0EspA" ) ) );
        
        light0Pos = BufferUtils.createFloatBuffer(4);
        light0Pos.put(0, Float.parseFloat( settings.getProperty("light0PosX" ) ) );
        light0Pos.put(1, Float.parseFloat( settings.getProperty("light0PosY" ) ) );
        light0Pos.put(2, Float.parseFloat( settings.getProperty("light0PosZ" ) ) );
        light0Pos.put(3, Float.parseFloat( settings.getProperty("light0PosW" ) ) );
        
        light1Amb = BufferUtils.createFloatBuffer(4);
        light1Amb.put(0, Float.parseFloat( settings.getProperty("light1AmbR" ) ) );
        light1Amb.put(1, Float.parseFloat( settings.getProperty("light1AmbG" ) ) );
        light1Amb.put(2, Float.parseFloat( settings.getProperty("light1AmbB" ) ) );
        light1Amb.put(3, Float.parseFloat( settings.getProperty("light1AmbA" ) ) );

        light1Dif = BufferUtils.createFloatBuffer(4);
        light1Dif.put(0, Float.parseFloat( settings.getProperty("light1DifR" ) ) );
        light1Dif.put(1, Float.parseFloat( settings.getProperty("light1DifG" ) ) );
        light1Dif.put(2, Float.parseFloat( settings.getProperty("light1DifB" ) ) );
        light1Dif.put(3, Float.parseFloat( settings.getProperty("light1DifA" ) ) );
        
        light1Esp = BufferUtils.createFloatBuffer(4);
        light1Esp.put(0, Float.parseFloat( settings.getProperty("light1EspR" ) ) );
        light1Esp.put(1, Float.parseFloat( settings.getProperty("light1EspG" ) ) );
        light1Esp.put(2, Float.parseFloat( settings.getProperty("light1EspB" ) ) );
        light1Esp.put(3, Float.parseFloat( settings.getProperty("light1EspA" ) ) );
        
        light1Pos = BufferUtils.createFloatBuffer(4);
        light1Pos.put(0, Float.parseFloat( settings.getProperty("light1PosX" ) ) );
        light1Pos.put(1, Float.parseFloat( settings.getProperty("light1PosY" ) ) );
        light1Pos.put(2, Float.parseFloat( settings.getProperty("light1PosZ" ) ) );
        light1Pos.put(3, Float.parseFloat( settings.getProperty("light1PosW" ) ) );
        
        materialShi = Float.parseFloat( settings.getProperty("materialShi" ) );

        showAxis = Boolean.parseBoolean( settings.getProperty("showAxis" ) );
        showBox  = Boolean.parseBoolean( settings.getProperty("showBox"  ) );
    }
    
    //Saves values to the settings file
    @Override
    protected void saveStrings() {

        settings.setProperty("width" , Integer.toString(width) ) ;
        settings.setProperty("height", Integer.toString(height)) ;
        settings.setProperty("posX"  , Integer.toString(posX)) ;
        settings.setProperty("posY"  , Integer.toString(posY)) ;
    
        settings.setProperty("light0AmbR", Float.toString( light0Amb.get(0) ) ); 
        settings.setProperty("light0AmbG", Float.toString( light0Amb.get(1) ) );
        settings.setProperty("light0AmbB", Float.toString( light0Amb.get(2) ) );
        settings.setProperty("light0AmbA", Float.toString( light0Amb.get(3) ) );
        
        settings.setProperty("light0DifR", Float.toString( light0Dif.get(0) ) ); 
        settings.setProperty("light0DifG", Float.toString( light0Dif.get(1) ) );
        settings.setProperty("light0DifB", Float.toString( light0Dif.get(2) ) );
        settings.setProperty("light0DifA", Float.toString( light0Dif.get(3) ) );
        
        settings.setProperty("light0EspR", Float.toString( light0Esp.get(0) ) ); 
        settings.setProperty("light0EspG", Float.toString( light0Esp.get(1) ) );
        settings.setProperty("light0EspB", Float.toString( light0Esp.get(2) ) );
        settings.setProperty("light0EspA", Float.toString( light0Esp.get(3) ) );
        
        settings.setProperty("light0PosX", Float.toString( light0Pos.get(0) ) ); 
        settings.setProperty("light0PosY", Float.toString( light0Pos.get(1) ) );
        settings.setProperty("light0PosZ", Float.toString( light0Pos.get(2) ) );
        settings.setProperty("light0PosW", Float.toString( light0Pos.get(3) ) );
        
        settings.setProperty("light1AmbR", Float.toString( light1Amb.get(0) ) ); 
        settings.setProperty("light1AmbG", Float.toString( light1Amb.get(1) ) );
        settings.setProperty("light1AmbB", Float.toString( light1Amb.get(2) ) );
        settings.setProperty("light1AmbA", Float.toString( light1Amb.get(3) ) );
        
        settings.setProperty("light1DifR", Float.toString( light1Dif.get(0) ) ); 
        settings.setProperty("light1DifG", Float.toString( light1Dif.get(1) ) );
        settings.setProperty("light1DifB", Float.toString( light1Dif.get(2) ) );
        settings.setProperty("light1DifA", Float.toString( light1Dif.get(3) ) );
        
        settings.setProperty("light1EspR", Float.toString( light1Esp.get(0) ) ); 
        settings.setProperty("light1EspG", Float.toString( light1Esp.get(1) ) );
        settings.setProperty("light1EspB", Float.toString( light1Esp.get(2) ) );
        settings.setProperty("light1EspA", Float.toString( light1Esp.get(3) ) );
         
        settings.setProperty("light1PosX", Float.toString( light1Pos.get(0) ) ); 
        settings.setProperty("light1PosY", Float.toString( light1Pos.get(1) ) );
        settings.setProperty("light1PosZ", Float.toString( light1Pos.get(2) ) );
        settings.setProperty("light1PosW", Float.toString( light1Pos.get(3) ) );
       
        settings.setProperty("materialShi", Float.toString( materialShi ) ); 
       
        settings.setProperty("showAxis", Boolean.toString( showAxis ) ); 
        settings.setProperty("showBox" , Boolean.toString( showBox  ) ); 
    }
}
