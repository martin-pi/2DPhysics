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
    private Vector2D position;
	private double area; //The area of this model, used for calculations of mass.

    /**
     * Constructor for objects of Class Model2D
     * @param points an already-constructed list of Vector2D objects defining the points that make up this shape.
     */
    public Model2D(List<Vector2D> points) {
		//Construct this model from a set of vectors or x/y pairs.
        this.points = points;
		
        //Correct the center of gravity.
        calculateCenterOfGravity();
		
		//Calculate the area of this planar shape.
        calculateArea();
    }

    /**
     * Constructor for objects of Class Model2D, constructs an n-sided polygon.
     * @param n number of vertices to construct this model from.
     * @param radius how far each vertex is from the center of gravity.
     */
    public Model2D(int n, double radius) {
        //Calculate the angle (in radians) between each vector and the next.
        // The first vertex is directly above the center of gravity.
        double rotation = Math.PI * 2 / n;
		//Construct this model from a set of calculated vectors.
		this.points = new ArrayList<Vector2D>();
        for (int i = 0; i < n; i++) {
            this.points.add(new Vector2D(0, radius).rotate(rotation * i));
        }

        //Correct the center of gravity.
        calculateCenterOfGravity();
		
		//Calculate the area of this planar shape.
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
        // Calculate the offset vector from the origin to the centroid (centroid = (x1 + x2 + ... + xk) / k)
        double x = 0;
        double y = 0;
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
        //If there are less than 3 points, you have a line or dot instead of a shape.
        if (points.size() < 3) {
            return;
        }
        //Calculate the area of each triangle making up this shape.
        for(int i = 0; i < points.size(); i++) {
            Vector2D first = points.get(i);
            Vector2D second = points.get((i + 1) % points.size());
            this.area += Math.abs(first.getX() * second.getY() - first.getY() * second.getX()) / 2;
        }
    }
	
	/**
	 * Calculate the bounding box of this model at its current rotation.
	 */
	public AABB calculateBounds(double rotation) {
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

	public void setPosition(Vector2D position) {
	    this.position = position;
    }

    public Vector2D getPosition() {
        if (position != null) {
            return position;
        }
	    return new Vector2D();
    }

    public boolean containsPoint(Vector2D point) {
        return false;
    }

    public boolean intersectsModel2D(Model2D other) {
	    return false;
    }

    public List<Vector2D> getPoints() {
        return this.points;
    }
    public double getArea() {
        return this.area;
    }
}
