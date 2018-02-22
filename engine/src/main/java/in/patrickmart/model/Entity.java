package in.patrickmart.model;

import java.util.Random;

public class Entity {
	public int id;
	private static int nextId = 0;
	
    private Vector2D position;
    private double rotation;
    private Vector2D velocity;
    private double rotationalVelocity;
    private Model2D model;
    private AABB bounds;

    // Colors here are being stored for debug purposes. TODO Implement materials and move colors there.
	private double[] color;
    private double[] originalColor;
	private double[] collisionColor ;

    public Entity(Vector2D position, Model2D model) {
		this.id = getNewId();

        this.model = model;
        this.bounds = model.calculateBounds(this.rotation);

        setPosition(position);
        this.rotation = 0;
        this.velocity = new Vector2D();
        this.rotationalVelocity = 0;

        // Calculate some random colors to make things look good. TODO remove these when materials are in.
        Random r = new Random();
        originalColor = new double[] {r.nextDouble(),0.65,0.80,0.75};
		this.color = originalColor;
        collisionColor = new double[] {0.9,0.4,0.4,0.75}; // Red
    }

    public Entity(Vector2D position, Model2D model, double[] color) {
		this.id = getNewId();

        this.model = model;
        this.bounds = model.calculateBounds(this.rotation);

        setPosition(position);
        this.rotation = 0;
        this.velocity = new Vector2D();
        this.rotationalVelocity = 0;

        // Add some color to make things look good. TODO remove these when materials are in.
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
     * Move this entity along its velocity vector, update its rotation.
     */
    public void calculatePosition() {
        if (rotationalVelocity != 0) { // If we have rotated, we need a new bounding box.
            bounds = model.calculateBounds(this.rotation);
        }
        setPosition(this.position); // TODO As soon as forces are being implemented, change this.
    }
	
	/**
     * Check collision between this and another entity.
     * @param other another Entity to check collision against.
     * @return null if there is no collision, otherwise a CollisionData object to aid in collisionResponse.
     */
    public CollisionData collisionCheck(Entity other) {
        // TODO Implement raycasting so we can predict collision.
        if (this.getBounds().intersectsAABB(other.getBounds())) {
            if (this.getModel().intersectsModel2D(other.getModel())) {
                //TODO implement precise collision check right about here.
            }
            return new CollisionData(this, other);
        }
        return null;
    }

    /**
     * Use the CollisionData generated from the collision check to move out of the collision and apply Normal force.
     */
    public void collisionResponse() {
		color = collisionColor;
    }
		
    /**
     * Accessor for position
     * @return position vector
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Mutator for position
     * @return position vector
     */
    public void setPosition(Vector2D position) {
        this.position = position.copy();
        this.bounds.setCenter(position.copy());
        this.model.setPosition(position.copy());
    }

    /**
     * Accessor for model
     * @return entity model
     */
    public Model2D getModel() {
        return model;
    }

    /**
     * accessor for this entity's model's bounding box.
     */
    public AABB getBounds() {
        return bounds;
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

    /**
     * Determine whether this entity is the same as some other entity.
     * @param e The entity to compare to this one
     * @return Whether or not the entities are actually the same entity
     */
	public boolean equals(Entity e) {
		return this.id == e.id;
	}

    /**
     * Creates a new unique ID for an entity. Prevents duplicate IDs.
     * @return a unique Entity ID
     */
	private static int getNewId() {
	    int r = nextId;
		nextId++;
		return r;
	}

}