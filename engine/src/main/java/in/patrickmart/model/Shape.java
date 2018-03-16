package in.patrickmart.model;

import java.util.List;
import java.util.ArrayList;

public interface Shape {
    void addShape(Shape shape);
    Shape getShape(int index);
    void removeShape(Shape shape);
    double getArea();
    AABB calculateBounds(double rotation);
    boolean containsPoint(Vector2D point);
    Vector2D intersectsShape(Shape other);
    void setPosition(Vector2D point);
    Vector2D getPosition();
    List<Vector2D> getPoints();
    ArrayList<Shape> getSubShapes();
    ArrayList<Vector2D> getNormals();
    double[]project(Vector2D axis);
}
