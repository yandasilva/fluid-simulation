package ic.apps.sph;

public class Vector {
    
    //A vector is a array of doubles with special properties & operations
    private double[] vec = null;
    
    //Constructors
    public Vector(double x, double y, double z){
        vec = new double[3];
        vec[0] = x;
        vec[1] = y;
        vec[2] = z;
    }
    
    public Vector(double[] d){
        vec = new double[3];
        vec = d;
    }
    
    public Vector(int n){
        vec = new double[n];
    }
    
    //Addition
    public Vector opAdd(Vector v){
        return new Vector(vec[0] + v.getVecData(0), vec[1] + v.getVecData(1), 
                vec[2] + v.getVecData(2));
    }
    
    //Subtraction
    public Vector opSub(Vector v){
        return new Vector(vec[0] - v.getVecData(0), vec[1] - v.getVecData(1), 
                vec[2] - v.getVecData(2));
    }
    
    //Scaling (multiplication by a constant)
    public Vector opScale(double s){
        return new Vector(vec[0]*s, vec[1]*s, vec[2]*s);
    }
    
    //Gets the magnitude of the vector
    public double norm(){
        double s = 0;
        for (int i = 0; i < vec.length; i++){
            s += vec[i]*vec[i];
        }
        return Math.sqrt(s);
    }
    
    //Normalizes the vector (divides its coordinates by its magnitude)
    public Vector normalize(){
        double s = norm();
        Vector result = this.opScale(1.0f/s);
        return result;
    }
    
    //Gets the dot product between this vector and another given one
    public double dot(Vector p){
        double s = 0;
        for (int i = 0; i < vec.length; i++){
            s += vec[i]*p.getVecData(i);
        }
        return s;
    }
    
    //Checks if the vector has NaN in any of its coordinates
    //Used for debugging
    public boolean hasNaN(){
        boolean hasIt = false;
        for (int i = 0; i < vec.length; i++){
            if (Double.isNaN(vec[i])){
                hasIt = true;
            }
        }
        return hasIt;
    }
    
    //Checks if two vectores are equal
    public boolean equals(Vector v){
        return (vec[0] == v.getVecData(0)) && (vec[1] == v.getVecData(1)) &&
                (vec[2] == v.getVecData(2));
    }
    
    //Checks if a vector is between two others
    public boolean isBetween(Vector min, Vector max){
        return (this.getVecData(0) <= max.getVecData(0) && this.getVecData(0) >= min.getVecData(0) &&
            this.getVecData(1) <= max.getVecData(1) && this.getVecData(1) >= min.getVecData(1) &&
                this.getVecData(1) <= max.getVecData(1) && this.getVecData(1) >= min.getVecData(1));
    }
    
    //Copies a vector
    public Vector copy(){
        return new Vector(vec[0], vec[1], vec[2]);
    }
    
    //Sets this vector coordinates to zero
    public void zero(){
        this.vec[0] = 0.0;
        this.vec[1] = 0.0;
        this.vec[2] = 0.0;
    }
    
    //Getters & setters
    public double[] getVecData(){
        return vec;
    }
    
    public double getVecData(int i){
        return vec[i];
    }
    
    public void setVecData (double[] d){
        vec = d;
    }
    
    public void setVecData (double d, int i){
        vec[i] = d;
    }
    
    public double[] getVec() {
        return vec;
    }

    public void setVec(double[] vec) {
        this.vec = vec;
    }
    
    public void setTo(Vector vec){
        this.vec[0] = vec.getVecData(0);
        this.vec[1] = vec.getVecData(1);
        this.vec[2] = vec.getVecData(2);
    }
    
    @Override
    public String toString(){
        return "(" + vec[0] +", " + vec[1] + ", " + vec[2] + ")";
    }
    
}
