package in.patrickmart.model;

public class Entity {
    private Vector2D position;
    private Vector2D velocity;
    private Model2D model;

    public Entity() {

    }

    public void step() {

    }


    /**
     * Check collision between this and another entity.
     * @param other another Entity to check collision against.
     * @return null if there is no collision, otherwise a CollisionData object to aid in collisionResponse.
     */
    public CollisionData checkCollision(Model2D other) {
        // TODO divide checkCollision into a rough and a precise check. Also implement AABB to do rough.
        // TODO Implement raycasting so we can predict collision.
        return null;
    }

    /**
     * Use the CollisionData generated from the collision check to move out of the collision and apply Normal force.
     * @param data Collision data for this collision.
     */
    public void collisionResponse(CollisionData data) {

    }
}