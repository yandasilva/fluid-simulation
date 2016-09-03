package ic.apps.sph;

//Java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Scene {

//----------- Auxiliary consts -----------
    final private static int X = 0;
    final private static int Y = 1;
    final private static int Z = 2;
    Vector zero = new Vector(0.0d, 0.0d, 0.0d);

//-----------  Settings files -----------
    private SceneSettings sceneSettings; //Scene settings
    private FluidSettings fluidSettings_1; //First fluid settings
    private FluidSettings fluidSettings_2; //Second fluid settings

//----------- Scene structures -----------
    private ParticleSystem fluid; //Fluid (state on step n - 1)
    private final ParticleSystem nextFluid; //Fluid (state on step n)
    private Vector tankMin; //Tank min point
    private Vector tankMax; //Tank max point
    private Obstacle obstacle; //Obstacle

//----------- Log variables -----------
    private double minPressure = Double.MAX_VALUE; //Min pressure of the step
    private double maxPressure = Double.MIN_VALUE; //Max pressure of the step
    private double minVelocity; //Min speed of the step
    private double maxVelocity; //Max speed of the step
    private double sceneMinPressure = Double.MAX_VALUE; //Min average pressure of the scene
    private double sceneMaxPressure = Double.MIN_VALUE; //Max average pressure of the scene
    private double sceneMinVelocity = Double.MAX_VALUE; //Min average speed of the scene
    private double sceneMaxVelocity = Double.MIN_VALUE; //Max average speed of the scene
    private double sceneMinDensity = Double.MAX_VALUE; //Min average density of the scene
    private double sceneMaxDensity = Double.MIN_VALUE; //Max average densit of the scene
    private double sceneAvgPressure = 0; //Average pressure of the scene
    private double sceneAvgVelocity = 0; //Average speed of the scene
    private double sceneAvgDensity = 0; //Average density of the scene
    private double sceneAvgNeighs = 0; //Average # of neighbors of the scene
    private double vel; //Auxiliar variable for velocity
    private int minNeighs; //Min # of neighbors of the step
    private int maxNeighs; //Max # of neighbors of the step
    private int sceneMinNeighs = Integer.MAX_VALUE; //Min # of neighbors of the scene
    private int sceneMaxNeighs = Integer.MIN_VALUE; //Max # of neighbors of the scene
    private int particlesIn = 0; //# of particles that entered the obstacle
    private int particlesOut = 0; //# of particles that exited the obstacle
    private double pressureAcc = 0; //Pressure accumulator
    private double velocityAcc = 0; //Velocity accumulator
    private final boolean counted[]; //Particles that have been counted
    private FileWriter fwP; //FileWriter for the pressure statistics file
    private BufferedWriter bwP; //BufferedWriter for the pressure statistics file
    private FileWriter fwV; //FileWriter for the velocity statistics file
    private BufferedWriter bwV; //BufferedWriter for the velocity statistics file
    private boolean files = false; //Did the program create statistics files?

//----------- Simultion and grid constants -----------
    private final int totalParticles; //Total # of particles
    private final double coreRadius; //Radius of search
    private final double cPolyKernel; //Constant part of the polynomial kernel
    private final double cSpikyKernel; //Constant part of the spiky kernel
    private final double cViscosityKernel; //Constant part of the viscosity kernel
    private final double gamma; //Leap-frog integration step
    private final double damping; //Colision damping
    private final double cellSide; //Size of a search cell side
    private final double renderMinZ; //Min Z of the rendered slice
    private final double renderMaxZ; //Max Z of the rendered slice
    private final Vector gravity; //Gravity vector
    private final String colorMode; //Color mode
    private boolean started = false; //Has the simulation started?
    private boolean hasSpawn = false; //Are there particles to spawn?
    private int step; //Current step
    private int freq; //Frequency of particle spawns
    private SearchGrid searchGrid; //Search grid

//----------- Fluid constants -----------
    private final double mass_1;
    private final double mass_2;
    private final double viscosity_1;
    private final double viscosity_2;
    private final double pressureConstant_1;
    private final double pressureConstant_2;

//----------- Temporary variables (for optimization) -----------
    boolean[] changed;
    int quant;
    int neighPart;
    int particle_neighbors;
    double density;
    double pressure;
    double distance;
    double normR;
    double scale;
    double vKernel;
    double mass;
    double escalar;
    double velocityNorm;
    double color;
    double x;
    double y;
    double z;
    boolean collided;
    Vector posI;
    Vector posJ;
    Vector velDiff;
    Vector velocity;
    Vector r;
    Vector result = new Vector(3);
    Vector resultantForce;
    Vector pressureForce = new Vector(3);
    Vector viscosityForce = new Vector(3);
    Vector tensionForce;
    Vector accel = new Vector(3);
    Vector oldPos;
    Vector newPos;
    Vector auxNewPos;
    Vector auxOldPos;
    CollisionResult collision;
    ArrayList<Integer> neighs;
    Iterator itrNeighs;
    Particle particle;
    ParticleInfo newInfo;
    ParticleInfo neighInfo;
    ParticleInfo infoI;
    ParticleInfo infoJ;
    Particle partI;
    Particle partJ;
    

//----------- Getters & setters -----------
    public Vector getTankMin() {
        return tankMin;
    }

    public void setTankMin(Vector tankMin) {
        this.tankMin = tankMin;
    }

    public Vector getTankMax() {
        return tankMax;
    }

    public void setTankMax(Vector tankMax) {
        this.tankMax = tankMax;
    }

    public SceneSettings getSceneSettings() {
        return sceneSettings;
    }

    public void setSceneSettings(SceneSettings sceneSettings) {
        this.sceneSettings = sceneSettings;
    }

    public FluidSettings getFluidSettings_1() {
        return fluidSettings_1;
    }

    public void setFluidSettings_1(FluidSettings fluidSettings) {
        this.fluidSettings_1 = fluidSettings;
    }

    public FluidSettings getFluidSettings_2() {
        return fluidSettings_2;
    }

    public void setFluidSettings_2(FluidSettings fluidSettings) {
        this.fluidSettings_2 = fluidSettings;
    }

    public ParticleSystem getFluid() {
        return fluid;
    }

    public void setFluid(ParticleSystem fluid) {
        this.fluid = fluid;
    }

    public double getSceneMinPressure() {
        return sceneMinPressure;
    }

    public void setSceneMinPressure(double sceneMinPressure) {
        this.sceneMinPressure = sceneMinPressure;
    }

    public double getSceneMaxPressure() {
        return sceneMaxPressure;
    }

    public void setSceneMaxPressure(double sceneMaxPressure) {
        this.sceneMaxPressure = sceneMaxPressure;
    }

    public double getSceneMinVelocity() {
        return sceneMinVelocity;
    }

    public void setSceneMinVelocity(double sceneMinVelocity) {
        this.sceneMinVelocity = sceneMinVelocity;
    }

    public double getSceneMaxVelocity() {
        return sceneMaxVelocity;
    }

    public void setSceneMaxVelocity(double sceneMaxVelocity) {
        this.sceneMaxVelocity = sceneMaxVelocity;
    }

    public double getSceneMinDensity() {
        return sceneMinDensity;
    }

    public void setSceneMinDensity(double sceneMinDensity) {
        this.sceneMinDensity = sceneMinDensity;
    }

    public double getSceneMaxDensity() {
        return sceneMaxDensity;
    }

    public void setSceneMaxDensity(double sceneMaxDensity) {
        this.sceneMaxDensity = sceneMaxDensity;
    }

    public int getSceneMinNeighs() {
        return sceneMinNeighs;
    }

    public void setSceneMinNeighs(int sceneMinNeighs) {
        this.sceneMinNeighs = sceneMinNeighs;
    }

    public int getSceneMaxNeighs() {
        return sceneMaxNeighs;
    }

    public void setSceneMaxNeighs(int sceneMaxNeighs) {
        this.sceneMaxNeighs = sceneMaxNeighs;
    }

    public double getSceneAvgPressure() {
        return sceneAvgPressure;
    }

    public void setSceneAvgPressure(double sceneAvgPressure) {
        this.sceneAvgPressure = sceneAvgPressure;
    }

    public double getSceneAvgVelocity() {
        return sceneAvgVelocity;
    }

    public void setSceneAvgVelocity(double sceneAvgVelocity) {
        this.sceneAvgVelocity = sceneAvgVelocity;
    }

    public double getSceneAvgDensity() {
        return sceneAvgDensity;
    }

    public void setSceneAvgDensity(double sceneAvgDensity) {
        this.sceneAvgDensity = sceneAvgDensity;
    }

    public double getSceneAvgNeighs() {
        return sceneAvgNeighs;
    }

    public void setSceneAvgNeighs(double sceneAvgNeighs) {
        this.sceneAvgNeighs = sceneAvgNeighs;
    }

    public SearchGrid getSearchGrid() {
        return searchGrid;
    }

    public void setSearchGrid(SearchGrid searchGrid) {
        this.searchGrid = searchGrid;
    }

    public boolean hasStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public int getParticlesIn() {
        return particlesIn;
    }

    public void setParticlesIn(int particlesIn) {
        this.particlesIn = particlesIn;
    }

    public int getParticlesOut() {
        return particlesOut;
    }

    public void setParticlesOut(int particlesOut) {
        this.particlesOut = particlesOut;
    }


    //----------- Constructor -----------
    public Scene(String sceneSettingsFile, String colorMode, double renderMinZ,
            double renderMaxZ, String statsDir) {

        //Creates scene objects from settings files
        this.sceneSettings = new SceneSettings(sceneSettingsFile);
        this.fluidSettings_1 = new FluidSettings(sceneSettings.getFluidSettingsFile_1());
        this.fluidSettings_2 = new FluidSettings(sceneSettings.getFluidSettingsFile_2());

        //Set tank boundaries
        tankMin = sceneSettings.getBoundaryMin();
        tankMax = sceneSettings.getBoundaryMax();

        //Creates the obstacle
        if (sceneSettings.hasObstacle()) {
            obstacle = new Obstacle(tankMin, tankMax, sceneSettings.getPorosity(),
                    sceneSettings.getObstacleFile());
        }
        //Inicializa o fluido
        if (sceneSettings.hasObstacle()) {
            fluid = new ParticleSystem(sceneSettings, fluidSettings_1, fluidSettings_2,
                    obstacle);
        } else {
            fluid = new ParticleSystem(sceneSettings, fluidSettings_1, fluidSettings_2,
                    null);
        }
        nextFluid = fluid;

        //Creates an array to signal particles that changed cells on the search grid
        changed = new boolean[fluid.getNumParticles()];

        //Criates scene min and max points from settings files
        Vector sceneMin = new Vector(sceneSettings.getBoundaryMin().getVecData(X),
                sceneSettings.getBoundaryMin().getVecData(Y),
                sceneSettings.getBoundaryMin().getVecData(Z));
        Vector sceneMax = new Vector(sceneSettings.getBoundaryMax().getVecData(X),
                sceneSettings.getBoundaryMax().getVecData(Y),
                sceneSettings.getBoundaryMax().getVecData(Z));

        //Stores scene infos
        gravity = sceneSettings.getGravity();
        coreRadius = sceneSettings.getCoreRadius();
        gamma = sceneSettings.getGamma();
        damping = sceneSettings.getDamping();
        cellSide = sceneSettings.getCellSide();

        //Stores fluid infos
        mass_1 = fluidSettings_1.getMass();
        mass_2 = fluidSettings_2.getMass();
        viscosity_1 = fluidSettings_1.getViscosity();
        viscosity_2 = fluidSettings_2.getViscosity();
        pressureConstant_1 = fluidSettings_1.getGasConstant();
        pressureConstant_2 = fluidSettings_2.getGasConstant();
        totalParticles = fluid.getNumParticles();

        //Calculates constant parts of the kernels
        cPolyKernel = (315.0d / (64.0d * Math.PI * Math.pow(coreRadius, 9)));
        cSpikyKernel = (-45.0d / (Math.PI * Math.pow(coreRadius, 6)));
        cViscosityKernel = (45.0d / (Math.PI * Math.pow(coreRadius, 6)));

        //Creates the search grid
        Vector margin = new Vector(1.0d, 1.0d, 1.0d);
        searchGrid = new SearchGrid(sceneMin.opSub(margin), sceneMax.opAdd(margin), cellSide);

        //Adds particles to the search grid
        for (int i = 0; i < totalParticles; i++) {
            searchGrid.addParticle(i, fluid.getG(i).getPos());
        }

        //Calculates particles' initial densities
        calculateInitialDensities();

        //Signals that the scene is ready
        this.started = !this.started;
        fluid.init = true;
        
        //Checks if the scene has particle spawn
        if (sceneSettings.getScene() != 1) {
            hasSpawn = true;
            freq = sceneSettings.getSpawnFreq();
        }

        //Initializes the current step as 0
        step = 0;

        //Initializes the array of particles that changed cells
        counted = new boolean[fluid.getNumParticles()];
        for (int i = 0; i < fluid.getNumParticles(); i++) {
            counted[i] = false;
        }

        //Initializes render variables
        this.colorMode = colorMode;
        this.renderMinZ = renderMinZ;
        this.renderMaxZ = renderMaxZ;

        //If the program should write statistics, prepare the file IO
        if (statsDir != null) {
            try {
                fwP = new FileWriter(statsDir + "pressure");
                fwV = new FileWriter(statsDir + "velocity");
                bwP = new BufferedWriter(fwP);
                bwV = new BufferedWriter(fwV);
                files = true;
            } catch (IOException ex) {
            }
        } else {
            files = false;
        }

    }

    //----------- Main loop -----------
    public void update() {
        //Initializes log variables
        minPressure = Double.MAX_VALUE;
        minVelocity = Double.MAX_VALUE;
        maxPressure = -1.0 * Double.MIN_VALUE;
        maxVelocity = -1.0 * Double.MIN_VALUE;
        minNeighs = Integer.MAX_VALUE;
        maxNeighs = -1 * Integer.MAX_VALUE;
        pressureAcc = 0;
        velocityAcc = 0;
        
        //If particles should be spawned this step, spawn them
        if (hasSpawn) {
            if (step % freq == 0) {
                fluid.spawnParticles(searchGrid);
                calculateInitialDensities();
            }
        }
        
        //Update every particle in the fluid
        for (int i = 0; i < fluid.getNumParticles(); i++) {
            updateParticle(i);
        }
        
        //Calculate log values
        sceneAvgPressure += pressureAcc / fluid.getNumParticles();
        sceneAvgVelocity += velocityAcc / fluid.getNumParticles();
        fluid = nextFluid;

        if (minNeighs < sceneMinNeighs) {
            sceneMinNeighs = minNeighs;
        } else if (maxNeighs > sceneMaxNeighs) {
            sceneMaxNeighs = maxNeighs;
        }
        sceneAvgNeighs += (minNeighs + maxNeighs) / 2.0;
        
        //Increment step
        step++;
    }

    public void updateParticle(int id) {
        
        //Get the particle and its info
        particle = (Particle) fluid.getG(id);
        infoI = (ParticleInfo) particle.getObj();
        
        //Create a new object for the updated particle info
        newInfo = new ParticleInfo();
        //Set the new info`s ID to the old one's
        newInfo.setFluidId(infoI.getFluidId());
        //Get the particle position
        posI = particle.getPos();
        //Get the particle neighbors
        neighs = searchGrid.getNeighs(posI);
        
        //Initializes the particle density
        density = 0;
        //Initializes the particle neighbors as -1 since we consider a particle
        //as being a neighbor of itself
        particle_neighbors = -1;
        
        //For every neighbor
        for (int j = 0; j < neighs.size(); j++) {
            //Get the neighbor particle and its position
            neighPart = neighs.get(j);
            posJ = fluid.getG(neighPart).getPos();
            //Calculate the distance between the particle and its neighbor
            r = posI.opSub(posJ);
            if (r.norm() < coreRadius) {
                particle_neighbors++;
            }
            //Get the neighbor info
            neighInfo = (ParticleInfo) fluid.getG(neighPart).getObj();
            //Set the mass according to the particle's ID
            if (neighInfo.getFluidId() == 1) {
                mass = mass_1;
            } else {
                mass = mass_2;
            }
            //Add neighbor contribution to the calculated density
            density += (mass * kernel(r));
        }
        //Log
        if (particle_neighbors < minNeighs) {
            minNeighs = particle_neighbors;
        } else if (particle_neighbors > maxNeighs) {
            maxNeighs = particle_neighbors;
        }
        //Set new density
        newInfo.setDensity(density);
        
        //Calculate and set new pressure
        if (infoI.getFluidId() == 1) {
            pressure = pressureConstant_1 * (density - infoI.getRestDensity());
        } else {
            pressure = pressureConstant_2 * (density - infoI.getRestDensity());
        }
        newInfo.setPressure(pressure);
        //Log 
        if (pressure > maxPressure) {
            maxPressure = pressure;
        } else if (pressure < minPressure) {
            minPressure = pressure;
        }
        
        //Initializes acceleration and forces
        accel.zero();
        result.zero();
        pressureForce.zero();
        
        //Calculate pressure force from neighbor contributions
        for (int j = 0; j < neighs.size(); j++) {
            neighPart = neighs.get(j);
            if (neighPart != id) {
                partJ = (Particle) fluid.getG(neighPart);
                posJ = partJ.getPos();
                infoJ = (ParticleInfo) partJ.getObj();
                r = posI.opSub(posJ);
                result = kernelSpiky(r);
                escalar = infoI.getPressure();
                escalar += infoJ.getPressure();
                escalar /= (2.0d * infoJ.getDensity());
                if (infoJ.getFluidId() == 1) {
                    mass = mass_1;
                } else {
                    mass = mass_2;
                }
                escalar *= mass;
                result = result.opScale(escalar);
                pressureForce = pressureForce.opAdd(result);
            }
        }
        //Get the pressure force simmetrical
        pressureForce = pressureForce.opScale(-1.0d);
        //Add pressure force and gravity to the acceleration vector
        accel = accel.opAdd(pressureForce);
        accel = accel.opAdd(gravity);

        //Initialize viscosity force
        viscosityForce.zero();
        //Calculate viscosity force from neighbor contributions
        for (int j = 0; j < neighs.size(); j++) {
            neighPart = neighs.get(j);
            if (neighPart != id) {
                partJ = (Particle) fluid.getG(neighPart);
                posJ = partJ.getPos();
                infoJ = (ParticleInfo) partJ.getObj();
                r = posI.opSub(posJ);
                result = infoJ.getVelocity();
                result = result.opSub(infoI.getVelocity());
                result = result.opScale(1.0d / infoJ.getDensity());
                if (infoJ.getFluidId() == 1) {
                    mass = mass_1;
                } else {
                    mass = mass_2;
                }
                result = result.opScale(mass);
                result = result.opScale(kernelViscosity(r));
                viscosityForce = viscosityForce.opAdd(result);
            }
        }
        //Multiply viscosity force to the correct dynamic viscoisty coefficient
        if (infoI.getFluidId() == 1) {
            viscosityForce = viscosityForce.opScale(viscosity_1);
        } else {
            viscosityForce = viscosityForce.opScale(viscosity_2);
        }
        //Add viscosity force to the acceleration vector
        accel = accel.opAdd(viscosityForce);

        //Divide the accumulated forces by the density to get the particle's
        //through Newton's 2nd Law, considering that each particle has an
        //unitary volume and, therefore, m = 1/density
        if (infoI.getDensity() != 0) {
            accel = accel.opScale((1.0d / infoI.getDensity()));
        } else {
            accel.zero();
        }

        //Get the particles's velocity and position
        velocity = infoI.getVelocity();
        oldPos = particle.getPos();
        newPos = particle.getPos();
        //Calculate new velocity and position through leap-frog integration
        result = infoI.getAccel();
        result = result.opAdd(accel);
        result = result.opScale(0.5d);
        result = result.opScale(gamma);
        velocity = velocity.opAdd(result);
        vel = velocity.norm();
        //Log
        if (vel > maxVelocity) {
            maxVelocity = vel;
        } else if (vel < minVelocity) {
            minVelocity = vel;
        }
        result = velocity;
        result = result.opScale(gamma);
        newPos = newPos.opAdd(result);
        result = accel;
        result = result.opScale(gamma * gamma / 2.0d);
        newPos = newPos.opAdd(result);

        //If there's an obstacle, check for collisions with it
        if (sceneSettings.hasObstacle()) {
            auxNewPos = newPos.copy();
            auxOldPos = oldPos.copy();
            collision = obstacle.checkCollision(auxOldPos, auxNewPos);
            if (collision.isChangedX() || collision.isChangedY() || collision.isChangedZ()) {
                if (collision.isChangedX()) {
                    velocity.setVecData((velocity.getVecData(X) * -1.0d * (1.0d - damping)), X);
                }
                if (collision.isChangedY()) {
                    velocity.setVecData((velocity.getVecData(Y) * -1.0d * (1.0d - damping)), Y);
                }
                if (collision.isChangedZ()) {
                    velocity.setVecData((velocity.getVecData(Z) * -1.0d * (1.0d - damping)), Z);
                }
                newPos = collision.getPosition();
                collided = true;
            }
        }

        //Save the calculated particle position and info
        newInfo.setVelocity(velocity);
        newInfo.setAccel(accel);
        particle.setPos(newPos);
        particle.setObj(newInfo);

        //Check for collisions with the tank walls (scene boundaries)
        checkCollision(particle, id, oldPos);
        if (searchGrid.hasChangedCell(oldPos, newPos)) {
            updateGrid(id, oldPos, newPos);
        }

        //Save particle to the fluid
        newPos = particle.getPos();
        newInfo = (ParticleInfo) particle.getObj();
        nextFluid.setG(particle, id);

        //Accumulates calculated pressure and velocity for the log and stats
        pressureAcc += pressure;
        velocityAcc += velocity.norm();

        //If there are statistics files to be written, write the calculated
        //pressure and velocity to them
        if (files) {
            try {
                bwP.write(pressure + "\n");
                bwV.write(velocity.norm() + "\n");
            } catch (IOException ex) {
            }
        }

    }

    //----------- Polynomial kernel -----------
    public double kernel(Vector r) {
        normR = r.norm();
        if (normR <= coreRadius) {
            vKernel = (coreRadius * coreRadius) - (normR * normR);
            return cPolyKernel * vKernel * vKernel * vKernel;
        } else {
            return 0;
        }
    }

    //----------- Spiky kernel gradient -----------
    public Vector kernelSpiky(Vector r) {
        normR = r.norm();
        if (normR <= coreRadius) {
            result = r.normalize();
            vKernel = coreRadius - normR;
            scale = cSpikyKernel * vKernel * vKernel;
            result = result.opScale(scale);
        } else {
            result.zero();
        }
        return result;
    }

    //----------- Viscosity kernel laplacian -----------
    public double kernelViscosity(Vector r) {
        normR = r.norm();
        if (normR <= coreRadius) {
            vKernel = coreRadius - normR;
            return cViscosityKernel * vKernel;
        } else {
            return 0;
        }
    }

    //----------- Calculates particles' initial densities -----------
    private void calculateInitialDensities() {
        for (int i = 0; i < fluid.getNumParticles(); i++) {
            partI = (Particle) fluid.getG(i);
            infoI = (ParticleInfo) partI.getObj();
            if (infoI.getRestDensity() == 0) {
                neighs = searchGrid.getNeighs(partI.getPos());
                posI = partI.getPos();
                density = 0;
                for (int j = 0; j < neighs.size(); j++) {
                    partJ = (Particle) fluid.getG(neighs.get(j));
                    infoJ = (ParticleInfo) partJ.getObj();
                    posJ = partJ.getPos();
                    r = posI.opSub(posJ);
                    if (infoJ.getFluidId() == 1) {
                        density += mass_1 * kernel(r);
                    } else {
                        density += mass_2 * kernel(r);
                    }
                }
                infoI.setDensity(density);
                infoI.setRestDensity(density);
                partI.setObj(infoI);
            }
        }
    }

    //----------- Updates the search grid -----------
    public void updateGrid(int part, Vector oldPos, Vector newPos) {
        searchGrid.removeParticle(part, oldPos);
        searchGrid.addParticle(part, newPos);
    }

    //----------- Checks for collisions with the tank walls -----------
    public void checkCollision(Particle particle, int i, Vector oldPos) {
        newPos = particle.getPos();
        infoI = (ParticleInfo) particle.getObj();
        x = newPos.getVecData(X);
        y = newPos.getVecData(Y);
        z = newPos.getVecData(Z);
        velocity = infoI.getVelocity();
        if (x <= tankMin.getVecData(X)) {
            x = tankMin.getVecData(X) + 0.1d;
            velocity.setVecData((velocity.getVecData(X) * -1.0d * (1.0d - damping)), X);
        } else if (x >= tankMax.getVecData(X)) {
            x = tankMax.getVecData(X) - 0.1d;
            velocity.setVecData((velocity.getVecData(X) * -1.0d * (1.0d - damping)), X);
        }
        if (y <= tankMin.getVecData(Y)) {
            y = tankMin.getVecData(Y) + 0.1d;
            velocity.setVecData((velocity.getVecData(Y) * -1.0d * (1.0d - damping)), Y);
        } else if (y >= tankMax.getVecData(Y)) {
            y = tankMax.getVecData(Y) - 0.1d;
            velocity.setVecData((velocity.getVecData(Y) * -1.0d * (1.0d - damping)), Y);
        }
        if (z <= tankMin.getVecData(Z)) {
            z = tankMin.getVecData(Z) + 0.1d;
            velocity.setVecData((velocity.getVecData(Z) * -1.0d * (1.0d - damping)), Z);
        } else if (z >= tankMax.getVecData(Z)) {
            z = tankMax.getVecData(Z) - 0.1d;
            velocity.setVecData((velocity.getVecData(Z) * -1.0d * (1.0d - damping)), Z);
        }
        newPos.setVecData(x, X);
        newPos.setVecData(y, Y);
        newPos.setVecData(z, Z);
        infoI.setVelocity(velocity);
        particle.setObj(infoI);
        particle.setPos(newPos);
    }

    //----------- Renders the scene -----------
    public void render(String cmap, String rmode, double pmin, double pmax) {
        if (colorMode.equalsIgnoreCase("simple")) {
            fluid.simpleRender(cmap, rmode, pmin, pmax, renderMinZ, renderMaxZ, 0);
        } else if (colorMode.equalsIgnoreCase("pressure")) {
            fluid.simpleRender(cmap, rmode, pmin, pmax, renderMinZ, renderMaxZ, 1);
        } else if (colorMode.equalsIgnoreCase("velocity")) {
            fluid.simpleRender(cmap, rmode, pmin, pmax, renderMinZ, renderMaxZ, 2);
        }
        if (sceneSettings.hasObstacle()) {
            obstacle.render(renderMinZ, renderMaxZ);
        }
    }

    //----------- Close statistics files -----------
    public void closeFiles() {
        try {
            bwP.close();
            bwV.close();
        } catch (IOException ex) {
        }
    }

}
