package in.patrickmart.model;

public class ForceGravity extends Force {

    public ForceGravity(Entity dest){
        super(null,dest);
    }
    /**
     * force comes from directly above each object
     */
    public Vector2D calculatePosition(){
        return destination.getPosition().copy().add(new Vector2D(0,1));
    }

    /**
     * What direction is this force being applied in?
     */
    public Vector2D calculateDirection(){
        return new Vector2D(0, -.00098);
    }

    /**
     * How much force is being applied?
     */
    public double calculateNewtons(){
        double newts = .00098 * destination.getMass();

        return newts;
    }
}
