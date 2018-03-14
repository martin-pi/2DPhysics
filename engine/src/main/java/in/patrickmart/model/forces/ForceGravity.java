package in.patrickmart.model.forces;

import in.patrickmart.model.Entity;
import in.patrickmart.model.Vector2D;

public class ForceGravity extends Force {

    public ForceGravity(Entity destination){
        super(null, destination);
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
