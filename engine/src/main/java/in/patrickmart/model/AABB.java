package in.patrickmart.model;

public class AABB {
    private Vector2D center;
    private double halfWidth;
    private double halfHeight;

    public AABB(Vector2D center, double halfWidth, double halfHeight) {
        this.center = center;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    public AABB(double x, double y, double halfWidth, double halfHeight) {
        center = new Vector2D(x, y);
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    public boolean intersectsAABB(AABB other) {
        return false;
    }

    public boolean containsPoint(Vector2D point) {
        return false;
    }

    public Vector2D getCenter() {
        return this.center;
    }

    public double getHalfWidth() {
        return this.halfWidth;
    }

    public double getHalfHeight() {
        return this.halfHeight;
    }
}
