package in.patrickmart.model;

import in.patrickmart.model.forces.*;

import java.util.ArrayList;
import java.util.Random;

public class ConcreteEntity implements Entity {
	protected int id;
	private static int nextId = 0;
	
    private Vector2D position;
    private double rotation;
    private Vector2D velocity;
    private double netTorque;  // in Newton Meters
    private double momentOfInertiaCenter; // moment if rotating around the center of mass.
    private double momentOfInertiaEdge; // moment if rotating around a point on its edge.
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

	private double[] color; // When no material is specified, this is the default color.
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
    }

    public ConcreteEntity(Vector2D position, ConcreteShape shape) {
        this.id = getNewId();

        this.forces = new ArrayList<Force>();
        this.shape = shape;
        this.material = null;
        this.bounds = shape.calculateBounds(this.rotation);

        this.mass = shape.getArea(); // With no material, we have no density.
        this.momentOfInertiaCenter = (Math.pow(shape.getDiameter(), 2) * this.mass) / 12; // Ic = (1/12)mL^2
        this.momentOfInertiaEdge = (Math.pow(shape.getDiameter(), 2) * this.mass) / 3; // Ie = (1/3)mL^2

        setPosition(position);
        this.rotation = 0;
        this.velocity = new Vector2D();
        this.angularVelocity = 0;
        this.acceleration = new Vector2D();
        this.angularAcceleration = 0;

        // Calculate some random colors as defaults since no material was provided.
        Random r = new Random();
        this.color = new double[] {r.nextDouble(),0.65,0.80,0.75};
    }

    public ConcreteEntity(Vector2D position, ConcreteShape shape, double[] color) {
		this.id = getNewId();

        this.forces = new ArrayList<Force>();
        this.shape = shape;
        this.bounds = shape.calculateBounds(this.rotation);

        this.mass = shape.getArea(); // With no material, we have no density.
        this.momentOfInertiaCenter = (Math.pow(shape.getDiameter(), 2) * this.mass) / 12; // Ic = (1/12)mL^2
        this.momentOfInertiaEdge = (Math.pow(shape.getDiameter(), 2) * this.mass) / 3; // Ie = (1/3)mL^2

        setPosition(position);
        this.rotation = 0;
        this.velocity = new Vector2D();
        this.angularVelocity = 0;
        this.acceleration = new Vector2D();
        this.angularAcceleration = 0;

		this.color = color; // A color was specified rather than a material.
    }

    /**
     * Apply a Force to this Entity.
     * @param force the Force to be applied to this Entity
     */
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

        // Calculate the net force and net rotation from forces.
        for (Force f : forces) {
            // Calculate the new Net Force.
            netForce.add(f.getForce());
        }

        // Newton's Second law: netForce = mass * acceleration -> acceleration = netForce / mass
        acceleration = new Vector2D(netForce.getX() / this.mass, netForce.getY() / this.mass);

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
        velocity = velocity.add(getAcceleration().mult(.01666));
    }

    /**
     * Move this entity along its velocity vector, update its rotation.
     */
    public void calculatePosition() {
        setPosition(this.position.add(getVelocity().mult(.01666)));

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
        if (roughCollision(other)) {
            Vector2D mtv = fineCollision(other);
            if (mtv != null) {
                return new CollisionData(this, other, mtv);
            }
        }
        return null;
    }

    /**
     * Cheap utility function for collisionCheck, using AABBs.
     * @param other the other Entity to check collision against.
     * @return true if this AABB and the other Entity's AABB are overlapping.
     */
    public boolean roughCollision(Entity other) {
        return this.getBounds().intersectsAABB(other.getBounds());
    }

    /**
     * Expensive utility function for collisionCheck, using Convex Shapes.
     * @param other the other Entity to check collision against.
     * @return true if this Shape and the other Entity's Shape are overlapping.
     */
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
     * Mutator for this entity's mass, which will override the mass gathered from density and volume.
     * @param mass the overriding mass value
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Sets the velocity of this Entity manually with no regard for real physics.
     * @param velocity the new velocity Vector2D of this Entity.
     */
    public void setVelocity(Vector2D velocity){
        this.velocity = velocity;
    }

    /**
     * Mutator for the position of this Entity. Also sets the position stored in the bounds and shape of this Entity.
     * @param position The new position value of this Entity
     */
    public void setPosition(Vector2D position) {
        this.position = position;
        this.bounds.setCenter(position);
        this.shape.setPosition(position);
    }

    /**
     * Mutator for the rotation of this Entity. Also sets the rotation of this Entity's Shape
     * @param rotation The new rotation value of this Entity
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
        this.shape.setRotation(rotation);
    }

    /**
     * Accessor for this entity's material, containing many of its static physical properties.
     * @return This Entity's Material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Accessor for this Entity's color.
     * @return an array of doubles representing RGBA values.
     */
    public double[] getColor() {
        if (material != null) {
            return material.getColor();
        } else {
            return color;
        }
    }

    /**
     * Accessor for this Entity's Shape.
     * @return This Entity's Shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Accessor for this Entity's bounding box.
     * @return The bounding box calculated from this Entity's Shape.
     */
    public AABB getBounds() {
        return bounds;
    }

    /**
     * Accessor for this Entity's mass.
     * @return The mass calculated from this Entity's Shape and Material
     */
    public double getMass(){
        return mass;
    }

    /**
     * Accessor for the Vector2D representing the net force currently acting on this Entity.
     * @return This Rntity's net force.
     */
    public Vector2D getNetForce(){
        return this.netForce;
    }

    /**
     * Accessor for the torque currently acting on this Entity.
     * @return This Entity's net torque.
     */
    public double getNetTorque() {
        return this.netTorque;
    }

    /**
     * Accessor for all Forces that are currently acting on this Entity.
     * @return an ArrayList of Forces.
     */
    public ArrayList<Force> getForces(){
        return this.lastForces; // By the time forces are current, they have been moved to lastForces.
    }

    /**
     * Accessor for this Entity's acceleration.
     * @return a Vector2D with the exact same values as this Entity's acceleration vector
     */
    public Vector2D getAcceleration() {
        return acceleration.copy();
    }

    /**
     * Accessor for this Entity's velocity.
     * @return a Vector2D with the exact same values as this Entity's velocity vector
     */
    public Vector2D getVelocity() {
        return velocity.copy();
    }

    /**
     * Accessor for position
     * @return position vector
     */
    public Vector2D getPosition() {
        return position.copy();
    }

    /**
     * Accessor for this Entity's angular acceleration.
     * @return this Entity's angular velocity, in Radians/second^2 (counter clockwise is positive)
     */
    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    /**
     * Accessor for this Entity's angular velocity.
     * @return this Entity's angular velocity, in Radians/second (counter clockwise is positive)
     */
    public double getAngularVelocity() {
        return angularVelocity;
    }

    /**
     * Accessor for rotation
     * @return the angle that this entity is rotated by
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Accessor for this entity's ID.
     * @return this entity's unique Entity ID
     */
    public int getId(){
        return id;
    }

    /**
     * Accessor for isColliding, allows non-entities to determine whether this Entity is currently colliding.
     * @return true if this Entity is colliding
     */
    public boolean isColliding() {
        return isColliding;
    }

    /**
     * Overrides equals(). Determine whether this entity is the same as some other entity.
     * @param e The entity to compare to this one
     * @return Whether or not the entities are actually the same entity
     */
    public boolean equals(Entity e) {
        return this.id == e.getId();
    }

    /**
     * Overrides toString().
     * @return String of the entity's components for debugging i guess
     */
    public String toString()
    {
        return position.toString() + ", " + shape.toString() +", " + velocity.toString() + ", " + color.toString();
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