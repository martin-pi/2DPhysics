package in.patrickmart.modelrefactor;

import in.patrickmart.model.AABB;
import in.patrickmart.model.*;
import in.patrickmart.model.Vector2D;

import java.util.ArrayList;

public interface Entity {
    void step();
    Force generateForce(ArrayList<Entity> entities);
    void applyForce(Vector2D direction); //need to change this when forces are implemented
    void calculateAcceleration();
    void calculateVelocity();
    void calculatePosition();
    CollisionData collisionCheck(Entity other);
    void collisionResponse();
    Shape getModel();
    AABB getBounds();
    Vector2D getPosition();
    double[] getColor();
    int getId();
    void setVelocity(Vector2D velocity);
    boolean equals(Entity e);
    String toString();
    Material getMaterial();
    void setPosition(Vector2D position);
}
