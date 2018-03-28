package in.patrickmart.model;

import in.patrickmart.model.forces.ForceFEA;
import in.patrickmart.model.forces.ForceGeneric;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

public class ForcesTest extends TestCase
{
    /**
     * Create the test case
     * @param testName name of the test case
     */
    public ForcesTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ForcesTest.class );
    }

    public void testFEAForce(){
        Entity e = new ConcreteEntity(new Vector2D(),new ConcreteShape(3,.1));
        e.setMass(10);
        ForceFEA grav = new ForceFEA(e);
        e.calculateAcceleration();
        assertEquals(grav.getForce().getY() / e.getMass(), e.getAcceleration().getY());
    }

    public void testGenericForce(){
        Entity e = new ConcreteEntity(new Vector2D(),new ConcreteShape(3,.1));
        e.setMass(10);
        ForceGeneric g = new ForceGeneric(e, new Vector2D(0,9.8));
        e.calculateAcceleration();
        assertEquals(g.getForce().copy().getY() / e.getMass(), e.getAcceleration().getY());
    }
}