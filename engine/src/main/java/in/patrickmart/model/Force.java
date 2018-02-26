package in.patrickmart.model;

public class Force {
    private Vector2D direction;

    public Force(Vector2D direction) {
        this.direction = direction;
    }

    public Vector2D getDirection(){
        return direction;
    }

    /**
     * calculate the force vector in Newtons
     * @param mass of entity
     * @return force vector in newtons
     */
    public  Vector2D calculateForce(double mass){
        Vector2D mag = direction.mult(mass);
        return mag;
    }
}
