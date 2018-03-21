package in.patrickmart.model;
import in.patrickmart.model.forces.Force;

import java.util.Vector;

public interface Entity {
    void step();
    void applyForce(Force force);
    void calculateAcceleration();
    void calculateVelocity();
    void calculatePosition();
    CollisionData collisionCheck(Entity other);
    void collisionResponse(Entity other, Vector2D mtv);
    Shape getShape();
    AABB getBounds();
    Vector2D getPosition();
    double[] getColor();
    int getId();
    void setVelocity(Vector2D velocity);
    boolean equals(Entity e);
    String toString();
    Material getMaterial();
    Vector2D getVelocity();
    void setPosition(Vector2D position);
    double getMass();
    void setMass(double mass);
}
