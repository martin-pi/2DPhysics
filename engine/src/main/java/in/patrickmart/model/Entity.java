package in.patrickmart.model;

import java.util.Random;

public class Entity {
	public int id;
	private static int maxId = 0;
	
    private Vector2D position;
    private Vector2D velocity;
    private Model2D model;
	
	private double[] color;
    private double[] originalColor;
	private double[] collisionColor;

    public Entity(Vector2D position, Model2D model) {
		this.id = getNewId();
        this.position = position;
        this.model = model;
        this.velocity = new Vector2D();
        Random r = new Random();
        originalColor = new double[] {r.nextDouble(),0.65,0.80,0.75};
		this.color = originalColor;
        collisionColor = new double[] {0.9,0.4,0.4,0.75}; // Red
    }

    public Entity(Vector2D position, Model2D model, double[] color) {
		this.id = getNewId();
        this.position = position;
        this.model = model;
        this.velocity = new Vector2D();
        originalColor = color;
		this.color = originalColor;
		collisionColor = new double[] {0.9,0.4,0.4,0.75}; // Red
    }
	
	public Vector2D applyForce(Vector2D acceleration, Force force) {
        return acceleration;
        //TODO applying a force should apply acceleration to this entity. Should acceleration also be a field?
    }
	
	/**
	 * Give this entity a chance to react to its surroundings and act on its own.
	 */
	public void step() {
		color = originalColor;
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
		//this.model.setPosition(this.position);
    }
	
	/**
     * Check collision between this and another entity.
     * @param other another Entity to check collision against.
     * @return null if there is no collision, otherwise a CollisionData object to aid in collisionResponse.
     */
    public CollisionData collisionCheck(Entity other) {
        // TODO divide checkCollision into a rough and a precise check.
        // TODO Implement raycasting so we can predict collision.
		// TODO give entity positions to their AABBs so we can use AABB.intersects(AABB) to check rough collision.
		AABB otherBounds = other.getModel().getBounds();
		double oTop = otherBounds.getCenter().getY() + otherBounds.getHalfHeight();
		double oBottom = otherBounds.getCenter().getY() - otherBounds.getHalfHeight();
		double oLeft = otherBounds.getCenter().getX() - otherBounds.getHalfWidth();
		double oRight = otherBounds.getCenter().getX() + otherBounds.getHalfWidth();
		
		double top = model.getBounds().getCenter().getY() + model.getBounds().getHalfHeight();
		double bottom = model.getBounds().getCenter().getY() - model.getBounds().getHalfHeight();
		double left = model.getBounds().getCenter().getX() - model.getBounds().getHalfWidth();
		double right = model.getBounds().getCenter().getX() + model.getBounds().getHalfWidth();
		
		if (left < oRight && right > oLeft && bottom < oTop && top > oBottom) {
			return new CollisionData(this, other);
		}
		
        return null;
    }

    /**
     * Use the CollisionData generated from the collision check to move out of the collision and apply Normal force.
     * @param data Collision data for this collision.
     */
    public void collisionResponse() {
		color = collisionColor;
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
     * @return String of the entity's components for debugging i guess
     */
    public String toString()
    {
        return position.toString() + ", " + model.toString() +", " + velocity.toString() + ", " + color.toString();
    }
	
	public boolean equals(Entity e) {
		return this.id == e.id;
	}
	
	public static int getNewId() {
		maxId++;
		return maxId;
	}

}