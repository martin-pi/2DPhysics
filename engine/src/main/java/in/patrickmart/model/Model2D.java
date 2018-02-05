package in.patrickmart.model;

public class Model2D {
    private Vector2D[] points;
    private double area;
    //private AABB bounds;

    public Model2D() {
        //Construct this model from a set of vectors or x/y pairs.
        this.area = calculateArea();
    }

    /**
     * Calculate the area of this model by calculating triangular area between the center and every 2 adjacent points.
     * @return the area of this Model.
     */
    private double calculateArea() {
        return 0;
    }



    public Vector2D[] getPoints() {
        return this.points;
    }
}
