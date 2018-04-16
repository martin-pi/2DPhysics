package in.patrickmart.model;

import java.util.List;
import java.util.ArrayList;

public interface Shape {
    void addShape(Shape shape);
    Shape getShape(int index);
    void removeShape(Shape shape);
    double getArea();
    double getDiameter();
    AABB calculateBounds();
    boolean containsPoint(Vector2D point);
    Vector2D intersectsShape(Shape other);
    void setPosition(Vector2D point);
    void setRotation(double rotation);
    void rotate(double rotation);
    Vector2D getPosition();
    double getRotation();
    List<Vector2D> getPoints();
    ArrayList<Shape> getSubShapes();
    ArrayList<Vector2D> getNormals();
    double[]project(Vector2D axis);
}
