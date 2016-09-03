package ic.mlibs.structures;

import ic.apps.sph.Vector;

public class Point3D {

    //Point position
    private Vector pos = null;
    //Point normal
    private Vector nrm = null;
    //Point associated object
    private Object  obj = null;
    
    //Constructors
    public Point3D(){
        pos = new Vector(3);
        nrm = new Vector(3);
    }

    public Point3D(double x, double y, double z) {
        pos = new Vector(x,y,z);
        nrm = new Vector(3);
    }    
    
    public Point3D(double x, double y, double z, double nx, double ny, double nz) {
        pos = new Vector( x, y, z);
        nrm = new Vector(nx,ny,nz);
    }    
    
    public Point3D(double [] p, double [] n) {
        pos = new Vector(p);
        nrm = new Vector(n);
    }
    
    public Point3D(double [] p) {
        pos = new Vector(p);
        nrm = new Vector(3);
    }
    
    //Getters and setters
    public Vector getPos() {
        return pos;
    }

    public void setPos(Vector pos) {
        this.pos = pos;
    }
    
    public double getPosX() {
        return pos.getVecData(0);
    }

    public double getPosY() {
        return pos.getVecData(1);
    }

    public double getPosZ() {
        return pos.getVecData(2);
    }
    
    public void setPosX(double x) {
        pos.setVecData(x,0);
    }

    public void setPosY(double y) {
        pos.setVecData(y,1);
    }

    public void setPosZ(double z) {
        pos.setVecData(z,2);
    }

    public Vector getNrm() {
        return nrm;
    }

    public void setNrm(Vector nrm) {
        this.nrm = nrm;
    }
    
    public double getNrmX() {
        return nrm.getVecData(0);
    }

    public double getNrmY() {
        return nrm.getVecData(1);
    }
    
    public double getNrmZ() {
        return nrm.getVecData(2);
    }
    
    public void setNrmX(double x) {
        nrm.setVecData(x,0);
    }

    public void setNrmY(double y) {
        nrm.setVecData(y,1);
    }
    
    public void setNrmZ(double z) {
        nrm.setVecData(z,2);
    }
    
    public Object getObj() {
        return obj;
    }
    
    public void setObj(Object obj) {
        this.obj = obj;
    }
}
