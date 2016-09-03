
package ic.apps.sph;

import ic.mlibs.util.Settings;

public class FluidSettings extends Settings{
    
    //Mass of a particle
    private double mass;
    //Density of a particle
    private double density;
    //Gas constant (k, derived from ideal gas state law)
    private double gasConstant;
    //Dynamic viscosity coefficient
    private double viscosity;
    
    //Min (bottom-left-back) position of the initial fluid volume
    private Vector volumeMin;
    //Max (top-right-front) position of the initial fluid volume
    private Vector volumeMax;
    //Initial spacing between particles in each direction
    private Vector spacing;
    
    public FluidSettings (String fileName){
        super (fileName);
    }
    
    //Loads values form the settings file
    @Override
    protected void loadStrings() {
        
        mass = Double.parseDouble(settings.getProperty("mass"));
        gasConstant = Double.parseDouble(settings.getProperty("gasConstant"));
        viscosity = Double.parseDouble(settings.getProperty("viscosity"));
        volumeMin = createVectorFromString(settings.getProperty("volumeMin"));
        volumeMax = createVectorFromString(settings.getProperty("volumeMax"));
        spacing = createVectorFromString(settings.getProperty("spacing"));
        
    }
    
    //Saves values to the settings file
    @Override
    protected void saveStrings() {
        
        settings.setProperty("mass", Double.toString(mass));
        settings.setProperty("gasConstant", Double.toString(gasConstant));
        settings.setProperty("viscosity", Double.toString(viscosity));
        settings.setProperty("volumeMin", volumeMin.toString());
        settings.setProperty("volumeMax", volumeMax.toString());
        settings.setProperty("spacing", spacing.toString());
        
    }
    
    //Getters
    public double getMass() {
        return mass;
    }

    public double getDensity() {
        return density;
    }

    public double getGasConstant() {
        return gasConstant;
    }

    public double getViscosity() {
        return viscosity;
    }

    public Vector getVolumeMin() {
        return volumeMin;
    }

    public Vector getVolumeMax() {
        return volumeMax;
    }

    public Vector getSpacing() {
        return spacing;
    }
    
    //Setters
    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public void setGasConstant(double gasConstant) {
        this.gasConstant = gasConstant;
    }

    public void setViscosity(double viscosity) {
        this.viscosity = viscosity;
    }

    public void setVolumeMin(Vector volumeMin) {
        this.volumeMin = volumeMin;
    }

    public void setVolumeMax(Vector volumeMax) {
        this.volumeMax = volumeMax;
    }    

    public void setSpacing(Vector spacing) {
        this.spacing = spacing;
    }
    
    //Generates a Vector object from a string
    public Vector createVectorFromString (String property){
        
        //Creates empty vector
        Vector result = new Vector(0.0f, 0.0f, 0.0f);
        
        //Removes spaces and parenthesis
        property = property.replace(" ", "");
        property = property.replace("(", "");
        property = property.replace(")", "");
        
        //Splits string at the commas
        String[] forcesArray = property.split(",");
        
        //Throws excpetion if splitting generated less than 3 strings
        if (forcesArray.length % 3 != 0) {
            throw new IllegalArgumentException("One of the vectors is missing components");
        } else {
            //Parses strings to floats and set them to Vector coordinates
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
    
}
