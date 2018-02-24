package in.patrickmart.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

public class EntityCollisionTest extends TestCase
{
    /**
     * Create the test case
     * @param testName name of the test case
     */
    public EntityCollisionTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( EntityCollisionTest.class );
    }

    /**
     * Test whether collision checks are returning false positives.
     */
    public void testFalsePositives() {
        // Build hard-coded models for test independence. These are 3 squares, a main one, one beside and one below it.
        ArrayList<Vector2D> firstPoints = new ArrayList<>();
        firstPoints.add(new Vector2D(1, 1));
        firstPoints.add(new Vector2D(1, -1));
        firstPoints.add(new Vector2D(-1, -1));
        firstPoints.add(new Vector2D(-1, 1));
        Entity first = new Entity(new Vector2D(0, 0), new Model2D(firstPoints));

        ArrayList<Vector2D> secondPoints = new ArrayList<>();
        secondPoints.add(new Vector2D(0.5, 0.5));
        secondPoints.add(new Vector2D(0.5, -0.5));
        secondPoints.add(new Vector2D(-0.5, -0.5));
        secondPoints.add(new Vector2D(-0.5, 0.5));
        Entity second = new Entity(new Vector2D(-2, 0), new Model2D(secondPoints));

        ArrayList<Vector2D> thirdPoints = new ArrayList<>();
        thirdPoints.add(new Vector2D(0.5, 0.5));
        thirdPoints.add(new Vector2D(0.5, -0.5));
        thirdPoints.add(new Vector2D(-0.5, -0.5));
        thirdPoints.add(new Vector2D(-0.5, 0.5));
        Entity third = new Entity(new Vector2D(0, -2), new Model2D(thirdPoints));

        // Test whether rough collision returns a false positive.
        assertFalse(first.roughCollision(second));
        assertFalse(first.roughCollision(third));
        assertFalse(second.roughCollision(third));

        // Test whether fine collision returns a false positive.
        assertFalse(first.fineCollision(second));
        assertFalse(first.fineCollision(third));
        assertFalse(second.fineCollision(third));
    }

    /**
     * Test whether rough collision checks are returning true.
     */
    public static void testRoughCollision() {
        // Build hard-coded models for test independence. These are 2 triangles with some space between them.
        ArrayList<Vector2D> firstPoints = new ArrayList<>();
        firstPoints.add(new Vector2D(1, -1));
        firstPoints.add(new Vector2D(-1, -1));
        firstPoints.add(new Vector2D(0, 1));
        Entity first = new Entity(new Vector2D(0, 0), new Model2D(firstPoints));

        ArrayList<Vector2D> secondPoints = new ArrayList<>();
        secondPoints.add(new Vector2D(1, 1));
        secondPoints.add(new Vector2D(-1, 1));
        secondPoints.add(new Vector2D(0, -1));
        Entity second = new Entity(new Vector2D(-1, 1), new Model2D(secondPoints));

        // Rough Collision should return true.
        assertTrue(first.roughCollision(second));

        // Fine Collision should fail.
        assertFalse(first.fineCollision(second));
    }

    /**
     * Test whether fine collision checks are returning true.
     */
    public static void testFineCollision() {
        // Build hard-coded models for test independence. These are intersecting triangles in an hourglass shape.
        ArrayList<Vector2D> firstPoints = new ArrayList<>();
        firstPoints.add(new Vector2D(1, -1));
        firstPoints.add(new Vector2D(-1, -1));
        firstPoints.add(new Vector2D(0, 1));
        Entity first = new Entity(new Vector2D(0, 0), new Model2D(firstPoints));

        ArrayList<Vector2D> secondPoints = new ArrayList<>();
        secondPoints.add(new Vector2D(1, 1));
        secondPoints.add(new Vector2D(-1, 1));
        secondPoints.add(new Vector2D(0, -1));
        Entity second = new Entity(new Vector2D(0, 1), new Model2D(secondPoints));

        // Rough Collision should return true.
        assertTrue(first.roughCollision(second));

        // Fine Collision should also return true.
        //TODO implement fine collision. assertTrue(first.fineCollision(second));
    }
}