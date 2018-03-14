package in.patrickmart.model.forces;

import in.patrickmart.model.Entity;
import in.patrickmart.model.Vector2D;

import java.util.ArrayList;

public abstract class Force {
    private Vector2D position;
    private Vector2D force;

    //Store the source and destination entities in order to provide data for our calculate methods.
    protected Entity source;
    protected Entity destination;

    /**
     * Constructor for objects of class force. Implements template method pattern to allow concrete forces to calculate
     * their effects separately from the way those effects are applied.
     * @param source The Entity that is generating this force
     * @param destination The Entity that this force is being applied to.
     */
    public Force(Entity source, Entity destination) {
        this.source = source;
        this.destination = destination;

        position = calculatePosition();
        force = calculateDirection();
        force.setMag(calculateNewtons());
        applyTo(destination);
    }

    public Force(Entity source, ArrayList<Entity> destinations) {
        //TODO implement multiple destination forces later.
        System.out.println("Multi-destination forces have not been implemented, make separate forces for each.");
    }

    /**
     * Where does this force interact with its destination from?
     */
    abstract Vector2D calculatePosition();

    /**
     * What direction is this force being applied in?
     */
    abstract Vector2D calculateDirection();

    /**
     * How much force is being applied?
     */
    abstract double calculateNewtons();

    /**
     * Add this force to an entity's net force calculation.
     * @param destination the entity
     */
    private void applyTo(Entity destination) {
        destination.applyForce(this);
    }

    /**
     * Accessor for this force as a vector.
     * @return This force as a positionless vector.
     */
    public Vector2D getForce() {
        return force;
    }

    /**
     * Accessor for the position of this force.
     * @return This force's position in the scenario.
     */
    public Vector2D getPosition() {
        return position;
    }
}
