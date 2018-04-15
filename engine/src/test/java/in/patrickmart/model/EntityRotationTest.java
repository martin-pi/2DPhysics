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
        return new TestSuite( EntityRotationTest.class );
    }

    /**
     * Test whether applying a CCW force will rotate an entity positively.
     */
    public void testPositiveRotation() {
        ConcreteShape shape = new ConcreteShape(4, 1);
        ConcreteEntity entity = new ConcreteEntity(new Vector2D(), shape);

        //The above shape should have a diameter of 2.0.
        assertEquals(entity.getShape().getDiameter(), 2.0);
        assertEquals(entity.getNetTorque(), 0.0);

        // Apply a force to the entity to see if it rotates.
        ForceGeneric forceTop = new ForceGeneric(null, entity, new Vector2D(-1, 0), new Vector2D(0, 1));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(1.0, entity.getNetTorque(),0.00000001);
        assertEquals(1.5, entity.getAngularAcceleration(),0.00000001);
        assertEquals(1.5 * 0.01666, entity.getAngularVelocity(),0.00000001);
        assertTrue(entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0);

        //Reset the entity.
        entity = new ConcreteEntity(new Vector2D(), shape);

        // Apply a force to the entity to see if it rotates.
        ForceGeneric forceRight = new ForceGeneric(null, entity, new Vector2D(0, 1), new Vector2D(1, 0));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(1.0, entity.getNetTorque(),0.00000001);
        assertEquals(1.5, entity.getAngularAcceleration(),0.00000001);
        assertEquals(1.5 * 0.01666, entity.getAngularVelocity(), 0.00000001);
        assertTrue(entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0);

        //Reset the entity.
        entity = new ConcreteEntity(new Vector2D(), shape);

        // Apply a force to the entity to see if it rotates.
        ForceGeneric forceAngled = new ForceGeneric(null, entity, new Vector2D(1, 1), new Vector2D(1, 0));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(1.0, entity.getNetTorque(),0.00000001);
        assertEquals(1.5, entity.getAngularAcceleration(),0.00000001);
        assertEquals(1.5 * 0.01666, entity.getAngularVelocity(), 0.00000001);
        assertTrue(entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0);

        /*System.out.println("T = " + entity.getNetTorque());
        System.out.println("Angular Acc = " + entity.getAngularAcceleration());
        System.out.println("Angular Vel = " + entity.getAngularVelocity());
        System.out.println("Acc = " + entity.getAcceleration());
        System.out.println("Vel = " + entity.getVelocity());*/
    }

    /**
     * Test whether applying a CW force will rotate an entity negatively.
     */
    public void testNegativeRotation() {
        ConcreteShape shape = new ConcreteShape(4, 1);
        ConcreteEntity entity = new ConcreteEntity(new Vector2D(), shape);
        ForceGeneric forceBot = new ForceGeneric(null, entity, new Vector2D(-1, 0), new Vector2D(0, -1));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(-1.0, entity.getNetTorque(),0.00000001);
        assertEquals(-1.5, entity.getAngularAcceleration(),0.00000001);
        assertEquals(-1.5 * 0.01666, entity.getAngularVelocity(), 0.00000001);
        assertTrue(entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0);


        entity = new ConcreteEntity(new Vector2D(), shape);
        ForceGeneric forceLeft = new ForceGeneric(null, entity, new Vector2D(0, 1), new Vector2D(-1, 0));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(-1.0, entity.getNetTorque(),0.00000001);
        assertEquals(-1.5, entity.getAngularAcceleration(),0.00000001);
        assertEquals(-1.5 * 0.01666, entity.getAngularVelocity(), 0.00000001);
        assertTrue(entity.getAcceleration().getY() == 0.5);
    }

    /**
     * Test cases that should not apply any rotation.
     */
    public void testZeroRotation() {
        ConcreteShape shape = new ConcreteShape(4, 1);
        ConcreteEntity entity = new ConcreteEntity(new Vector2D(), shape);
        ForceGeneric forceCenter = new ForceGeneric(null, entity, new Vector2D(-1, 0), new Vector2D(0, 0));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(0.0, entity.getNetTorque(),0.00000001);
        assertEquals(0.0, entity.getAngularAcceleration(),0.00000001);
        assertEquals(0.0, entity.getAngularVelocity(), 0.00000001);
        //assertTrue(entity.getVelocity().getX() == -0.5);

        entity = new ConcreteEntity(new Vector2D(), shape);
        ForceGeneric forcePull = new ForceGeneric(null, entity, new Vector2D(-1, 0), new Vector2D(-1, 0));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(0.0, entity.getNetTorque(),0.00000001);
        assertEquals(0.0, entity.getAngularAcceleration(),0.00000001);
        assertEquals(0.0, entity.getAngularVelocity(), 0.00000001);
        assertTrue(entity.getAcceleration().getX() == -0.5);
    }

    public void testLeverArmAngle() {
        ConcreteShape shape = new ConcreteShape(4, 1);
        ConcreteEntity entity = new ConcreteEntity(new Vector2D(), shape);
        ForceGeneric force = new ForceGeneric(null, entity, new Vector2D(1, 0), new Vector2D(0, -1));

        double leverX = force.getPosition().getX() - shape.getPosition().getX();
        double leverY = force.getPosition().getY() - shape.getPosition().getY();
        Vector2D leverArm = new Vector2D(leverX, leverY);
        System.out.println("Lever Arm: " + leverArm + " Length: " + leverArm.mag() + " Heading: " + (leverArm.heading() * 57.295779513));
        System.out.println("Force: " + force.getForce() + " Length: " + force.getForce().mag() + " Heading: " + (force.getForce().heading() * 57.295779513));
        System.out.println("Angle Between: " + (leverArm.angleBetween(force.getForce()) * 57.295779513));


        force.getForce().rotate(0.785398);
        System.out.println("Force: " + force.getForce() + " Length: " + force.getForce().mag() + " Heading: " + (force.getForce().heading() * 57.295779513));
    }

    public void testEntityNotAtOrigin() {
        ConcreteShape shape = new ConcreteShape(4, 1);
        ConcreteEntity entity = new ConcreteEntity(new Vector2D(), shape);
        entity.setPosition(new Vector2D(1, 1));

        //The above shape should have a diameter of 2.0.
        assertEquals(entity.getShape().getDiameter(), 2.0);
        assertEquals(entity.getNetTorque(), 0.0);

        // Apply a force to the entity to see if it rotates.
        ForceGeneric forceTop = new ForceGeneric(null, entity, new Vector2D(-1, 0), new Vector2D(1, 2));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(1.0, entity.getNetTorque(),0.00000001);
        assertEquals(1.5, entity.getAngularAcceleration(),0.00000001);
        assertEquals(1.5 * 0.01666, entity.getAngularVelocity(),0.00000001);
        assertTrue(entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0);

        // Try a negative rotation.
        entity = new ConcreteEntity(new Vector2D(), shape);
        entity.setPosition(new Vector2D(1, 1));

        ForceGeneric forceBot = new ForceGeneric(null, entity, new Vector2D(-1, 0), new Vector2D(1, 0));

        //Step the entity forward.
        entity.calculateAcceleration();
        entity.calculateVelocity();
        entity.calculatePosition();
        entity.step();

        assertEquals(-1.0, entity.getNetTorque(),0.00000001);
        assertEquals(-1.5, entity.getAngularAcceleration(),0.00000001);
        assertEquals(-1.5 * 0.01666, entity.getAngularVelocity(), 0.00000001);
        assertTrue(entity.getVelocity().getX() != 0 || entity.getVelocity().getY() != 0);
    }
}