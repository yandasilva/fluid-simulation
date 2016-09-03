package ic.apps.sph;

//Static LWJGL
import static org.lwjgl.opengl.GL11.*;

//Graphics data structures
import ic.mlibs.gl.GLPointSet;
import ic.mlibs.structures.Point3D;
import ic.mlibs.util.ColorMap;

//ParticleSystem extends GLPointSet
//Since GLPointSet is a set of Point3Ds and Particle extends Point3D,
//ParticleSystem is a set of Particles
public class ParticleSystem extends GLPointSet {

    //Constants for easily accessing indexes
    public static int X = 0;
    public static int Y = 1;
    public static int Z = 2;

    //Constant vectors for particle initial settings when spawned
    private static Vector SPAWN_MIN;
    private static Vector SPAWN_MAX;
    private static Vector SPAWN_SIZE;
    private static Vector SPAWN_SPACING;
    private static Vector SPAWN_VELOCITY;
    private static double SPAWN_MASS;

    //Fluid color map
    private ColorMap map = new ColorMap();

    private int numParticles;
    private int part;
    private int numPartX;
    private int numPartY;
    private int numPartZ;
    private int nPart;
    private int insidePores = 0;

    private Particle particle;
    private ParticleInfo info;

    private Vector spawnBase;

    public boolean init;
    
    //Getters
    public int getNumParticles() {
        return numParticles;
    }
    
    public int getFirstFluidParticles() {
        return numParticles - insidePores;
    }
    
    public int getSecondFluidParticles() {
        return numParticles - insidePores;
    }

    //Setters
    public void setNumParticles(int numParticles) {
        this.numParticles = numParticles;
    }
    
    //Constructor
    public ParticleSystem(SceneSettings sceneSettings,
            FluidSettings fluidSettings_1, FluidSettings fluidSettings_2,
            Obstacle obstacle) {
        try {
            //Switch between the two different scenes
            switch (sceneSettings.getScene()) {
                case 1:
                    breakingDam(fluidSettings_1, fluidSettings_2);
                    break;
                default:
                    porous(sceneSettings, fluidSettings_1, fluidSettings_2, obstacle);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    //Creates a breaking dam scene, with two fluid volumes over an obstacle
    private void breakingDam(FluidSettings fluidSettings_1, FluidSettings fluidSettings_2) {

        //Initializes particle counters (total and per fluid)
        part = 0;
        numParticles = 0;
        //Creates the two fluids volumes
        createFluid(1, fluidSettings_1);
        createFluid(2, fluidSettings_2);
        //Sets the total number of particles
        setNvert(numParticles);
    }
    
    //Creates a scene with one fluid being injected on the obstacle through a
    //volume over it and the second one confined inside its pores
    private void porous(SceneSettings sceneSettings, FluidSettings fluidSettings_1,
            FluidSettings fluidSettings_2, Obstacle obstacle) {
        
        //Initializes particle counters (totla and per fluid)
        part = 0;
        numParticles = 0;
        //Creates the fluid volume
        createFluid(1, fluidSettings_1, sceneSettings.getSpawnVel());
        //Distributes the second fluid inside the obtacle's pores
        populatePores(2, fluidSettings_2, obstacle);
        //Sets the position of the plane of particles that's spawned
        SPAWN_MIN = sceneSettings.getSpawnMin();
        SPAWN_MAX = sceneSettings.getSpawnMax();
        SPAWN_SIZE = SPAWN_MAX.opSub(SPAWN_MIN);
        //Sets the initial velocity of spawned particles
        SPAWN_VELOCITY = sceneSettings.getSpawnVel();
        //Sets the initial spacing between spawned particles
        SPAWN_SPACING = fluidSettings_1.getSpacing();
        //Sets the mass of spawned particles
        SPAWN_MASS = fluidSettings_1.getMass();
        //Sets the total number of particles
        setNvert(numParticles);
    }

    public void createFluid(int id, FluidSettings fluidSettings) {
        
        //Initial spacing between particles in the fluid
        Vector spacing = fluidSettings.getSpacing();
        //Dimensions of the volume of fluid
        Vector size = fluidSettings.getVolumeMax().opSub(fluidSettings.getVolumeMin());
        
        //Calculates the number of particles in each direction
        numPartX = (int) Math.floor(size.getVecData(X) / spacing.getVecData(X));
        numPartY = (int) Math.floor(size.getVecData(Y) / spacing.getVecData(Y));
        numPartZ = (int) Math.floor(size.getVecData(Z) / spacing.getVecData(Z));

        //Calculates the total number of particles inside the volume
        nPart = numPartX * numPartY * numPartZ;
        
        //Sets the base of the particle positioning to the volume's min position
        Vector base = fluidSettings.getVolumeMin();
        //Adds half the spacing to the base in order to prevent particles from
        //being contained in the planes that define the volume of fluid 
        base.setVecData(base.getVecData(X) + spacing.getVecData(X) / 2.0d, X);
        base.setVecData(base.getVecData(Y) + spacing.getVecData(Y) / 2.0d, Y);
        base.setVecData(base.getVecData(Z) + spacing.getVecData(Z) / 2.0d, Z);

        //Iterates through the # of created particles, adding the value of
        //spacing to the coordinates of the base position in order to get the
        //position of the created particle
        for (double z = base.getVecData(Z); z <= fluidSettings.getVolumeMax().getVecData(Z);
                z += spacing.getVecData(Z)) {
            for (double y = base.getVecData(Y); y <= fluidSettings.getVolumeMax().getVecData(Y);
                    y += spacing.getVecData(Y)) {
                for (double x = base.getVecData(X); x <= fluidSettings.getVolumeMax().getVecData(X);
                        x += spacing.getVecData(X)) {
                    //Creates particle info
                    info = new ParticleInfo(fluidSettings.getMass(), id);
                    //Creates particle
                    particle = new Particle(x, y, z);
                    //Sets particle info to particle
                    particle.setObj(info);
                    //Adds particle to the fluid
                    G.add(particle);
                    //Increments the number of particles
                    part++;
                }
            }
        }
        //Adds the number of created particles to the total
        numParticles += part;
    }
    
    //Same as above, but creates particles with a given initial velocity
    public void createFluid(int id, FluidSettings fluidSettings, Vector vel) {
        Vector spacing = fluidSettings.getSpacing();
        Vector size = fluidSettings.getVolumeMax().opSub(fluidSettings.getVolumeMin());

        numPartX = (int) Math.floor(size.getVecData(X) / spacing.getVecData(X));
        numPartY = (int) Math.floor(size.getVecData(Y) / spacing.getVecData(Y));
        numPartZ = (int) Math.floor(size.getVecData(Z) / spacing.getVecData(Z));

        nPart = numPartX * numPartY * numPartZ;
        System.out.println(nPart);
        double mass = fluidSettings.getMass();
        System.out.println(mass);

        Vector base = fluidSettings.getVolumeMin();
        base.setVecData(base.getVecData(X) + spacing.getVecData(X) / 2.0d, X);
        base.setVecData(base.getVecData(Y) + spacing.getVecData(Y) / 2.0d, Y);
        base.setVecData(base.getVecData(Z) + spacing.getVecData(Z) / 2.0d, Z);

        System.out.println(base);

        for (double z = base.getVecData(Z); z <= fluidSettings.getVolumeMax().getVecData(Z);
                z += spacing.getVecData(Z)) {
            for (double y = base.getVecData(Y); y <= fluidSettings.getVolumeMax().getVecData(Y);
                    y += spacing.getVecData(Y)) {
                for (double x = base.getVecData(X); x <= fluidSettings.getVolumeMax().getVecData(X);
                        x += spacing.getVecData(X)) {
                    info = new ParticleInfo(mass, id, vel);
                    particle = new Particle(x, y, z);
                    particle.setObj(info);
                    G.add(particle);
                    part++;
                }
            }
        }
        numParticles += part;
    }
    
    //Similar to the above methods, but only creates a particle if the cell that
    //contains its position is a pore
    public void populatePores(int id, FluidSettings fluidSettings, Obstacle obstacle) {
        Vector spacing = fluidSettings.getSpacing();
        Vector start = obstacle.getStartingPoint();

        Vector base = start;
        base.setVecData(base.getVecData(X) + spacing.getVecData(X) / 2.0d, X);
        base.setVecData(base.getVecData(Y) + spacing.getVecData(Y) / 2.0d, Y);
        base.setVecData(base.getVecData(Z) + spacing.getVecData(Z) / 2.0d, Z);

        for (double z = base.getVecData(Z); z <= obstacle.getEndingPoint().getVecData(Z);
                z += spacing.getVecData(Z)) {
            for (double y = base.getVecData(Y); y <= obstacle.getEndingPoint().getVecData(Y);
                    y += spacing.getVecData(Y)) {
                for (double x = base.getVecData(X); x <= obstacle.getEndingPoint().getVecData(X);
                        x += spacing.getVecData(X)) {
                    if (obstacle.isEmpty(x, y, z)) {
                        info = new ParticleInfo(fluidSettings.getMass(), id);
                        particle = new Particle(x, y, z);
                        particle.setObj(info);
                        G.add(particle);
                        part++;
                        insidePores++;
                    }
                }
            }
        }
        numParticles += part;
    }
    
    //Similar to the above, but creates particles inside a given spawn volume
    public void spawnParticles(SearchGrid searchGrid) {
        numPartX = (int) Math.floor(SPAWN_SIZE.getVecData(X) / SPAWN_SPACING.getVecData(X));
        numPartY = (int) Math.floor(SPAWN_SIZE.getVecData(Y) / SPAWN_SPACING.getVecData(Y));
        numPartZ = (int) Math.floor(SPAWN_SIZE.getVecData(Z) / SPAWN_SPACING.getVecData(Z));

        nPart = numPartX * numPartY * numPartZ;

        spawnBase = SPAWN_MIN.copy();
        spawnBase = spawnBase.opAdd(SPAWN_SPACING.opScale(0.5d));

        for (double z = SPAWN_MIN.getVecData(Z); z <= SPAWN_MAX.getVecData(Z);
                z += SPAWN_SPACING.getVecData(Z)) {
            for (double y = SPAWN_MIN.getVecData(Y); y <= SPAWN_MAX.getVecData(Y);
                    y += SPAWN_SPACING.getVecData(Y)) {
                for (double x = SPAWN_MIN.getVecData(X); x <= SPAWN_MAX.getVecData(X);
                        x += SPAWN_SPACING.getVecData(X)) {
                    info = new ParticleInfo(SPAWN_MASS, 1, SPAWN_VELOCITY);
                    particle = new Particle(x, y, z);
                    particle.setObj(info);
                    G.add(particle);
                    searchGrid.addParticle(part, particle.getPos());
                    part++;
                }
            }
        }

        numParticles += nPart;
    }

    //Destroys a particle from the fluid
    public void destroyParticle(int id, Particle particle, SearchGrid searchGrid) {
        G.remove(particle);
        setNvert(getNvert() - 1);
        numParticles--;
    }

    //Renders the fluid
    public void simpleRender(String cmap, String rmode, double pmin, double pmax,
            double renderMinZ, double renderMaxZ, int mode) {
        //Sets the color map and its limits
        map.setColorMap(cmap);
        map.setMapExtrems(pmin, pmax);

        //Sets point size
        glPointSize(5);
        glBegin(GL_POINTS);

        {
            //For all particles
            for (int i = 0; i < getNvert(); i++) {
                Point3D v = getG(i);
                if (v.getPosZ() >= renderMinZ && v.getPosZ() <= renderMaxZ) {
                    switch (mode) {
                        //Mode 0: colors each particle according to its fluid ID
                        case 0:
                            if (info.getFluidId() == 1) {
                                //Blue
                                glColor4d(0, 0, 1, 0.5);
                            } else {
                                //Yellow
                                glColor4d(1, 1, 0, 0.5);
                            }
                            break;
                        //Mode 0: colors the fluid according to its pressure field
                        case 1:
                            map.setGLColor(info.getPressure(), (byte) 255, false);
                            break;
                        //Mode 0: colors the fluid according to its velocity field
                        case 2:
                            map.setGLColor(info.getVelocity().norm(), (byte) 255, false);
                            break;
                    }
                    glVertex3d(v.getPosX(), v.getPosY(), v.getPosZ());
                }
            }
            glEnd();

            map.setMapExtrems(0, 255);
            map.drawMap(-1.25f, -1.25f, 0.1f, 2.5f);

        }
    }

}