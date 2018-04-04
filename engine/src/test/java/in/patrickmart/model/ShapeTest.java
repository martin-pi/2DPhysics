package in.patrickmart.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

public class ShapeTest extends TestCase
{
    /**
     * Create the test case
     * @param testName name of the test case
     */
    public ShapeTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ShapeTest.class );
    }

    /**
     * Test the creation of models via bad predefined points.
     */
    public void testBadPoints()
    {
        // Try to create a shape without enough predefined points.
        ArrayList<Vector2D> points = new ArrayList<>();
        points.add(new Vector2D(-1, 1));
        points.add(new Vector2D(1, 1));

        ConcreteShape failedModel = new ConcreteShape(points);
        assertTrue(failedModel.getArea() == 0);
    }

    /**
     * Test the creation of models via predefined points.
     */
    public void testGoodPoints()
    {
        // Create a shape with predefined points.
        ArrayList<Vector2D> points = new ArrayList<>();
        points.add(new Vector2D(-1, 1));
        points.add(new Vector2D(1, 1));
        points.add(new Vector2D(1, -1));
        points.add(new Vector2D(-1, -1));

        ConcreteShape model = new ConcreteShape(points);
        assertTrue(model.getArea() == 4.0);
    }

    /**
     * Test the creation of models via automation.
     */
    public void testGeneration() {
        //Generate a 4 sided shape with each point 1m from the center.
        ConcreteShape model = new ConcreteShape(4, 1);
        assertTrue(model.getArea() == 2.0);
    }

    /**
     * Test rotating a shape. Does it move all of the individual points to the right place?
     * Also test both methods of rotating.
     */
    public void testRotation() {
        double ROTATE_BY = 0.5;
        //Define a vector that has no rotation. (0 rotation is pointed East in the cartesian coordinate system.)
        Vector2D noRotation = new Vector2D().fromAngle(0);
        // Generate a 4 sided shape and determine the angle of a vertex.
        ConcreteShape shape = new ConcreteShape(4, 1);
        double angleBefore = shape.getPoints().get(0).angleBetween(noRotation);
        // Rotate the entire shape.
        shape.rotate(ROTATE_BY);

        // Determine the amount that a single vertex of the shape was rotated by.
        double angleAfter = shape.getPoints().get(0).angleBetween(noRotation);
        double difference = angleAfter - angleBefore;

        assertTrue(difference == ROTATE_BY);
    }
}