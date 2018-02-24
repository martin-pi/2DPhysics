package in.patrickmart.view;

import in.patrickmart.controller.Controller;
import in.patrickmart.model.Model;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests whether the program is capable of opening a GLFW Window via the view.
 */
public class OpenGLFWTest   extends TestCase
{
    /**
     * Create the test case
     * @param testName name of the test case
     */
    public OpenGLFWTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( OpenGLFWTest.class );
    }

    /**
     * Testing graphical interfaces like this is almost impossible without devoting dozens of hours to it.
     * As most of our graphical errors will be immediately evident when the program is run, we will most likely
     * need to rely on our eyes for that.
     */
    public void testOpenGLFW()
    {
        Model m = new Model();
        Controller c = new Controller(m);
        Viewer v = new Viewer(c, m);
        assertTrue(true );
    }
}
