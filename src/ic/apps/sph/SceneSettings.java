package ic.apps.sph;

//Imports
import ic.mlibs.util.Settings;

public class SceneSettings extends Settings{
    
    //Neighbor search radius
    private double coreRadius;
    //Leap-frog step
    private double gamma;
    //Collision damping
    private double damping;
    //Search grid cell side
    private double cellSide;
    //Obstacle porosity
    private double porosity;
    
    //Path of the .settings file for both fluids
    private String fluidSettingsFile_1;
    private String fluidSettingsFile_2;
    //Path of the .settings file for the obstacle
    private String obstacleFile;
    
    //Min point of the scene
    private Vector boundaryMin;
    //Max point of the scene
    private Vector boundaryMax;
    //Min point of the particle spawn volume
    private Vector spawnMin;
    //Max point of the particle spawn volume
    private Vector spawnMax;
    //Initial velocity of spawned particles
    private Vector spawnVel;
    //Gravity vector
    private Vector gravity;
    
    //Scene type
    private int scene;
    //Frequency of particle spawning
    private int spawnFreq;
    
    //Is there an obstacle?
    private boolean obstacle;
        
    //Constructor
    public SceneSettings (String fileName) {
        super(fileName);
    }

    //Loads values from settings file
    @Override
    protected void loadStrings() {
        fluidSettingsFile_1 = settings.getProperty("fluidSettingsFile_1");
        fluidSettingsFile_2 = settings.getProperty("fluidSettingsFile_2");
        obstacleFile = settings.getProperty("obstacleFile");
        boundaryMin = createVectorFromString(settings.getProperty("boundaryMin"));
        boundaryMax = createVectorFromString(settings.getProperty("boundaryMax"));
        gravity = createVectorFromString(settings.getProperty("gravity"));
        spawnMin = createVectorFromString(settings.getProperty("spawnMin"));
        spawnMax = createVectorFromString(settings.getProperty("spawnMax"));
        spawnVel = createVectorFromString(settings.getProperty("spawnVel"));
        scene = Integer.parseInt(settings.getProperty("scene"));
        spawnFreq = Integer.parseInt(settings.getProperty("spawnFreq"));
        coreRadius = Double.parseDouble(settings.getProperty("coreRadius"));
        gamma = Double.parseDouble(settings.getProperty("gamma"));
        damping = Double.parseDouble(settings.getProperty("damping"));
        cellSide = Double.parseDouble(settings.getProperty("cellSide"));
        porosity = Double.parseDouble(settings.getProperty("porosity"));
        obstacle = Boolean.parseBoolean(settings.getProperty("obstacle"));
    }

    //Saves values to settings file
    @Override
    protected void saveStrings() {
        settings.setProperty("fluidSettingsFile_1", fluidSettingsFile_1);
        settings.setProperty("fluidSettingsFile_2", fluidSettingsFile_2);
        settings.setProperty("obstacleFile", obstacleFile);
        settings.setProperty("boundaryMin", boundaryMin.toString());
        settings.setProperty("boundaryMax", boundaryMax.toString());
        settings.setProperty("gravity", gravity.toString());
        settings.setProperty("spawnMin", spawnMin.toString());
        settings.setProperty("spawnMax", spawnMax.toString());
        settings.setProperty("spawnVel", spawnVel.toString());
        settings.setProperty("scene", Integer.toString(scene));
        settings.setProperty("spawnFreq", Integer.toString(spawnFreq));
        settings.setProperty("coreRadius", Double.toString(coreRadius));
        settings.setProperty("gamma", Double.toString(gamma));
        settings.setProperty("damping", Double.toString(damping));
        settings.setProperty("cellSide", Double.toString(cellSide));
        settings.setProperty("porosity", Double.toString(porosity));
        settings.setProperty("obstacle", Boolean.toString(obstacle));
    }
    
    //Splits a string at its semicolons
    public String[] splitString (String s) {
        if (!s.isEmpty()){
            s = s.replaceAll(" ", "");
            return s.split(";", 0);
        } else {
            return null;
        }
        
    }
    
    //Concats strings adding semicolons between them
    public String concatStrings (String[] s){
        if (s.length > 0){
            String result = s[0];
            for (int i = 1; i < s.length; i++){
                result = result.concat("; ");
                result = result.concat(s[i]);
            }
            return result;
        } else {
            return null;
        }
    }
    
    //Creates a vector from a string
    public Vector createVectorFromString (String property){
        Vector result = new Vector(0.0f, 0.0f, 0.0f);
        property = property.replace(" ", "");
        property = property.replace("(", "");
        property = property.replace(")", "");
        String[] forcesArray = property.split(",");
        if (forcesArray.length % 3 != 0) {
            throw new IllegalArgumentException("One of the vectors is missing components");
        } else {
            for (int i = 0; i < forcesArray.length; i++) {
                switch (i % 3) {
                    case 0:
                        result.setVecData(Double.parseDouble(forcesArray[i]), 0);
                        break;
                    case 1:
                        result.setVecData(Double.parseDouble(forcesArray[i]), 1);
                        break;
                    case 2:
                        result.setVecData(Double.parseDouble(forcesArray[i]), 2);
                        break;
                }
            }
        }
        return result;
    }

    //Getters & setters

    public String getFluidSettingsFile_1() {
        return fluidSettingsFile_1;
    }

    public void setFluidSettingsFile_1(String fluidSettingsFile) {
        this.fluidSettingsFile_1 = fluidSettingsFile;
    }
    
    public String getFluidSettingsFile_2() {
        return fluidSettingsFile_2;
    }

    public void setFluidSettingsFile_2(String fluidSettingsFile) {
        this.fluidSettingsFile_2 = fluidSettingsFile;
    }

    public Vector getBoundaryMin() {
        return boundaryMin;
    }

    public void setBoundaryMin(Vector boundaryMin) {
        this.boundaryMin = boundaryMin;
    }

    public Vector getBoundaryMax() {
        return boundaryMax;
    }

    public void setBoundaryMax(Vector boundaryMax) {
        this.boundaryMax = boundaryMax;
    }

    public Vector getGravity() {
        return gravity;
    }

    public void setGravity(Vector gravity) {
        this.gravity = gravity;
    }

    public int getScene() {
        return scene;
    }

    public void setScene(int scene) {
        this.scene = scene;
    }
    
    public double getCoreRadius() {
        return coreRadius;
    }

    public void setCoreRadius (double coreRadius) {
        this.coreRadius = coreRadius;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getDamping() {
        return damping;
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }

    public double getCellSide() {
        return cellSide;
    }

    public void setCellSide(double cellSide) {
        this.cellSide = cellSide;
    }

    public Vector getSpawnMin() {
        return spawnMin;
    }

    public void setSpawnMin(Vector spawnMin) {
        this.spawnMin = spawnMin;
    }

    public Vector getSpawnMax() {
        return spawnMax;
    }

    public void setSpawnMax(Vector spawnMax) {
        this.spawnMax = spawnMax;
    }

    public int getSpawnFreq() {
        return spawnFreq;
    }

    public void setSpawnFreq(int spawnFreq) {
        this.spawnFreq = spawnFreq;
    }

    public Vector getSpawnVel() {
        return spawnVel;
    }

    public void setSpawnVel(Vector spawnVel) {
        this.spawnVel = spawnVel;
    }

    public String getObstacleFile() {
        return obstacleFile;
    }

    public void setObstacleFile(String obstacleFile) {
        this.obstacleFile = obstacleFile;
    }    

    public double getPorosity() {
        return porosity;
    }

    public void setPorosity(double porosity) {
        this.porosity = porosity;
    }

    public boolean hasObstacle() {
        return obstacle;
    }

    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }    
    
}