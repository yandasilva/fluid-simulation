package ic.apps.sph;

import ic.mlibs.structures.Point3D;

//A particle is an extension of a Point3D
//Made for more readability since the fluid is composed of particles
public class Particle extends Point3D{
    
    //Constructor
    public Particle(double posX, double posY, double posZ){
        super(posX, posY, posZ);
    }
    
    //toString to write its position in a readable manner for debugging
    @Override
    public String toString(){
        return "("+getPosX()+", "+getPosY()+", "+getPosZ()+")";
    }
    
}
