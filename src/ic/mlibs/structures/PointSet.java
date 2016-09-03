package ic.mlibs.structures;

//Java
import java.io.BufferedReader;
import java.io.File;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PointSet {

    protected int nvert = 0;            //Number of vertices
    protected ArrayList<Point3D> G; //Vertices coordinates

    //Constructors
    public PointSet() {
        G = new ArrayList<Point3D>();
    }

    public PointSet(String file) {
        readPointSet(file);
    }

    //Getters & setters
    public int getNvert() {
        return nvert;
    }

    public void setNvert(int nv) {
        nvert = nv;
    }

    public ArrayList<Point3D> getG() {
        return G;
    }

    public Point3D getG(int v) {
        return G.get(v);
    }

    public void setG(Point3D p, int v) {
        G.set(v, p);
    }

    //Get a bounding box of the point set
    public void getBbox(double[] min, double[] max) {

        double t_mx, t_Mx, t_my, t_My, t_mz, t_Mz;

        //Starts the limiters
        t_mx = t_Mx = G.get(0).getPosX();
        t_my = t_My = G.get(0).getPosY();
        t_mz = t_Mz = G.get(0).getPosZ();

        //Run on the vertices
        for (int i = 1; i < getNvert(); ++i) {
            if (G.get(i).getPosX() < t_mx) {
                t_mx = G.get(i).getPosX();
            }
            if (G.get(i).getPosX() > t_Mx) {
                t_Mx = G.get(i).getPosX();
            }

            if (G.get(i).getPosY() < t_my) {
                t_my = G.get(i).getPosY();
            }
            if (G.get(i).getPosY() > t_My) {
                t_My = G.get(i).getPosY();
            }

            if (G.get(i).getPosZ() < t_mz) {
                t_mz = G.get(i).getPosZ();
            }
            if (G.get(i).getPosZ() > t_Mz) {
                t_Mz = G.get(i).getPosZ();
            }
        }

        //Sets the bounding box
        min[0] = t_mx;
        min[1] = t_my;
        min[2] = t_mz;
        max[0] = t_Mx;
        max[1] = t_My;
        max[2] = t_Mz;
    }    
    
    //Rescale point set
    public void rescale() {

        double[] c = new double[3];
        double[] l = new double[3];

        double[] min = new double[3];
        double[] max = new double[3];

        //Calculates bounding box
        getBbox(min, max);

        //Gets the center of the bounding box
        c[0] = (double) (max[0] + min[0]) / 2;
        c[1] = (double) (max[1] + min[1]) / 2;
        c[2] = (double) (max[2] + min[2]) / 2;

        //Computes the box sides
        double size = 0;
        for (int i = 0; i < 3; i++) {
            l[i] = Math.abs(max[i] - min[i]);
            if (l[i] > size) {
                size = l[i];
            }
        }

        //Updates the vertices geometry
        for (int i = 0; i < getNvert(); i++) {
            double tx = G.get(i).getPosX();
            double ty = G.get(i).getPosY();
            double tz = G.get(i).getPosZ();

            tx -= c[0];
            if (size != 0) {
                tx /= .5f * size;
            }
            G.get(i).setPosX(tx);

            ty -= c[1];
            if (size != 0) {
                ty /= .5f * size;
            }
            G.get(i).setPosY(ty);

            tz -= c[2];
            if (size != 0) {
                tz /= .5f * size;
            }
            G.get(i).setPosZ(tz);
        }

    }  
    
    //Reads point set from .off file
    public final void readPointSet(String file) {
        try {
            Scanner scan;

            FileReader read = new FileReader(file);
            BufferedReader buf = new BufferedReader(read);

            //Test if its an .off file
            String magic = buf.readLine();
            if (!magic.equalsIgnoreCase("OFF")) 
                return;

            scan = new Scanner(buf.readLine());

            //Gets number of vertices
            nvert = Integer.parseInt(scan.next());
            
            //Reads points
            ArrayList<Point3D> tempG = new ArrayList<Point3D>(nvert);
            for (int i = 0; i < nvert; i++) {
                scan = new Scanner(buf.readLine());

                Point3D p = new Point3D();

                double x = Double.parseDouble(scan.next());
                double y = Double.parseDouble(scan.next());
                double z = Double.parseDouble(scan.next());

                p.setPosX(x);
                p.setPosY(y);
                p.setPosZ(z);
                p.setObj(new Double(0.025));

                tempG.add(p);
            }
            
            buf.close();
            
            //Allocates point set
            allocPointSet();
            
            //Copies data
            Collections.copy( G,tempG );
            
            //Rescales point set
            rescale();

        } catch (IOException ex) {
            System.out.println("Error on Che::readChe.");
        }
    }

    //Writes point set to .off file
    public void writePointSet(String file) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(file));
            DataOutputStream dat = new DataOutputStream(fos);

            dat.writeChars("OFF");

            dat.writeInt(nvert);
            dat.writeInt(0);
            dat.writeInt(0);


            for (int i = 0; i < nvert; i++) {
                Point3D p = new Point3D();

                dat.writeDouble(p.getPosX());
                dat.writeDouble(p.getPosY());
                dat.writeDouble(p.getPosZ());
            }

            dat.flush();
            dat.close();

        } catch (IOException ex) {
           System.out.println("Error on PointSet::writePointSet.");
        }
    }

    //Allocates point set
    private void allocPointSet() {
        if (nvert == 0) {
            return;
        }
        G  = new ArrayList<Point3D>(nvert);
        for (int i = 0; i < nvert; i++) {
             G.add(null);
        }
   }
}
