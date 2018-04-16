package in.patrickmart.model;
import in.patrickmart.model.forces.Force;

import java.util.ArrayList;
import java.util.Vector;

public abstract class Entity {
    private static int nextId = 0;

    public abstract void step();
    public abstract void applyForce(Force force);
    public abstract void calculateAcceleration();
    public abstract void calculateVelocity();
    public abstract void calculatePosition();
    public abstract CollisionData collisionCheck(Entity other);
    public abstract void collisionResponse(Entity other, Vector2D mtv);
    public abstract Shape getShape();
    public abstract AABB getBounds();
    public abstract Vector2D getPosition();
    public abstract double[] getColor();
    public abstract int getId();
    public abstract void setVelocity(Vector2D velocity);
    public abstract boolean equals(Entity e);
    public abstract Material getMaterial();
    public abstract Vector2D getVelocity();
    public abstract Vector2D getNetForce();
    public abstract ArrayList<Force> getForces();
    public abstract void setPosition(Vector2D position);
    public abstract void setRotation(double rotation);
    public abstract double getMass();
    public abstract void setMass(double mass);
    public abstract Vector2D getAcceleration();
    public abstract boolean isColliding();
    public abstract void setId(int id);

    /**
     * Creates a new unique ID for an entity. Prevents duplicate IDs.
     * @return a unique Entity ID
     */
    protected static int getNewId() {
        int r = nextId;
        nextId++;
        return r;
    }
}
