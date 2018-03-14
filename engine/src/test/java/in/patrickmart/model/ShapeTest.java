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
}