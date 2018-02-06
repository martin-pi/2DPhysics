package in.patrickmart.model;

public class Entity {
    private Vector2D position;
    private Vector2D velocity;
    private Model2D model;

    public Entity(Vector2D position, Model2D model) {
        this.position = position;
        this.model = model;
        this.velocity = new Vector2D();
    }

    public void calculateAcceleration() {
        //this.acceleration = new Vector2D();
        //applyForce(gravity); //Something like this?
    }

    public Vector2D applyForce(Vector2D acceleration, Force force) {
        return acceleration;
        //TODO applying a force should apply acceleration to this entity. Should acceleration also be a field?
    }

    /**
     * Apply acceleration to this entity's velocity.
     */
    public void calculateVelocity() {

    }

    /**
     * Move this entity along its velocity vector.
     */
    public void calculatePosition() {

    }




    /**
     * Check collision between this and another entity.
     * @param other another Entity to check collision against.
     * @return null if there is no collision, otherwise a CollisionData object to aid in collisionResponse.
     */
    public CollisionData collisionCheck(Entity other) {
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