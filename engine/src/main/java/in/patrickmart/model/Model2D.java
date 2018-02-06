package in.patrickmart.model;

import java.util.ArrayList;
import java.util.List;

/**
 *	@author Patrick Martin
 *	@version 0.1
 *  Uses a set of Vector2Ds to define a convex shape.
 */
public class Model2D {
    private List<Vector2D> points;  //A collection of vectors representing offsets from the model's center of gravity.
    private double area; //The area of this model, used for calculations of mass.

    /**
     * Constructor for objects of Class Model2D
     * @param points an already-constructed list of Vector2D objects defining the points that make up this shape.
     */
    public Model2D(List<Vector2D> points) {
        this.points = points;

        //Correct the center of gravity.
        calculateCenterOfGravity();
        //Construct this model from a set of vectors or x/y pairs.
        calculateArea();
    }

    /**
     * Constructor for objects of Class Model2D, constructs an n-sided polygon.
     * @param n number of vertices to construct this model from.
     * @param radius how far each vertex is from the center of gravity.
     */
    public Model2D(int n, double radius) {
        this.points = new ArrayList<Vector2D>();

        //Calculate the angle (in radians) between each vector and the next.
        // The first vertex is directly above the center of gravity.
        double rotation = Math.PI * 2 / n;


        for (int i = 0; i < n; i++) {
            this.points.add(new Vector2D(0, radius).rotate(rotation * i));
        }

        //Correct the center of gravity.
        calculateCenterOfGravity();
        //Construct this model from a set of vectors or x/y pairs.
        calculateArea();
    }

    /**
     * Default Constructor for objects of Class Model2D, constructs a triangle with a 1m "radius".
     */
    public Model2D() {
        int n = 3;
        int radius = 1;

        //Calculate the angle (in radians) between each vector and the next.
        // The first vertex is directly above the center of gravity.
        this.points = new ArrayList<Vector2D>();

        //Add each vertex and rotate it into position.
        double rotation = Math.PI * 2 / n; // How much should each
        for (int i = 0; i < n; i++) {
            this.points.add(new Vector2D(0, radius).rotate(rotation * i));
        }
        
        //Correct the center of gravity.
        calculateCenterOfGravity();
        //Construct this model from a set of vectors or x/y pairs.
        calculateArea();
    }

    /**
     *  Adjust all vectors in this model so that their origin is the model's center of gravity.
     */
    private void calculateCenterOfGravity() {
        //TODO scan the shape to determine the actual center of gravity, offset the points by the difference.
    }

    /**
     * Calculate the area of this model by calculating triangular area between the center and every 2 adjacent points.
     * @return the area of this Model.
     */
    private void calculateArea() {
        double area = 0;
        //If there are less than 3 points, you have a line or dot instead of a shape.
        if (points.size() < 3) {
            this.area = area;
        }
        //Calculate the area of each triangle making up this shape.
        for(int i = 0; i < points.size() - 1; i++) {
            Vector2D first = points.get(i);
            Vector2D second = points.get(i + 1);
            area += Math.abs(first.getX() * second.getY() - first.getY() * second.getX()) / 2;
        }

        this.area = area;
    }



    public List<Vector2D> getPoints() {
        return this.points;
    }
    public double getArea() {
        return this.area;
    }
}
