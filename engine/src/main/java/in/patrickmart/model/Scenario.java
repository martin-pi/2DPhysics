package in.patrickmart.model;

import in.patrickmart.model.forces.Force;
import in.patrickmart.model.forces.ForceFEA;
import in.patrickmart.model.forces.ForceGravity;
import in.patrickmart.model.forces.ForceNormal;

import java.util.ArrayList;

public class Scenario {
    private ArrayList<Entity> entities; // TODO implement Quadtree.
    private ArrayList<Entity> selectedEntities;
    private ArrayList<CollisionData> collisions;
    private ArrayList<Force> forces;
    private boolean FEAgravity;
    private boolean gravity;

    public Scenario() {
        entities = new ArrayList<Entity>();
        selectedEntities = new ArrayList<Entity>();
        collisions = new ArrayList<CollisionData>();
        forces = new ArrayList<Force>();

    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public void addForce(Force f) {
        forces.add(f);
    }

    public void removeForce(Force f) {
        forces.remove(f);
    }

    public void applyForces() {
        for (Force f : forces) {

        }
    }

    public void step() {
        // Reset the list of collisions for this new step.
        collisions = new ArrayList<>();

        // Move each object in the scenario along its velocity vector.
        for (Entity e:entities) {
            if (FEAgravity) {
                new ForceFEA(e);
            }
            if(gravity) {
                for (Entity o: entities) {
                    if (!e.equals(o)) {
                        new ForceGravity(o,e);
                    }
                }
            }
            e.calculateAcceleration();
            e.calculateVelocity();
            e.calculatePosition();
            e.step();
        }
        // Check if any collisions have occurred.
        collisionCheck();
        // For each collision that has happened, use the resulting collision data to adjust object locations, apply forces.
        collisionResponse();
    }

    /**
     * Look for entities that are close to each other, check if they are close enough to touch.
     */
    private void collisionCheck() {
        for (Entity e: entities) {
            for (Entity n: entities) {
                if (!e.equals(n))
                {
                    CollisionData c = e.collisionCheck(n);
                    if (c != null) {
                        // There is a collision. Check to make sure we haven't already detected this collision.
                        boolean duplicateFound = false;
                        for (CollisionData d : collisions) {
                            if (c.equals(d)) {
                                duplicateFound = true;
                            }
                        }
                        if (!duplicateFound) {
                            // This is not a duplicate collsion.
                            collisions.add(c);
                        }
                    }
				}
            }
        }
    }

    public Entity selectAtPosition(Vector2D point) {
        Entity selected = null;
        for (Entity e: entities){
            if(e.getBounds().containsPoint(point)) {
                selected = e;
            }
        }
        return selected;
    }

    /**
     * For each collision pair resulting from the collision check, make necessary adjustments.
     */
    private void collisionResponse() {
        //TODO iterate through collisions and call resolve on the collisiondata objects to get collisionResponses.
        for (CollisionData c : collisions){
            c.resolve(gravity, FEAgravity);
        }
    }

    /**
     * accessor for entities
     * @return list of entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Force> getForces(){
        return forces;
    }

    public void toggleFEAgravity(){
        FEAgravity = !FEAgravity;
    }

    public void toggleGravity() {
        gravity = !gravity;
    }

}
