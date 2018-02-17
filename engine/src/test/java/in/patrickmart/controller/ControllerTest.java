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
     * Rigourous Test :-)
     */
    public void testControllerLoop()
    {
        Model m = new Model();
        Controller c = new Controller(m);
        long initialTime = System.nanoTime();
        c.loop(); //need to figure out how to run this in a separate thread
        c.viewEvent();
        c.stop();
        long testTime = (System.nanoTime() - initialTime) / c.ticks;
        long expectedTime = 3; //need to figure out how much time this should take
        assertTrue(testTime <= expectedTime);
    }
}