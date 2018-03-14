package in.patrickmart.model.forces;

import in.patrickmart.model.Entity;
import in.patrickmart.model.Vector2D;

import java.util.Random;

public class ForceGeneric extends Force {
    public ForceGeneric(Entity source, Entity destination){
        super(null, destination);
    }

    @Override
    protected Vector2D calculatePosition() {
        return destination.getPosition().copy();
    }

    @Override
    protected double calculateNewtons() {
        Random r = new Random();
        return r.nextDouble();
    }

    @Override
    protected Vector2D calculateDirection() {
        return new Vector2D().random();
    }
}
