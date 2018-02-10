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
        assertTrue(v.getX() >= -1 && v.getX() <= 1 && v.getY() >= -1 && v.getY() <= 1);
    }
}
