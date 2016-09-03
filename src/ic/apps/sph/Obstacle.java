package ic.apps.sph;

//File IO
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Static LWJGL
import static org.lwjgl.opengl.GL11.*;

public class Obstacle {
    
    //Side of an obstacle cell
    double side = 2.0;
    //Boolean matrix representing the obstacle
    boolean[][][] topology;
    //Min (bottom-left-back) position of the obstacle
    Vector min;    
    //Max (top-right-front) position of the obstacle
    Vector max;
    //Auxiliary runtime variables created here for efficiency
    Vector result;
    int x;
    int y;
    int z;
    int i;
    int j;
    int k;
    int l;
    int m;
    int n;
    double pointX;
    double pointY;
    double pointZ;
    double height;
    Vector startingPoint;
    Vector endingPoint;
    
    //Getters
    public double getHeight() {
        return height;
    }

    public Vector getStartingPoint() {
        return startingPoint;
    }

    public Vector getEndingPoint() {
        return endingPoint;
    }

    //Constructor
    public Obstacle(Vector tankMin, Vector tankMax, double porosity, String path) {
        
        //Difference between the max and min points of the tank
        Vector diff = tankMax.opSub(tankMin);
        //The obstacle height is pores the tank height
        height = diff.getVecData(1) / 2;
        //Calculates # of cells in each direction
        x = (int) (diff.getVecData(0) / side);
        y = (int) (height / side);
        z = (int) (diff.getVecData(2) / side);
        //The min position of the obstacle is the same as the tank
        startingPoint = tankMin.copy();
        //Calculates the max position of the obstacle
        endingPoint = new Vector(tankMax.getVecData(0),
                startingPoint.getVecData(1) + height, tankMax.getVecData(2));
        //Initializes the matrix with the calculated dimansions
        topology = new boolean[x][y][z];

        //Calculates the # of pores in the obstacle from its porosity
        int pores = (int) (x * y * z * porosity);
        //Initializes a counter
        int counter = 0;
        try {
            //Tries to read obstacle porosity from its settings 
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                int pos = 0;
                for (i = 0; i < x; i++) {
                    for (j = 0; j < y; j++) {
                        for (k = 0; k < z; k++) {
                            if (line.charAt(pos) == '1') {
                                topology[i][j][k] = true;
                            } else {
                                topology[i][j][k] = false;
                                counter++;
                            }
                            pos++;
                        }
                    }
                }
            } else {
                throw new Exception();
            }
            bufferedReader.close();
            System.out.println("READ FILE");
        } catch (Exception ex) {
            //If couldn't read from file, initialize matrix with true values
            for (i = 0; i < x; i++) {
                for (j = 0; j < y; j++) {
                    for (k = 0; k < z; k++) {
                        topology[i][j][k] = true;
                    }
                }
            }
        }
        
        //Calculates # of remaining pores to assign
        int remaining = pores - counter;
        //Sets random cells as pores by setting its matrix position to false
        i = (int) (Math.random() * x);
        j = (int) (Math.random() * y);
        k = (int) (Math.random() * z);
        while (remaining > 0) {
            while (!topology[i][j][k]) {
                i = (int) (Math.random() * x);
                j = (int) (Math.random() * y);
                k = (int) (Math.random() * z);
            }
            topology[i][j][k] = false;
            remaining--;
        }
        if (pores - counter >= 0) {
            //Write obstacle topology to a file
            writeObstacleFile(path);
        }

    }

    public CollisionResult checkCollision(Vector oldPos, Vector newPos) {
        
        //Creates a new (-INFINITY, 0, 0) to signal that no collision happened
        Vector notCollided = new Vector(Double.MIN_VALUE, 0, 0);
        //Create a CollisionResult obstacle
        CollisionResult collResult = new CollisionResult(notCollided, false, false, false);
        //If the new particle position is beyond the obstacle boundaries,
        //there was no collision
        if (newPos.getVecData(0) < startingPoint.getVecData(0)
                || newPos.getVecData(0) > endingPoint.getVecData(0)
                || newPos.getVecData(1) < startingPoint.getVecData(1)
                || newPos.getVecData(1) > endingPoint.getVecData(1)
                || newPos.getVecData(2) < startingPoint.getVecData(2)
                || newPos.getVecData(2) > endingPoint.getVecData(2)) {
            return collResult;
        } else {
            //If the new particle position is within the obstacle boundaries,
            //calculate the cell where its new position is
            i = (int) ((newPos.getVecData(0) - startingPoint.getVecData(0)) / side);
            j = (int) ((newPos.getVecData(1) - startingPoint.getVecData(1)) / side);
            k = (int) ((newPos.getVecData(2) - startingPoint.getVecData(2)) / side);
            //If the new cell is beyond the matrix dimensions or it's a pore,
            //there was no collision
            if ((i >= x) || (j >= y) || (k >= z)
                    || (i < 0) || (j < 0) || (k < 0) || (!topology[i][j][k])) {
                return collResult;
            } else {
                //If the new particle position is inside an obstacle (filled cell),
                //calculate the cell where its old position was
                l = (int) ((oldPos.getVecData(0) - startingPoint.getVecData(0)) / side);
                m = (int) ((oldPos.getVecData(1) - startingPoint.getVecData(1)) / side);
                n = (int) ((oldPos.getVecData(2) - startingPoint.getVecData(2)) / side);
                //Create a result vector from the particle's new position
                result = newPos.copy();
                //Get the min and max position of the particle's new cell
                min = getMin(i, j, k);
                max = getMax(i, j, k);
                //If the particle changed cells on the X direction,
                //correct its X coordinate
                if ((i > l) && (i != 0)) {
                    result.setVecData(min.getVecData(0) - 0.1, 0);
                    collResult.setChangedX(true);
                } else if ((i < l) && (i != (x - 1))) {
                    result.setVecData(max.getVecData(0) + 0.1, 0);
                    collResult.setChangedX(true);
                }
                //If the particle changed cells on the Y direction,
                //correct its Y coordinate
                if (j > m) {
                    result.setVecData(min.getVecData(1) - 0.1, 1);
                    collResult.setChangedY(true);
                } else if ((j < m) || (j == (y - 1))) {
                    result.setVecData(max.getVecData(1) + 0.1, 1);
                    collResult.setChangedY(true);
                }
                //If the particle changed cells on the Z direction,
                //correct its Z coordinate
                if (k > n) {
                    result.setVecData(min.getVecData(2) - 0.1, 2);
                    collResult.setChangedZ(true);
                } else if (k < n) {
                    result.setVecData(max.getVecData(2) + 0.1, 2);
                    collResult.setChangedZ(true);
                }
                //Returns the collision result with the new particle position
                collResult.setPosition(result);
                return collResult;
            }
        }
    }

    //Checks if a position (p, m, n) resides in an empty cell
    public boolean isEmpty(double p, double m, double n) {
        i = (int) ((p - startingPoint.getVecData(0)) / side);
        j = (int) ((m - startingPoint.getVecData(1)) / side);
        k = (int) ((n - startingPoint.getVecData(2)) / side);
        return ((i >= x) || (j >= y) || (k >= z)
                || (i < 0) || (j < 0) || (k < 0) || (!topology[i][j][k]));
    }
    
    //Gets the min (bottom-left-back) position of an obstacle cell
    public Vector getMin(int l, int m, int n) {
        return new Vector(startingPoint.getVecData(0) + l * side,
                startingPoint.getVecData(1) + m * side,
                startingPoint.getVecData(2) + n * side);
    }

    //Gets the max (top-right-front) position of an obstacle cell
    public Vector getMax(int l, int m, int n) {
        return new Vector(startingPoint.getVecData(0) + (l + 1) * side,
                startingPoint.getVecData(1) + (m + 1) * side,
                startingPoint.getVecData(2) + (n + 1) * side);
    }

    //Checks if a position a is between min and max
    public boolean isBetween(Vector a, Vector min, Vector max) {
        return ((a.getVecData(0) >= min.getVecData(0))
                && (a.getVecData(0) <= max.getVecData(0))
                && (a.getVecData(1) >= min.getVecData(1))
                && (a.getVecData(1) <= max.getVecData(1))
                && (a.getVecData(2) >= min.getVecData(2))
                && (a.getVecData(2) <= max.getVecData(2)));
    }

    //Writes obstacle topology to a file
    private void writeObstacleFile(String path) {
        String fileName = path;
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (i = 0; i < x; i++) {
                for (j = 0; j < y; j++) {
                    for (k = 0; k < z; k++) {
                        if (topology[i][j][k]) {
                            bufferedWriter.write("1");
                        } else {
                            bufferedWriter.write("0");
                        }
                    }
                }
            }
            bufferedWriter.close();
        } catch (IOException ex) {
        }
    }

    //Renders the obstacle
    public void render(double renderMinZ, double renderMaxZ) {
        for (i = 0; i < x; i++) {
            for (j = 0; j < y; j++) {
                for (k = 0; k < z; k++) {
                    if (topology[i][j][k]) {
                        pointX = startingPoint.getVecData(0) + (side * i);
                        pointY = startingPoint.getVecData(1) + (side * j);
                        pointZ = startingPoint.getVecData(2) + (side * k);

                        if (pointZ <= renderMaxZ && pointZ >= renderMinZ) {
                            glColor4f(0.75f, 0.75f, 0.75f, 0.75f);
                            glBegin(GL_LINE_LOOP);
                            glVertex3d(pointX, pointY, pointZ);
                            glVertex3d(pointX + side, pointY, pointZ);
                            glVertex3d(pointX + side, pointY + side, pointZ);
                            glVertex3d(pointX, pointY + side, pointZ);
                            glEnd();
                            glBegin(GL_LINE_LOOP);
                            glVertex3d(pointX, pointY, pointZ + side);
                            glVertex3d(pointX + side, pointY, pointZ + side);
                            glVertex3d(pointX + side, pointY, pointZ);
                            glVertex3d(pointX, pointY, pointZ);
                            glEnd();
                            glBegin(GL_LINE_LOOP);
                            glVertex3d(pointX, pointY, pointZ);
                            glVertex3d(pointX, pointY, pointZ + side);
                            glVertex3d(pointX, pointY + side, pointZ + side);
                            glVertex3d(pointX, pointY + side, pointZ);
                            glEnd();
                            glBegin(GL_LINE_LOOP);
                            glVertex3d(pointX, pointY + side, pointZ + side);
                            glVertex3d(pointX + side, pointY + side, pointZ + side);
                            glVertex3d(pointX + side, pointY + side, pointZ);
                            glVertex3d(pointX, pointY + side, pointZ);
                            glEnd();
                            glBegin(GL_LINE_LOOP);
                            glVertex3d(pointX, pointY, pointZ + side);
                            glVertex3d(pointX + side, pointY, pointZ + side);
                            glVertex3d(pointX + side, pointY + side, pointZ + side);
                            glVertex3d(pointX, pointY + side, pointZ + side);
                            glEnd();
                            glBegin(GL_LINE_LOOP);
                            glVertex3d(pointX + side, pointY, pointZ);
                            glVertex3d(pointX + side, pointY, pointZ + side);
                            glVertex3d(pointX + side, pointY + side, pointZ + side);
                            glVertex3d(pointX + side, pointY + side, pointZ);
                            glEnd();
                        }
                    }
                }
            }
        }
    }

}
