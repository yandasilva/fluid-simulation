package ic.apps.sph;

import ic.mlibs.util.Settings;

public class SPHAppSettings extends Settings{
    
    //Path of the .off file with the tank geometric information
    private String mfile;
    //Render mode
    private String rmode;
    //Color map
    private String cmap;
    //Path of the SceneSettings.settings file
    private String sceneSettingsFile;
    //Color mode (simple, pressure or velocity)
    private String colorMode;
    //Should the program generate scene statistics?
    private String generateStats;
    
    //Color map min and max
    private double cmapMin;
    private double cmapMax;
    //Min and max Z to render
    private double renderMinZ;
    private double renderMaxZ;
    
    //Constructors
    public SPHAppSettings(String mfile, String rmode, String cmap, double cmapMin, double cmapMax, String arq) {
        super(arq);
        this.mfile = mfile;
        this.rmode = rmode;
        this.cmap = cmap;
        this.cmapMin = cmapMin;
        this.cmapMax = cmapMax;
    }
    
    public SPHAppSettings(String fileName) {
        super(fileName);
    }

    //Getters
    public String getMfile() {
        return mfile;
    }

    public String getRmode() {
        return rmode;
    }

    public String getCmap() {
        return cmap;
    }

    public String getSceneSettingsFile() {
        return sceneSettingsFile;
    }

    public double getCmapMin() {
        return cmapMin;
    }

    public double getCmapMax() {
        return cmapMax;
    }

    public String getColorMode() {
        return colorMode;
    }

    public double getRenderMinZ() {
        return renderMinZ;
    }

    public double getRenderMaxZ() {
        return renderMaxZ;
    }    

    public String getGenerateStats() {
        return generateStats;
    }

    //Setters
    public void setMfile(String mfile) {
        this.mfile = mfile;
    }

    public void setRmode(String rmode) {
        this.rmode = rmode;
    }

    public void setCmap(String cmap) {
        this.cmap = cmap;
    }

    public void setSceneSettingsFile(String sceneSettingsFile) {
        this.sceneSettingsFile = sceneSettingsFile;
    }

    public void setCmapMin(double cmapMin) {
        this.cmapMin = cmapMin;
    }

    public void setCmapMax(double cmapMax) {
        this.cmapMax = cmapMax;
    }

    public void setColorMode(String colorMode) {
        this.colorMode = colorMode;
    }

    public void setRenderMinZ(double renderMinZ) {
        this.renderMinZ = renderMinZ;
    }

    public void setRenderMaxZ(double renderMaxZ) {
        this.renderMaxZ = renderMaxZ;
    }

    public void setGenerateStats(String generateStats) {
        this.generateStats = generateStats;
    }    
    
    //Clear values
    @Override
    protected void clearSettings() {
        mfile = null;
        rmode   = null;
        cmap    = null;
        sceneSettingsFile = null;
        cmapMin = 0.0f;
        cmapMax = 0.0f;
        super.clearSettings();
    }

    //Load values from the file
    @Override
    protected void loadStrings() {
        mfile = settings.getProperty("mfile");
        rmode = settings.getProperty("rmode");
        cmap = settings.getProperty("cmap");
        colorMode = settings.getProperty("colorMode");
        sceneSettingsFile = settings.getProperty("sceneSettingsFile");
        generateStats = settings.getProperty("generateStats");
        cmapMin = Double.parseDouble(settings.getProperty("cmapMin"));
        cmapMax = Double.parseDouble(settings.getProperty("cmapMax"));
        renderMinZ = Double.parseDouble(settings.getProperty("renderMinZ"));
        renderMaxZ = Double.parseDouble(settings.getProperty("renderMaxZ"));
    }

    //Save values to the file
    @Override
    protected void saveStrings() {
        settings.setProperty("mfile" , mfile);
        settings.setProperty("rmode", rmode);
        settings.setProperty("cmap", cmap);
        settings.setProperty("colorMode", colorMode);
        settings.setProperty("sceneSettingsFile", sceneSettingsFile);
        settings.setProperty("generateStats", generateStats);
        settings.setProperty("cmapMin", Double.toString(cmapMin));
        settings.setProperty("cmapMax", Double.toString(cmapMax));
        settings.setProperty("renderMinZ", Double.toString(renderMinZ));
        settings.setProperty("renderMaxZ", Double.toString(renderMaxZ));
    }
    
}
