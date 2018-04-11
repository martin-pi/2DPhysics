package in.patrickmart.model;

import in.patrickmart.model.forces.Force;
import in.patrickmart.model.forces.ForceGeneric;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

public class EntityRotationTest extends TestCase
{
    /**
     * Create the test case
     * @param testName name of the test case
     */
    public EntityRotationTest( String testName )
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
        ConcreteShape shape = new ConcreteShape(4, 1);
        ConcreteEntity entity = new ConcreteEntity(new Vector2D(), shape);

        //The above shape should have a diameter of 2.0.
        assertEquals(entity.getShape().getDiameter(), 2.0);
        assertEquals(entity.getNetTorque(), 0.0);

        // Apply a force to the entity to see if it rotates.
        ForceGeneric force = new ForceGeneric(null, entity, new Vector2D(-1, 0), new Vector2D(0, 1));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(entity.getNetTorque(), 1.0); //TODO may need to be multiplied by 0.16666.
        assertEquals(entity.getAngularAcceleration(), 1.5); //TODO may need to be multiplied by 0.16666.
        assertEquals(entity.getAngularVelocity(), 1.5); //TODO may need to be multiplied by 0.16666.
    }
}