package ic.apps.sph;

public class ParticleInfo{
    
    //Velocity
    private Vector velocity;
    //Acceleration
    private Vector accel;
    
    //Pressure
    private double pressure;
    //Desnity
    private double density;
    //Rest density
    private double restDensity;
    //Mass
    private double mass;
    
    //ID to differentiate different fluids
    private int fluidId;
    
    //Has this particle been counted already?
    //Created to avoid counting a particle multiple times when it exists and
    //reenters the obtacle
    public boolean counted;
    
    //Constructors
    public ParticleInfo() {
        this.velocity = new Vector(0.0d, 0.0d, 0.0d);
        this.accel = new Vector(0.0d, 0.0d, 0.0d);
        this.pressure = 0.0d;
        counted = false;
    }
    
    public ParticleInfo(double mass, int fluidId) {
        this.velocity = new Vector(0.0d, 0.0d, 0.0d);
        this.accel = new Vector(0.0d, 0.0d, 0.0d);
        this.pressure = 0.0d;
        this.density = 0.0d;
        this.restDensity = 0.0d;
        this.mass = mass;
        this.fluidId = fluidId;
        counted = false;
    }
    
    public ParticleInfo(double mass, int fluidId, Vector initVel) {
        this.velocity = new Vector(initVel.getVec());
        this.accel = new Vector(0.0d, 0.0d, 0.0d);
        this.pressure = 0.0d;
        this.density = 0.0d;
        this.restDensity = 0.0d;
        this.mass = mass;
        this.fluidId = fluidId;
    }
    
    //Getters
    public Vector getVelocity() {
        return velocity;
    }

    public Vector getAccel() {
        return accel;
    }

    public double getPressure() {
        return pressure;
    }
    
    public double getDensity() {
        return density;
    }

    public double getRestDensity() {
        return restDensity;
    }  

    public double getMass() {
        return mass;
    }
    
    public int getFluidId() {
        return fluidId;
    }
    
    //Setters
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void setAccel(Vector accel) {
        this.accel = accel;
    }   

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public void setRestDensity(double restDensity) {
        this.restDensity = restDensity;
    }  

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setFluidId(int fluidId) {
        this.fluidId = fluidId;
    }
    
    //toString for readabilty and debugging
    @Override
    public String toString(){
        return "["+restDensity+"; "+mass+"; "+density+"; "+pressure+"; "+velocity+"]";
    }
    
}
