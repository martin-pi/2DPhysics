package in.patrickmart.model;

import java.util.ArrayList;

public class Scenario {
    private ArrayList<Entity> entities; // TODO implement Quadtree.
    private ArrayList<CollisionData> collisions;
    private ArrayList<Force> forces;

    public Scenario() {
        entities = new ArrayList<Entity>();
        collisions = new ArrayList<CollisionData>();
        forces = new ArrayList<Force>();
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public void step() {
		// Reset the list of collisions for this new step.
		collisions = new ArrayList<CollisionData>();
        // Move each object in the scenario along its velocity vector.
        for (Entity e:entities) {
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
                        boolean duplicate = false;
                        for (CollisionData d : collisions) {
                            if (c.equals(d)) {
                                duplicate = true;
                            }
                        }
                        if (!duplicate) {
                            // This is not a duplicate collsion.
                            collisions.add(c);
                        }
                    }
				}
            }
        }
    }

    /**
     * For each collision pair resulting from the collision check, make necessary adjustments.
     */
    private void collisionResponse() {
        //TODO iterate through collisions and call resolve on the collisiondata objects to get collisionResponses.
        for (CollisionData c : collisions){
            c.resolve();
        }
    }

    /**
     * accessor for entities
     * @return list of entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * add a force to the scenario
     * @param f the force to be added
     */
    public void addForce(Force f){
        forces.add(f);
    }

    public void removeForce(){
        //TODO: actually remove specific forces
        forces.clear();
    }

    public ArrayList<Force> getForces(){
        return forces;
    }

}
