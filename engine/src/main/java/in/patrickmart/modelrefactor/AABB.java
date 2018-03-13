package in.patrickmart.modelrefactor;

import in.patrickmart.model.Vector2D;

public class AABB {
    private Vector2D center;
    private double halfWidth;
    private double halfHeight;

    /**
     * Constructor for objects of class AABB with a center vector.
     * @param center The position of the center of this box
     * @param halfWidth Half the total width of this box
     * @param halfHeight Half the total height of this box
     */
    public AABB(Vector2D center, double halfWidth, double halfHeight) {
        this.center = center;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    /**
     * Constructor for objects of class AABB without a known center vector.
     * @param x The x position of the center of this box
     * @param y The y position of the center of this box
     * @param halfWidth Half the total width of this box
     * @param halfHeight Half the total height of this box
     */
    public AABB(double x, double y, double halfWidth, double halfHeight) {
        center = new Vector2D(x, y);
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    /**
     * Tests whether any part of this bounding box intersects any part of another bounding box.
     * @param other The other bounding box
     * @return true if there is any overlap between the two boxes
     */
    public boolean intersectsAABB(AABB other) {
		double oTop = other.getCenter().getY() + other.getHalfHeight();
		double oBottom = other.getCenter().getY() - other.getHalfHeight();
		double oLeft = other.getCenter().getX() - other.getHalfWidth();
		double oRight = other.getCenter().getX() + other.getHalfWidth();
		
		double top = center.getY() + halfHeight;
		double bottom = center.getY() - halfHeight;
		double left = center.getX() - halfWidth;
		double right = center.getX() + halfWidth;
		
		return left <= oRight && right > oLeft && bottom < oTop && top >= oBottom;
    }

    /**
     * Returns true if the provided point is within this bounding box.
     * @param point The point to test
     * @return true if the point is within this bounding box
     */
    public boolean containsPoint(Vector2D point) {
        double top = center.getY() + halfHeight;
        double bottom = center.getY() - halfHeight;
        double left = center.getX() - halfWidth;
        double right = center.getX() + halfWidth;

        return point.getX() < right && point.getX() >= left && point.getY() >= bottom && point.getY() < top;
    }

    /**
     * Mutator for the center vector of this bounding box.
     * @param newCenter The new position of this bounding box
     */
    public void setCenter(Vector2D newCenter) {
        this.center = newCenter.copy();
    }

    /**
     * Accessor for the center vector of this bounding box.
     * @return a vector representing the position of the center of this bounding box.
     */
    public Vector2D getCenter() {
        return this.center;
    }

    /**
     * Accessor for the value of half the width of this bounding box.
     * @return Half the total width of this bounding box
     */
    public double getHalfWidth() {
        return this.halfWidth;
    }

    /**
     * Accessor for the value of half the height of this bounding box.
     * @return Half the total height of this bounding box
     */
    public double getHalfHeight() {
        return this.halfHeight;
    }
}
