package in.patrickmart.model;
import in.patrickmart.model.forces.Force;

import java.util.ArrayList;
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
    Vector2D getNetForce();
    ArrayList<Force> getForces();
    void setPosition(Vector2D position);
    void setRotation(double rotation);
    double getMass();
    void setMass(double mass);
    Vector2D getAcceleration();
    boolean isColliding();
    void setNewId(int id);
    void setId(int id);
}
