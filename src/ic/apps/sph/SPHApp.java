package ic.apps.sph;

//Utilities
import ic.mlibs.util.Application;

//Java
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.imageio.ImageIO;

//LWJGL
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class SPHApp extends SPHAppSettings implements Application {

    //Scene object containing fluid and obstacle
    private Scene scene;
    
    //Current and total number of steps
    private int steps = 0;
    private final double maxSteps = 751;

    //Is the application running?
    private boolean play = false;

    //Timestamps for file naming and logs
    long startTime = 0;
    long timeBefore = 0;
    long timeAfter = 0;
    long average = 0;
    
    //Directories for scene screenshots, log and statistics
    private String imageDir = "";
    private String timeLog = "";
    private String statsDir = "";

    //Has the scene log been printed?
    boolean printed = false;

    //Constructor
    public SPHApp(String confFile) {
        super(confFile);
    }

    //Create the app
    @Override
    public void create() throws Exception {
        try {
            //Creates a file directory
            createDir();
            //Creates the scene
            if (getGenerateStats().equalsIgnoreCase("yes")){
                scene = new Scene(getSceneSettingsFile(), getColorMode(), 
                    getRenderMinZ(), getRenderMaxZ(), statsDir);
            } else {
                scene = new Scene(getSceneSettingsFile(), getColorMode(), 
                    getRenderMinZ(), getRenderMaxZ(), null);
            }
            //Creates the keyboard
            Keyboard.create();

        } catch (LWJGLException e) {
            destroy();
            System.out.println("Error in ExApp::create");
        }
    }

    //Destroys the app
    @Override
    public void destroy() {
        scene = null;
        clearSettings();
    }
    
    //Runs and stops the app
    @Override
    public void run() {
        play = !play;
        if (play) {
            System.out.print("Starting the simulation...");
        } else {
            System.out.println("Stoping the simulation...");
        }
    }

    //Render method being continually called
    @Override
    public void render() {
        //If the app is running, the scene is ready and there are still steps left
        if (play && scene.hasStarted() && steps < maxSteps) {
            //If left shift is not being pressed (pause key)
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                //Updates the scene and increments the # of steps
                scene.update();
                steps++;
            }
        //If it has reached the max # of steps but not printed the log yet
        } else if (steps == maxSteps && !printed) {
            //If the scene generated statistics
            if (getGenerateStats().equalsIgnoreCase("yes")){
                //Close written statistics files
                scene.closeFiles();
            }
            //Print the scene log to a file
            writeLog();
            printed = true;
        }
        //Render the scene
        scene.render(getCmap(), getRmode(), getCmapMin(), getCmapMax());
        if (((steps % 250) == 0) && (steps != 0)){
            //Take a screenshot of the scene on steps 0, 250, 500, 750...
            screenShot();
        }
    }

    //Reloads the app settings and recreates the scene
    @Override
    public void reset() {
        loadSettings();
        scene = new Scene(getSceneSettingsFile(), getColorMode(), 
                    getRenderMinZ(), getRenderMaxZ(), statsDir);
    }
    
    //Reloads the app settings
    @Override
    public void reload() {
        loadSettings();
    }

    //Write the scene log to a file
    public void writeLog() {
        BufferedWriter writer = null;
        try {
            File logFile = new File("log/results/" + timeLog);

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("LOG DA SIMULAÇÃO:\n");
            writer.write("\n");
            writer.write("FLUIDO 1:" + "\n");
            writer.write("Número de partículas (fluido 1): " + scene.getFluid().getFirstFluidParticles()+ "\n");
            writer.write("Número de partículas (fluido 2): " + scene.getFluid().getFirstFluidParticles()+ "\n");
            writer.write("Número de partículas inseridas: " + scene.getParticlesIn()+ "\n");
            writer.write("Número de partículas removidas: " + scene.getParticlesOut()+ "\n");
            double pctg = (double) ((double) scene.getParticlesOut()/ (double) scene.getParticlesIn()) * 100d;
            DecimalFormat formatter = new DecimalFormat("#0.00");
            writer.write("As partículas removidas correspondem a " + formatter.format(pctg) + 
                    "% das partículas inseridas.");
        } catch (IOException e) {
        } finally {
            try {
                writer.close();
            } catch (IOException e) {

            }
        }
    }

    //Creates the file directories for the screenshots and the statistics
    private void createDir(){
        timeLog = new SimpleDateFormat("ddMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
        imageDir = "simulations/" + timeLog + "/";
        statsDir = "log/stats/" + timeLog + "/";
        File imgDir = new File(imageDir);
        File stsDir = new File(statsDir);
        imgDir.mkdir();
        stsDir.mkdir();
    }
    
    //Writes the screen to a PNG and saves it
    private void screenShot() {
        GL11.glReadBuffer(GL11.GL_FRONT);
        int height = Display.getDisplayMode().getHeight();
        int width = Display.getDisplayMode().getWidth();
        int bpp = 4;
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        File file = new File(String.format(imageDir+"%03d", steps));
        String format = "PNG";
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
        }
        
    }
}

