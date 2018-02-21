package in.patrickmart.model;

import java.util.Random;

public class Entity {
	public int id;
    private Vector2D position;
    private Vector2D velocity;
    private Model2D model;
    private double[] color;

    public Entity(Vector2D position, Model2D model) {
        this.position = position;
        this.model = model;
        this.velocity = new Vector2D();
        Random r = new Random();
        color = new double[] {r.nextDouble(),0.65,0.80,0.75};
    }

    public Entity(Vector2D position, Model2D model, double[] color) {
        this.position = position;
        this.model = model;
        this.velocity = new Vector2D();
        this.color = color;
    }
	
	public Vector2D applyForce(Vector2D acceleration, Force force) {
        return acceleration;
        //TODO applying a force should apply acceleration to this entity. Should acceleration also be a field?
    }
	
	/**
	 * Give this entity a chance to react to its surroundings and act on its own.
	 */
	public void step() {
		
	}
	
	/**
	 * Calculate the angular and linear acceleration of this entity.
	 */
    public void calculateAcceleration() {
        //this.acceleration = new Vector2D();
        //applyForce(gravity); //Something like this?
    }

    /**
     * Apply acceleration to this entity's linear and rotational velocity.
     */
    public void calculateVelocity() {

    }

    /**
     * Move this entity along its velocity vector.
     */
    public void calculatePosition() {

    }
	
	/**
	 * Check this Entity against all entities in its surroundings for potential collision.
	 */ 
	public boolean collisionCheck() {
		return false;
	}
	
	/**
	 * Resolve all collisions involving this entity.
	 */
	public void collisionResponse() {
		
	}

    /**
     * accessor for position
     * @return position vector
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * accessor for model
     * @return entity model
     */
    public Model2D getModel() {
        return model;
    }

    /**
     * accessor for color.
     * @return color array
     */
    public double[] getColor() {
        return color;
    }


    /**
     * Check collision between this and another entity.
     * @param other another Entity to check collision against.
     * @return null if there is no collision, otherwise a CollisionData object to aid in collisionResponse.
     */
    public CollisionData collisionCheck(Entity other) {
        // TODO divide checkCollision into a rough and a precise check. Also implement AABB to do rough.
        // TODO Implement raycasting so we can predict collision.
        return null;
    }

    /**
     * Use the CollisionData generated from the collision check to move out of the collision and apply Normal force.
     * @param data Collision data for this collision.
     */
    public void collisionResponse(CollisionData data) {

    }

    /**
     * @return String of the entity's components for debugging i guess
     */
    public String toString()
    {
        return position.toString() + ", " + model.toString() +", " + velocity.toString() + ", " + color.toString();
    }

}