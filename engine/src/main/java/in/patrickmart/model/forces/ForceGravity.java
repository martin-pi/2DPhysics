package in.patrickmart.model.forces;

import in.patrickmart.model.Entity;
import in.patrickmart.model.Vector2D;

public class ForceGravity extends Force{
    static final double G = .0000000000667;

    public ForceGravity(Entity source, Entity destination){
        super(source, destination);
    }

    /**
     * Where does this force interact with its destination from?
     */
    public Vector2D calculatePosition(){
        return source.getPosition();
    }

    /**
     * What direction is this force being applied in?
     */
    public Vector2D calculateDirection(){
        return destination.getPosition().sub(source.getPosition());
    }

    /**
     * How much force is being applied?
     */
    public double calculateNewtons(){
        return G * ((source.getMass() * destination.getMass())/ Math.pow((source.getPosition().dist(destination.getPosition())), 2));
    }
}
