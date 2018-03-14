package in.patrickmart.model;

import java.util.ArrayList;

public class Force {
    private Vector2D position;
    private Vector2D force;

    public Force(Entity source, Entity destination) {
        calculatePosition();
        calculateDirection();
        calculateNewtons();
        applyTo(destination);
    }


    public Force(Entity source, ArrayList<Entity> destinations) {

    }

    /**
     * Where does this force interact with its destination from?
     */
    public void calculatePosition() {

    }

    /**
     * What direction is this force being applied in?
     */
    public void calculateDirection() {

    }

    /**
     * How much force is being applied?
     */
    public void calculateNewtons() {

    }

    /**
     * Add this force to an entity's net force calculation.
     * @param destination the entity
     */
    public void applyTo(Entity destination) {
        destination.applyForce(this);
    }

    public Vector2D getForce() {
        return force;
    }

    public Vector2D getPosition() {
        return position;
    }
}
