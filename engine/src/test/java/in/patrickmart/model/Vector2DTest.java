package in.patrickmart.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class Vector2DTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Vector2DTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( Vector2DTest.class );
    }

    /**
     * Test the creation of new vectors.
     */
    public void testConstructors()
    {
        Vector2D v = new Vector2D(); //Default constructor
        assertTrue( v.getX() == 0 && v.getY() == 0);
        v = new Vector2D(2, 3); //Custom X and Y constructor
        assertTrue( v.getX() == 2 && v.getY() == 3);
        Vector2D u = v.copy(); //Duplication of a vector.
        assertTrue(u.getX() == 2 && v.getY() == 3);

        u = new Vector2D().random(); //Random vector
        assertTrue(u.getX() >= -1 && u.getX() <= 1 && u.getY() >= -1 && u.getY() <= 1);
    }

    /**
     * tests the set and get methods of the library.
     */
    public void testGettersAndSetters()
    {
        Vector2D v = new Vector2D(5,5);
        double x = 5;
        double y = 5;
        assertEquals(x,v.getX());
        assertEquals(y,v.getY());
        x = 7;
        y = 9;
        v.setX(x);
        v.setY(y);
        assertEquals(x,v.getX());
        assertEquals(y,v.getY());
        v.set(x,y);
        assertEquals(x,v.getX());
        assertEquals(y,v.getY());
    }

    /**
     * tests the vector to string method.
     */
    public void testVectorToString()
    {
        double x = 9;
        double y = 8;
        Vector2D v = new Vector2D(x,y);
        assertEquals("(9.0, 8.0)",v.toString());
    }

    /**
     * tests that the copied vector matches the initial vector.
     */
    public void testVectorCopy()
    {
        double x = 8;
        double y = 9;
        Vector2D v = new Vector2D(x,y);
        Vector2D c = v.copy();
        assertEquals(v.getX(),c.getX());
        assertEquals(v.getY(),c.getY());
    }

    /**
     * tests vector subtraction.
     */
    public void testSubtract()
    {
        Vector2D v1 = new Vector2D(2,7);
        Vector2D v2 = new Vector2D(5,9);
        Vector2D result = new Vector2D(2 - 5, 7 - 9);
        assertEquals(result.toString(), v1.sub(v2).toString());
    }

    /**
     * tests vector addition.
     */
    public void testAddition()
    {
        Vector2D v1 = new Vector2D(2,7);
        Vector2D v2 = new Vector2D(5,9);
        Vector2D result = new Vector2D(2 + 5, 7 + 9);
        assertEquals(result.toString(), v1.add(v2).toString());
    }

    /**
     * tests vector multiplication.
     */
    public void testMultiplication()
    {
        double x = 4;
        double y = 6;
        double s = 5;
        Vector2D v1 = new Vector2D(x,y);
        Vector2D result = new Vector2D(x * s, y * s);
        assertEquals(result.toString(), v1.mult(s).toString());
    }

    /**
     * tests vector division.
     */
    public void testDivision()
    {
        double x = 4;
        double y = 6;
        double s = 5;
        Vector2D v1 = new Vector2D(x,y);
        Vector2D result = new Vector2D(x / s, y / s);
        assertEquals(result.toString(), v1.div(s).toString());
    }

}
