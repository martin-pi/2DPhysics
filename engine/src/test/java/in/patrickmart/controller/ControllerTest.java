package in.patrickmart.controller;

import in.patrickmart.model.Model;
import in.patrickmart.view.Viewer;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class ControllerTest
        extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ControllerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ControllerTest.class );
    }

    /**
     * Test whether the controller loop runs, and whether it steps with reasonable consistency.
     */
    public void testControllerLoop()
    {
        Model m = new Model();
        Controller c = new Controller(m);
        c.loop(300); //Run the controller for 300 ticks. Should take about 5 seconds.

        double acceptableVariance = 5;
        assertTrue(c.getActualTicksPerSecond() > c.getTicksPerSecond() - acceptableVariance
            && c.getTicksPerSecond() < c.getTicksPerSecond() + acceptableVariance);
    }
}