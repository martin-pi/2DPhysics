package in.patrickmart.controller;

import in.patrickmart.model.*;
import in.patrickmart.model.forces.*;

import java.util.ArrayList;
import java.util.Random;

public class Controller {
    private Model model;
    private boolean running;
    private int ticksPerSecond; //How many times per second should the controller call step?
    private double actualTicksPerSecond; // How many times per second is step actually called?

    public Controller(Model model) {
        this.model = model;
        this.running = false ;
        actualTicksPerSecond = 0;
        ticksPerSecond = 60;
    }

    /**
     * The main program loop. Contains the code to both start and run the loop.
     */
    public void loop(Integer loops) {
        System.out.println("Starting the Controller Loop.");

        int ticks = 0;
        int totalTicks = 0;
        this.running = true;
        // Similar to code found at https://stackoverflow.com/questions/18283199/java-main-game-loop
        long initialTime = System.nanoTime();
        long timer = initialTime + 1000000000;
        final long updateTime = 1000000000 / (ticksPerSecond); // 1000000000 nanoseconds in a second.
        long nextUpdateTime = initialTime + updateTime;

        while (running) {
            long currentTime = System.nanoTime();

            if (currentTime >= nextUpdateTime) {
                step();
                ticks++;
                nextUpdateTime += updateTime;  //+ ((nextUpdateTime - currentTime) * 2); increased accuracy but caused hang.
            }

            if (currentTime >= timer) {
                actualTicksPerSecond = Math.floor((actualTicksPerSecond + ticks) / 2 * 100) / 100;

                totalTicks += ticks;
                ticks = 0;
                timer += 1000000000;
            }

            if (loops > 0 && totalTicks >= loops) {
                running = false;
            }
        }
    }

    /**
     * Stops the main program loop.
     */
    public void stop() {
        running = false;
        System.out.println("Stopping the Controller Loop.");
    }

    /**
     * Steps the model.
     */
    public void step() {
        model.step();
    }

    /**
     * A view event that gets handled by the controller.
     */
    public void viewEvent() {
        //TODO for now this is hardcoded to add an object at a random location. this should interpret commands from View
        Random r = new Random();
        double x = (r.nextDouble() * 2) - 1;
        double y = (r.nextDouble() * 2) - 1;
        int sides = r.nextInt((10 - 3) + 1) + 3;
        Entity e = new ConcreteEntity(new Vector2D(x,y), new ConcreteShape(sides ,.1));
        //e.setVelocity(new Vector2D((r.nextDouble() - .5) * .01,(r.nextDouble() - .5) * .01));
        //TODO: remove this. forcing mass to be large here.
        e.setMass(5000.0);
        model.addEntity(e);
        System.out.println("Added random entity #" + e.getId() + " to the model.");
    }

    /**
     * toggles the force of gravity acting on all entities
     */
    public void toggleFEA() {
        model.getScenario().toggleFEAgravity();
    }

    /**
     * toggle gravity of objects
     */
    public void toggleGravity() {
        model.getScenario().toggleGravity();
    }

    public void launchBall(double cameraScale) {
        Entity e = new ConcreteEntity(new Vector2D(1 / cameraScale,-.5 / cameraScale), new ConcreteShape(8 ,.1/cameraScale));
        e.setVelocity(new Vector2D(-1 / cameraScale,1 / cameraScale));
        e.setMass(500 / cameraScale * 2);
        model.addEntity(e);
        System.out.println("Added launched Entity #" + e.getId() + " to the model.");
    }

    public void createEntityClick(Vector2D position, double cameraScale) {
        System.out.println("Alt-Click at: " + position.getX() + ", " + position.getY());
        int sides = 12;
        Entity e = new ConcreteEntity(new Vector2D(position.getX(),position.getY()), new ConcreteShape(sides ,.1 / cameraScale));
        //e.setVelocity(new Vector2D((r.nextDouble() - .5) * .01,(r.nextDouble() - .5) * .01));
        //TODO: remove this. forcing mass to be large here.
        e.setMass(500 / cameraScale * 2);
        model.addEntity(e);
        System.out.println("Added random Entity #" + e.getId() + " to the model.");
    }

    public Force createForce(Entity applyTo, Vector2D position, Vector2D force) {
        System.out.println("Applied force (" + force.getX() + ", " + force.getY() + ")to Entity #" + applyTo.getId() + ".");
        return new ForceGeneric(null, applyTo, force, position);
    }

    public Entity selectAtPosition(Vector2D position) {
        return model.getScenario().selectAtPosition(position);
    }

    public Entity getLatestEntity() {
       ArrayList<Entity> entities = model.getScenario().getEntities();
       int highest = -1;
       Entity latest = null;
       for (Entity e : entities) {
           if (e.getId() > highest) {
               latest = e;
           }
       }
       return latest;
    }

    public void createGround(double cameraScale) {
        Entity e = new StaticEntity(new Vector2D(0,(-1 / cameraScale) - (1 / cameraScale)), new ConcreteShape(4, 2 / cameraScale));
        e.setRotation(Math.PI/4);
        model.addEntity(e);
    }

    public void clearEntities() {
        model.getScenario().clearEntities();
    }

    public void destroyEntity(Entity entity) {
        model.removeEntity(entity);
    }

    /**
     * accessor for this controller's model
     * @return model
     */
    public Model getModel(){
        return model;
    }

    /**
     * Accessor for actualTicksPerSecond, an estimate of program performance.
     * @return the most recent actualTicksPerSecond value.
     */
    public double getActualTicksPerSecond() {
        return actualTicksPerSecond;
    }

    /**
     * Accessor for ticksPerSecond.
     * @return the preset ticksPerSecond value.
     */
    public double getTicksPerSecond() {
        return ticksPerSecond;
    }
}
