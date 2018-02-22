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
		double oTop = other.getCenter().getY() + other.getHalfHeight();
		double oBottom = other.getCenter().getY() - other.getHalfHeight();
		double oLeft = other.getCenter().getX() - other.getHalfWidth();
		double oRight = other.getCenter().getX() + other.getHalfWidth();
		
		double top = center.getY() + halfHeight;
		double bottom = center.getY() - halfHeight;
		double left = center.getX() - halfWidth;
		double right = center.getX() + halfWidth;
		
		if (left < oRight && right > oLeft && bottom < oTop && top > oBottom) {
			return true;
		}
        return false;
    }

    public boolean containsPoint(Vector2D point) {
        return false; //TODO implement containsPoint.
    }

    public Vector2D getCenter() {
        return this.center;
    }
	
	public void setCenter(Vector2D newCenter) {
		this.center = newCenter.copy();
	}

    public double getHalfWidth() {
        return this.halfWidth;
    }

    public double getHalfHeight() {
        return this.halfHeight;
    }
}
