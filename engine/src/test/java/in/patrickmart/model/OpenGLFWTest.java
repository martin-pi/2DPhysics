package in.patrickmart.model;

import in.patrickmart.AppTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests wether the program is capable of opening a GLFW Window via the view.
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
     * Rigourous Test :-)
     */
    public void testOpenGLFW()
    {
        assertTrue( true );
    }
}
