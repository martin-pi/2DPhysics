package in.patrickmart.model;

public class Force {
    private Vector2D direction;
    private Vector2D acceleration;

    public Force(Vector2D direction) {
        this.direction = direction;
    }

    public Vector2D getDirection(){
        return direction;
    }
}
