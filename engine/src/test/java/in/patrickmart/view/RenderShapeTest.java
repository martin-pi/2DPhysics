package in.patrickmart.view;

import in.patrickmart.model.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RenderShapeTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RenderShapeTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( RenderShapeTest.class );
    }

    /**
     * Test that entity coordinates are accessible from outside of the model, and that they don't break when leaving it.
     */
    public void testApp()
    {
        double acceptableError = 0.0000000001; // Vector calculations will always result in some rounding/error.

        Model m = new Model();
        m.addEntity(new ConcreteEntity(new Vector2D(1, 2), new Model2D(4, 0.5)));
        Scenario s = m.getScenario(); // A scenario can be retrieved.
        Entity e = s.getEntities().get(0); // A specific entity can be retrieved from a scenario.

        // Test that Entity locations can be retrieved properly.
        assertTrue(e.getPosition().getX() == 1);
        assertTrue(e.getPosition().getY() == 2);


        // Test that point vectors can be retrieved properly.
        assertTrue(-acceptableError <= e.getModel().getPoints().get(0).getX() &&
                e.getModel().getPoints().get(0).getX() <= acceptableError);
        assertTrue(-acceptableError + 0.5 <= e.getModel().getPoints().get(0).getY() &&
                e.getModel().getPoints().get(0).getY() <= acceptableError + 0.5);
    }
}
