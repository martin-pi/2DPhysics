package in.patrickmart.model.forces;

import in.patrickmart.model.Entity;
import in.patrickmart.model.Vector2D;

import java.util.Random;

public class ForceGeneric extends Force {

    /**
     * Constructor for a generic force, providing a way to manually define a Force.
     * @param source The source of this force, may be null
     * @param destination The Entity to apply this force to
     * @param force A vector representing this force.
     * @param position This Force's position in the Scenario.
     */
    public ForceGeneric(Entity source, Entity destination, Vector2D force, Vector2D position) {
        super();
        this.source = source;
        this.destination = destination;
        this.position = position;
        this.force = force;
        applyTo(destination);
    }
    public ForceGeneric(Entity destination, Vector2D force) {
        super();
        this.source = null;
        this.destination = destination;
        this.position = new Vector2D();
        this.force = force;
        applyTo(destination);
    }

    @Override
    protected Vector2D calculatePosition() {
        return new Vector2D();
    }

    @Override
    protected double calculateNewtons() {
        return 0;
    }

    @Override
    protected Vector2D calculateDirection() {
        return new Vector2D();
    }
}
