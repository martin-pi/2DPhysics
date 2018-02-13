package in.patrickmart.model;

import java.util.ArrayList;

public class Scenario {
    private ArrayList<Entity> entities; // TODO implement Quadtree.
    private ArrayList<CollisionData> collisions;

    public Scenario() {
        entities = new ArrayList<Entity>();
        collisions = new ArrayList<CollisionData>();
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public void step() {
        //TODO Move each object in the scenario along its velocity vector.
        for (Entity e:entities) {
            e.calculateAcceleration();
            e.calculateVelocity();
            e.calculatePosition();
        }
        //Check if a collision has occurred.
        collisionCheck();
        //If a collision has happened, use the resulting collision data to adjust object locations, apply forces.
        collisionResponse();
    }

    /**
     * Look for entities that are close to each other, check if they are close enough to touch.
     */
    private void collisionCheck() {
        for (Entity e: entities) {
            for (Entity n: entities) {
                collisions.add(e.collisionCheck(n));
            }
        }
    }

    /**
     * For each collision pair resulting from the collision check, make necessary adjustments.
     */
    private void collisionResponse() {
        //TODO implement collision response.
    }

    /**
     * accessor for entities
     * @return list of entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
