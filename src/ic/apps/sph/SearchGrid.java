package ic.apps.sph;

//Java
import java.util.ArrayList;

//LWJGL
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glVertex3d;

public class SearchGrid {
    
    //The grid is represented by a matrix of lists
    private final ArrayList[][][] grid;
    //Min point of the grid
    private final Vector boundaryMin;
    //Side of a cell
    private final double cellSize;
    //Dimensions of the grid in each direction
    private final int dimX;
    private final int dimY;
    private final int dimZ;
    
    //Auxiliary variables (optimization)
    private ArrayList neighs;
    private Vector steps;
    private int stepsX;
    private int stepsY;
    private int stepsZ;
    private int i;
    private int j;
    private int k;
    private boolean found;
    
    //Constructor
    public SearchGrid (Vector min, Vector max, double radius){
        
        //Sets the min point of the grid and its cell side
        boundaryMin = min;
        cellSize = radius;
        
        //Calculates the size of the grid
        double sizeX = max.getVecData(0)-min.getVecData(0);
        double sizeY = max.getVecData(1)-min.getVecData(1);
        double sizeZ = max.getVecData(2)-min.getVecData(2);
        
        //Calculates the dimensions of the grid
        dimX = (int) Math.ceil(sizeX/radius)+1;
        dimY = (int) Math.ceil(sizeY/radius)+1;
        dimZ = (int) Math.ceil(sizeZ/radius)+1;
        
        //Initializes the grid with the calculated dimensions
        grid = new ArrayList[dimX][dimY][dimZ];
        
        //Initializes each cell with an empty array of integers
        for (i = 0; i < dimX; i++){
            for (j = 0; j < dimY; j++){
                for (k = 0; k < dimZ; k++){
                    grid[i][j][k] = new ArrayList<Integer>();
                }
            }
        }
    }
    
    //Adds a particle to the search grid
    public void addParticle (int id, Vector position){
        
        steps = position.opSub(boundaryMin);
        steps = steps.opScale(1.0f/cellSize);
        stepsX = (int) Math.floor(steps.getVecData(0));
        stepsY = (int) Math.floor(steps.getVecData(1));
        stepsZ = (int) Math.floor(steps.getVecData(2));
        if (stepsX >= 0 && stepsX < dimX && stepsY >= 0 && stepsY < dimY &&
                stepsZ >= 0 && stepsZ < dimZ){
            grid[stepsX][stepsY][stepsZ].add(id);
        }
        
    }
    
    //Removes a particle from the grid 
    public void removeParticle(int id, Vector position){
        
        steps = position.opSub(boundaryMin);
        steps = steps.opScale(1.0f/cellSize);
        stepsX = (int) Math.floor(steps.getVecData(0));
        stepsY = (int) Math.floor(steps.getVecData(1));
        stepsZ = (int) Math.floor(steps.getVecData(2));
        if (stepsX >= 0 && stepsX < dimX && stepsY >= 0 && stepsY < dimY &&
                stepsZ >= 0 && stepsZ < dimZ){
            grid[stepsX][stepsY][stepsZ].remove(grid[stepsX][stepsY][stepsZ].indexOf(id));
        }
        
    }
    
    //Gets the neighborhood of a particle given its position
    public ArrayList<Integer> getNeighs(Vector position){
        
        steps = position.opSub(boundaryMin);
        steps = steps.opScale(1.0f/cellSize);
        stepsX = (int) Math.floor(steps.getVecData(0));
        stepsY = (int) Math.floor(steps.getVecData(1));
        stepsZ = (int) Math.floor(steps.getVecData(2));
        neighs = new ArrayList<Integer>();
        for (i = stepsX-1; i <= stepsX+1; i++){
            for (j = stepsY-1; j <= stepsY+1; j++){
                for (k = stepsZ-1; k <= stepsZ+1; k++){
                    if (i >= 0 && i < dimX && j >= 0 && j < dimY &&
                            k >= 0 && k < dimZ){
                        neighs.addAll(grid[i][j][k]);
                    }
                }
            }
        }
        return neighs;
    }
    
    //Checks if a particle changed cells due to its movement
    public boolean hasChangedCell(Vector before, Vector after){
        steps = before.opSub(boundaryMin);
        steps = steps.opScale(1.0f/cellSize);
        stepsX = (int) Math.floor(steps.getVecData(0));
        stepsY = (int) Math.floor(steps.getVecData(1));
        stepsZ = (int) Math.floor(steps.getVecData(2));
        
        steps = after.opSub(boundaryMin);
        steps = steps.opScale(1.0f/cellSize);
        stepsX -= (int) Math.floor(steps.getVecData(0));
        stepsY -= (int) Math.floor(steps.getVecData(1));
        stepsZ -= (int) Math.floor(steps.getVecData(2));
        
        return (stepsX != 0) || (stepsY != 0) || (stepsZ != 0);
    }
    
    //Gets the number of cells in the grid
    public int getSize(){
        return dimX*dimY*dimZ;
    }
    
    //Checks if the cell of a particle is the one at position (0, 0, 0) of the grid
    public boolean isFirstCell(Vector position){
        steps = position.opSub(boundaryMin);
        steps = steps.opScale(1.0f/cellSize);
        stepsX = (int) Math.floor(steps.getVecData(0));
        stepsY = (int) Math.floor(steps.getVecData(1));
        stepsZ = (int) Math.floor(steps.getVecData(2));
        return (stepsX==0) && (stepsY==0) && (stepsZ==0);
    }
    
    //Gets the indices of all partices in a cell given its id
    public ArrayList<Integer> getCell (int id){
        i = 0;
        j = 0;
        k = 0;
        id--;
        found = id < 0;
        while (i < dimX && !found){
            while (j < dimY && !found){
                while (k < dimZ && !found){
                    id--;
                    found = id < 0;
                }
            }
        }
        return grid[i][j][k];
    }
    
    //Get the indices of all particles in a cell given a position
    public ArrayList<Integer> getCell (Vector position){
        steps = position.opSub(boundaryMin);
        steps = steps.opScale(1.0f/cellSize);
        stepsX = (int) Math.floor(steps.getVecData(0));
        stepsY = (int) Math.floor(steps.getVecData(1));
        stepsZ = (int) Math.floor(steps.getVecData(2));
        return grid[stepsX][stepsY][stepsZ];
    }
    
    //Renders the search grid
    public void render() {
        double x;
        double y;
        double z;
        for (int m = 0; m < dimX; m++) {
            for (int n = 0; n < dimY; n++){
                for (int o = 0; o < dimZ; o++){
                    if (true) {
                        glColor4d(1, 1, 1, 0.5);
                        x = boundaryMin.getVecData(0) + m*cellSize;
                        y = boundaryMin.getVecData(1) + n*cellSize;
                        z = boundaryMin.getVecData(2) + o*cellSize;
                        glPointSize(4);
                        glBegin(GL_POINTS);
                        {
                            glVertex3d(x, y, z);

                        }
                        glEnd();
                    }
                }
            }
        }
    }
}
