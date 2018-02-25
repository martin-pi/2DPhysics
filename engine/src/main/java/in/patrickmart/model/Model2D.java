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





    public boolean containsPoint(Vector2D point) {
        //Not really needed as of yet.
        // TODO: use this to determine which object is clicked on for displaying information window
        return false;
    }

    /**
     * Implements Hyperplane Separation Theorem, the best named theorem in existence, to determine intersection.
     * @param other The model to check collision against
     * @return true if this model and the other model are intersecting
     */
    public boolean intersectsModel2D(Model2D other) {
	    // Build a list of normal vectors from both shapes. Each normal is one of our axes
        ArrayList<Vector2D> axes = getNormals();
        axes.addAll(other.getNormals());

        // For each axis, find the min and max dot product of that axis with each point in this shape and the other
        for (Vector2D axis : axes) {
            double[] projection = project(axis);
            double min = projection[0];
            double max = projection[1];

            double[] otherProjection = other.project(axis);
            double oMin = otherProjection[0];
            double oMax = otherProjection[1];

            // Determine if there is any overlap between the min/max of this and the other shape. if not, return false
            // seems to say there is no collision only when the objects have space between them on the x axis.
            if (!(min <= oMax && oMin <= max)) {
                    return false;
            }
        }

        // TODO Calculate the minimum translation vector and return it if there is a collision.
	    return true;
    }

    /**
     * Projects this model onto an axis, and returns the interval of that projection.
     * @param axis A normal vector to project this model onto.
     * @return The minimum and maximum dot product of the points in this model.
     */
    public double[] project(Vector2D axis) {
        double min = points.get(0).copy().add(position).dot(axis); // Start the min and max on a calculated value. 0 might not be in range.
        double max = points.get(0).copy().add(position).dot(axis);

        for (int i = 1; i < points.size(); i++) { // Find the projection of this model on this axis
            double dot = points.get(i).copy().add(position).dot(axis);

            if (dot < min) {
                min = dot;
            }
            if (dot > max) {
                max = dot;
            }
        }

        return new double[] {min, max};
    }

    /**
     * Calculates the edge vectors that define this shape. An edge is a line between two points from the points list.
     * @return An ArrayList of every edge vector in this model.
     */
    public ArrayList<Vector2D> getEdges() {
        ArrayList<Vector2D> edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            // edge[n] = points[n + 1] - points[n];
            edges.add(points.get((i + 1) % points.size()).copy().sub(points.get(i)));
        }
        return edges;
    }

    /**
     * Calculates the normal vectors that define this shape. A normal vector is perpendicular to an edge vector.
     * @return An ArrayList of every normal vector in this model.
     */
    public ArrayList<Vector2D> getNormals() {
        ArrayList<Vector2D> normals = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            // edge[n] = points[n + 1] - points[n];
            // Get the normal vector of each edge vector by swapping x and y, then negating one of them.
            normals.add(points.get((i + 1) % points.size()).copy().sub(points.get(i)).getNormal());
        }
        return normals;
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
        return this.points;
    }
    public double getArea() {
        return this.area;
    }
}
