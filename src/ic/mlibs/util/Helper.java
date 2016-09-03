package ic.mlibs.util;

import ic.mlibs.structures.Point3D;

public class Helper {

    //Cross product
    public static Point3D cross(Point3D v0, Point3D v1){
        double[] a0 = new double[3];
        double[] a1 = new double[3];

        a0[0] = v0.getPosX();
        a0[1] = v0.getPosY();
        a0[2] = v0.getPosZ();

        a1[0] = v1.getPosX();
        a1[1] = v1.getPosY();
        a1[2] = v1.getPosZ();

        double x =  a0[1] * a1[2] - a1[1] * a0[2] ;
        double y =  a0[2] * a1[0] - a1[2] * a0[0] ;
        double z =  a0[0] * a1[1] - a1[0] * a0[1] ;
                
        return new Point3D( x,y,z );
    }
    
    //Dot product
    public static double dot(Point3D v0, Point3D v1){
                        
        return v0.getPos().dot(v1.getPos());
    }    
    
    //Mixed product
    public static double mixed(Point3D v0, Point3D v1, Point3D v2){
        
        Point3D crs = cross(v1,v2);
                
        return dot(v0,crs);
    }
    
    //Addition
    public static Point3D add(Point3D v0, Point3D v1){
        
        double [] s = ( v0.getPos().opAdd(v1.getPos()) ).getVecData();
        
        return new Point3D( s );
    }        

    //Subtraction
    public static Point3D sub(Point3D v0, Point3D v1){
     
        double [] s = ( v0.getPos().opSub(v1.getPos()) ).getVecData();
        
        return new Point3D( s );
    }
     
    //Mid point between two points
    public static Point3D midpoint(Point3D v0, Point3D v1){
        
        double [] s = ( v0.getPos().opAdd(v1.getPos()) ).opScale(0.5f).getVecData();
        double [] n = ( v0.getNrm().opAdd(v1.getNrm()) ).opScale(0.5f).getVecData();
        
        return new Point3D( s,n );
    }        
   
    //Triangle normal
    public static void normal(Point3D v0, Point3D v1, Point3D v2, double[] n) {
        double[] a0 = new double[3];
        double[] a1 = new double[3];

        a0[0] = v1.getPosX() - v0.getPosX();
        a0[1] = v1.getPosY() - v0.getPosY();
        a0[2] = v1.getPosZ() - v0.getPosZ();

        a1[0] = v2.getPosX() - v0.getPosX();
        a1[1] = v2.getPosY() - v0.getPosY();
        a1[2] = v2.getPosZ() - v0.getPosZ();

        n[0] = a0[1] * a1[2] - a1[1] * a0[2];
        n[1] = a0[2] * a1[0] - a1[2] * a0[0];
        n[2] = a0[0] * a1[1] - a1[0] * a0[1];
    }

    //Triangle area
    public static double area(Point3D v0, Point3D v1, Point3D v2) {
        double area = 0;

        double a = sub(v1,v0).getPos().norm();
        double b = sub(v2,v1).getPos().norm();
        double c = sub(v0,v2).getPos().norm();

        double s = (a + b + c) / 2;

        return (double) Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    //Angle between points
    public static double angle(Point3D p0, Point3D p1, Point3D p2) {

        //Vectors between points
        Point3D v0 = sub(p0,p1);
        Point3D v1 = sub(p0,p2);

        //Dot product between vectors
        double dot = dot(v0,v1);

        //Vector normls
        double v0Size = v0.getPos().norm();
        v0Size = (v0Size > Double.MIN_VALUE) ? (v0Size) : (Double.MIN_VALUE);
        double v1Size = v1.getPos().norm();
        v1Size = (v1Size > Double.MIN_VALUE) ? (v1Size) : (Double.MIN_VALUE);

        //Angle between vectors
        double arg = dot / v0Size / v1Size;
        double angle = (double) Math.acos(arg);

        return angle;
    }
    
    //Intersection between edge and triangle
    public static boolean triInterEdge(Point3D vP, Point3D vQ, Point3D vA, Point3D vB, Point3D vC) {
        
	Point3D edge1 = sub(vB,vA) ;
        Point3D edge2 = sub(vC,vA) ;
        
	//Begin calculating determinant - also used to calculate U parameter
        Point3D PQ = sub(vQ,vP);
        Point3D pvec = cross(PQ, edge2);

	//If determinant is near zero, ray lies in plane of triangle
	double det = dot(edge1, pvec);

	if (det > -Double.MIN_VALUE && det < Double.MIN_VALUE)
            return false;
	double detInv = 1.0f / det;

	//Calculate distance from rt->v0 to ray origin
	Point3D tvec = sub(vP, vA);

	//Calculate U parameter and test bounds
	double u = dot(tvec, pvec) * detInv;
	if (u < 0.0 || u > 1.0)
            return false;

	//Prepare to test V parameter
	Point3D qvec = cross(tvec, edge1);

	//Calculate V parameter and test bounds
	double v = dot(PQ, qvec) * detInv;
	if (v < 0.0 || u + v > 1.0)
            return false;

	//Calculate t, ray intersects triangle
	double t = dot(edge2, qvec) * detInv;

	return t >= 0.0 && t <= 1.0;
    }    
    
    //Calculate n!
    public static int fatorial(int n){
        int res = 1;
        for (int i = 1; i <= n; i++) {
            res*=i;
        }
        
        return res;
    }
   
}
