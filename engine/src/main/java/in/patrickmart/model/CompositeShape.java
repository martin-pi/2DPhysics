package in.patrickmart.model;

import java.util.ArrayList;
import java.util.List;

/**
 * CompositeShape was initially meant to implement the composite pattern in order to allow for more complex shapes, but
 * it is outside of our current focus.
 */
public class CompositeShape {
    private ArrayList<Shape> shapes;
    private Vector2D position;
    private double area;

    public CompositeShape(ArrayList<Shape> shapes){
        this.shapes = shapes;
        calculateArea();
        calculateCenterOfGravity();
    }

    public void addShape(Shape model){
        shapes.add(model);
    }

    public Shape getShape(int index){
        return shapes.get(index);
    }

    public void removeShape(Shape model){
        shapes.remove(model);
    }

    /**
     * Adjust all vectors in this model so that their origin is the model's center of gravity.
     */
    private void calculateCenterOfGravity() {
        // Calculate the offset vector from the origin to the centroid (centroid = (x1 + x2 + ... + xk) / k)
        double x = 0;
        double y = 0;
        ArrayList<Vector2D> points = new ArrayList<>();
        for(Shape m : shapes){
            List<Vector2D> subpoints = m.getPoints();
            for (Vector2D point : subpoints) {
                if (!points.contains(point)) {
                    points.add(point);
                }
            }
        }
        for (Vector2D p : points) {
            x += p.getX();
            y += p.getY();
        }
        x /= points.size();
        y /= points.size();
        Vector2D offset = new Vector2D(x, y);
        // Subtract the offset vector from every point, effectively moving the origin to the center of gravity.
        for (Vector2D p : points) {
            p.sub(offset);
        }
    }

    /**
     * Calculate the area of this model by calculating triangular area between the center and every 2 adjacent points.
     * @return the area of this Model.
     */
    private void calculateArea() {
        this.area = 0;
        for(Shape m : shapes){
            this.area += m.getArea();
        }
    }

    /**
     * Calculate the bounding box of this model at its current rotation.
     */
    public AABB calculateBounds(double rotation) {
        ArrayList<Vector2D> points = new ArrayList<>();
        for(Shape m : shapes){
            List<Vector2D> subpoints = m.getPoints();
            for (Vector2D point : subpoints) {
                points.add(point);
            }
        }
        double furthestX = 0;
        double furthestY = 0;

        for (Vector2D p : points) {
            Vector2D q = p.copy().rotate(rotation);
            if (Math.abs(q.getX()) > furthestX) {
                furthestX = Math.abs(q.getX());
            }
            if (Math.abs(q.getY()) > furthestY) {
                furthestY = Math.abs(q.getY());
            }
        }

        return new AABB(new Vector2D(), furthestX, furthestY);
    }

    /**
     * determines if a point is within the model
     * @param point
     * @return true if point is within model.
     */
    public boolean containsPoint(Vector2D point) {
        for(Shape m : shapes){
            if(m.containsPoint(point)){
                return true;
            }
        }
        return false;
    }

    /**
     * Implements Hyperplane Separation Theorem, the best named theorem in existence, to determine intersection.
     * @param other The model to check collision against
     * @return true if this model and the other model are intersecting
     */
    public Vector2D intersectsShape(Shape other) {
        return null;
    }

    public double[] project(Vector2D axis) {
        double[] fixme = {0, 0};
        return fixme;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getPosition() {
        if (position != null) {
            return position;
        }
        return new Vector2D();
    }

    public List<Vector2D> getPoints() {
        List<Vector2D> points = new ArrayList<>();
        for(Shape m : shapes){
            List<Vector2D> subPoints = m.getPoints();
            for (Vector2D point : subPoints) {
                if (!points.contains(point)) {
                    points.add(point);
                }
            }
        }
        return points;
    }

    public ArrayList<Vector2D> getNormals() {
        ArrayList<Vector2D> subNormals = new ArrayList<>();
        for(Shape m : shapes){
            subNormals.addAll(m.getNormals());
        }
        return subNormals;
    }

    public ArrayList<Shape> getSubShapes(){
        ArrayList<Shape> subModels= new ArrayList<>();
        for(Shape m : shapes){
            subModels.addAll(m.getSubShapes());
        }
        return subModels;
    }
    public double getArea(){
        return this.area;
    }
}
