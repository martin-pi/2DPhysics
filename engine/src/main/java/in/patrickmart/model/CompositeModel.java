package in.patrickmart.model;

import java.util.List;
import java.util.ArrayList;

public interface CompositeModel {
    void addModel(Model2D model);
    Model2D getModel(int index);
    void removeModel(Model2D model);
    double getArea();
    AABB calculateBounds(double rotation);
    boolean containsPoint(Vector2D point);
    boolean intersectsModel2D(Model2D other);
    void setPosition(Vector2D point);
    Vector2D getPosition();
    List<Vector2D> getPoints();
    ArrayList<Model2D> getSubModels();
}
