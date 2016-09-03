package ic.apps.sph;

//Stores collision detection results
public class CollisionResult {
    
    //Particle position
    Vector position;
    
    //Has any of its coordinates changed due to collision?
    boolean changedX;
    boolean changedY;
    boolean changedZ;

    //Constructor
    public CollisionResult(Vector position, boolean changedX, boolean changedY, 
            boolean changedZ) {
        this.position = position;
        this.changedX = changedX;
        this.changedY = changedY;
        this.changedZ = changedZ;
    }

    //Getters
    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public boolean isChangedX() {
        return changedX;
    }

    public void setChangedX(boolean changedX) {
        this.changedX = changedX;
    }

    public boolean isChangedY() {
        return changedY;
    }

    public void setChangedY(boolean changedY) {
        this.changedY = changedY;
    }

    public boolean isChangedZ() {
        return changedZ;
    }

    public void setChangedZ(boolean changedZ) {
        this.changedZ = changedZ;
    }
    
    
    
}
