package in.patrickmart.model;

import in.patrickmart.model.forces.*;

import java.util.ArrayList;
import java.util.Random;

public class ConcreteEntity implements Entity{
	public int id;
	private static int nextId = 0;
	
    private Vector2D position;
    private double rotation;
    private Vector2D velocity;
    private double netTorque;  // Newton Meters
    private double momentOfInertiaCenter;
    private double momentOfInertiaEdge;
    private double angularVelocity;
    private Vector2D acceleration;
    private double angularAcceleration;

    private ArrayList<Force> forces;
    private ArrayList<Force> lastForces;
    private Vector2D netForce;
    private ConcreteShape shape;
    private Material material;
    private AABB bounds;
    private double mass;

    // When no material is specified, these are the default colors.
	private double[] color;
    private double[] originalColor;
	private boolean isColliding = false;

    public ConcreteEntity(Vector2D position, ConcreteShape shape, Material material) {
		this.id = getNewId();

		this.forces = new ArrayList<Force>();
        this.shape = shape;
        this.material = material;
        this.bounds = shape.calculateBounds(this.rotation);

        this.mass = material.getDensity() * shape.getArea();
        this.momentOfInertiaCenter = (Math.pow(shape.getDiameter(), 2) * this.mass) / 12; // Ic = (1/12)mL^2
        this.momentOfInertiaEdge = (Math.pow(shape.getDiameter(), 2) * this.mass) / 3; // Ie = (1/3)mL^2

        setPosition(position);
        this.rotation = 0;
        this.velocity = new Vector2D();
        this.angularVelocity = 0;
        acceleration = new Vector2D();
        this.angularAcceleration = 0;

        // Calculate some random colors as defaults if there are no materials.
        Random r = new Random();
        originalColor = new double[] {r.nextDouble(),0.65,0.80,0.75};
		this.color = originalColor;
    }

    public ConcreteEntity(Vector2D position, ConcreteShape shape) {
        this.id = getNewId();

        this.forces = new ArrayList<Force>();
        this.shape = shape;
        this.material = null;
        this.bounds = shape.calculateBounds(this.rotation);

        this.mass = shape.getArea();
        this.momentOfInertiaCenter = (Math.pow(shape.getDiameter(), 2) * this.mass) / 12; // Ic = (1/12)mL^2
        this.momentOfInertiaEdge = (Math.pow(shape.getDiameter(), 2) * this.mass) / 3; // Ie = (1/3)mL^2

        setPosition(position);
        this.rotation = 0;
        this.velocity = new Vector2D();
        this.angularVelocity = 0;
        this.acceleration = new Vector2D();
        this.angularAcceleration = 0;

        // Calculate some random colors as defaults if there are no materials.
        Random r = new Random();
        originalColor = new double[] {r.nextDouble(),0.65,0.80,0.75};
        this.color = originalColor;
    }

    public ConcreteEntity(Vector2D position, ConcreteShape shape, double[] color) {
		this.id = getNewId();

        this.forces = new ArrayList<Force>();
        this.shape = shape;
        this.bounds = shape.calculateBounds(this.rotation);

        this.mass = shape.getArea();
        this.momentOfInertiaCenter = (Math.pow(shape.getDiameter(), 2) * this.mass) / 12; // Ic = (1/12)mL^2
        this.momentOfInertiaEdge = (Math.pow(shape.getDiameter(), 2) * this.mass) / 3; // Ie = (1/3)mL^2

        setPosition(position);
        this.rotation = 0;
        this.velocity = new Vector2D();
        this.angularVelocity = 0;
        this.acceleration = new Vector2D();
        this.angularAcceleration = 0;

        // Calculate some random colors as defaults if there are no materials.
        originalColor = color;
		this.color = originalColor;
    }

	public void applyForce(Force force) {
        this.forces.add(force);
    }
	
	/**
	 * Give this entity a chance to react to its surroundings and act on its own.
	 */
	public void step() {
		isColliding = false;
	}
	
	/**
	 * Calculate the angular and linear acceleration of this entity.
	 */
    public void calculateAcceleration() {
        // Start by finding netForce and netTorque.
        netForce = new Vector2D();
        netTorque = 0.0;

        // Calculate the net force and net rotation from forces.
        for (Force f : forces) {
            // Calculate the new Net Torque.
            double leverX = f.getPosition().getX() - getPosition().getX();
            double leverY = f.getPosition().getY() - getPosition().getY();
            Vector2D leverArm = new Vector2D(leverX, leverY);
            double theta = leverArm.angleBetween(f.getForce());
            // T = rFsin(theta)
            netTorque += leverArm.mag() * f.getForce().mag() * (Math.sin(theta));

            // Calculate the new Net Force.
            netForce.add(f.getForce());
        }

        // Newton's Second law: netForce = mass * acceleration -> acceleration = netForce / mass
        acceleration = new Vector2D(netForce.getX() / this.mass, netForce.getY() / this.mass);

        // Similarly, for torque: netTorque = momentOfInertia(around center of mass) * angularAcceleration
        angularAcceleration = netTorque / momentOfInertiaCenter;
        if (netTorque != 0) {
            System.out.println("Net Torque: " + netTorque);
        }
        // Clear the list of forces so that they don't build up step after step.
        lastForces = forces;
        forces = new ArrayList<>();
    }

    /**
     * Apply acceleration to this entity's linear and rotational velocity.
     */
    public void calculateVelocity() {
        //acceleration * time + velocity
        //every step should be about 1/60th of a second
        //velocity = velocity.add(acceleration.mult(1/60));
        velocity = velocity.add(getAcceleration().mult(.01666));
        angularVelocity -= angularAcceleration * 0.01666;
    }

    /**
     * Move this entity along its velocity vector, update its rotation.
     */
    public void calculatePosition() {
        setPosition(this.position.add(getVelocity().mult(.01666)));
        setRotation((rotation - (angularVelocity * 0.01666)) % (Math.PI * 2));

        if (angularVelocity != 0) { // If we have rotated, we need a new bounding box.
            bounds = shape.calculateBounds(this.rotation);
        }
    }
	
	/**
     * Check collision between this and another entity.
     * @param other another Entity to check collision against.
     * @return null if there is no collision, otherwise a CollisionData object to aid in collisionResponse.
     */
    public CollisionData collisionCheck(Entity other) {
        // TODO Implement raycasting so we can predict collision.
        if (roughCollision(other)) {
            Vector2D mtv = fineCollision(other);
            if (mtv != null) {
                return new CollisionData(this, other, mtv);
            }
        }
        return null;
    }

    public boolean roughCollision(Entity other) {
        return this.getBounds().intersectsAABB(other.getBounds());
    }

    public Vector2D fineCollision(Entity other) {
        return this.getShape().intersectsShape(other.getShape());
    }

    /**
     * Use the CollisionData generated from the collision check to move out of the collision and apply Normal force.
     */
    public void collisionResponse(Entity other, Vector2D mtv) {
        isColliding = true;
		this.position.add(mtv);
    }

    /**
     * Accessor for position
     * @return position vector
     */
    public Vector2D getPosition() {
        return position.copy();
    }

    /**
     * Accessor for rotation
     * @return the angle that this entity is rotated by
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Mutator for position
     */
    public void setPosition(Vector2D position) {
        this.position = position;
        this.bounds.setCenter(position);
        this.shape.setPosition(position);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        this.shape.setRotation(rotation);
    }

    /**
     * Accessor for this entity's shape or "shape."
     * @return This entity's shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * accessor for this entity's shape's bounding box.
     */
    public AABB getBounds() {
        return bounds;
    }

    /**
     * accessor for mass
     */
    public double getMass(){
        return mass;
    }

    /**
     * Accessor for this entity's material, containing many of its static physical properties.
     * @return This entity's material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * for setting an initial launch velocity or a one time velocity change
     * @param velocity
     */
    public void setVelocity(Vector2D velocity){
        this.velocity = velocity;
    }

    public Vector2D getNetForce(){
        return this.netForce;
    }

    public double getNetTorque() {
        return this.netTorque;
    }

    public ArrayList<Force> getForces(){
        return this.lastForces;
    }

    /**
     * accessor for color.
     * @return color array
     */
    public double[] getColor() {
        //TODO remove that else, every entity needs a material.
        if (material != null) {
            return material.getColor();
        } else {
            return color;
        }
    }

    /**
     * @return String of the entity's components for debugging i guess
     */
    public String toString()
    {
        return position.toString() + ", " + shape.toString() +", " + velocity.toString() + ", " + color.toString();
    }

    /**
     * Determine whether this entity is the same as some other entity.
     * @param e The entity to compare to this one
     * @return Whether or not the entities are actually the same entity
     */
	public boolean equals(Entity e) {
		return this.id == e.getId();
	}
	public int getId(){
	    return id;
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

    /**
     * setter for entity mass
     */
    public void setMass(double mass){
        this.mass = mass;
    }

    public Vector2D getVelocity() {
        return velocity.copy();
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public Vector2D getAcceleration() {
        return acceleration.copy();
    }

    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    public boolean isColliding(){
        return isColliding;
    }
}