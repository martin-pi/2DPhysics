package in.patrickmart.model;

import java.util.ArrayList;

public class Scenario {
    private ArrayList<Entity> entities; // TODO implement Quadtree.
    private ArrayList<Force> forces;

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
                e.collisionCheck(n);
            }
        }
    }

    /**
     * For each collision pair resulting from the collision check, make necessary adjustments.
     */
    private void collisionResponse() {

    }
}
