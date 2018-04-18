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
     * Toggles the application of a Flat-Earth-Approximation gravity to all dynamic Entities.
     */
    public void toggleFEA() {
        model.getScenario().toggleFEAgravity();
    }

    /**
     * Toggle application of gravitational force between all Entities.
     */
    public void toggleGravity() {
        model.getScenario().toggleGravity();
    }

    /**
     * launches a ball from the right hand corner of the screen.
     * @param cameraPosition
     * @param cameraScale
     */
    public void launchBall(Vector2D cameraPosition, double cameraScale) {
        Entity e = new ConcreteEntity(new Vector2D(-cameraPosition.getX() + (1 / cameraScale),-cameraPosition.getY()), new ConcreteShape(8 ,.1/cameraScale));
        e.setVelocity(new Vector2D(-1 / cameraScale,0));
        e.setMass(500 / cameraScale * 2);
        model.addEntity(e);
        System.out.println("Added launched Entity #" + e.getId() + " to the scenario.");
    }

    /**
     * creates an entity at the location of the click
     * @param position
     * @param cameraScale
     */
    public void createEntityClick(Vector2D position, double cameraScale) {
        int sides = 12;
        Entity e = new ConcreteEntity(new Vector2D(position.getX(),position.getY()), new ConcreteShape(sides ,.1 / cameraScale));
        e.setMass(500 / cameraScale * 2);
        model.addEntity(e);
        System.out.println("Added random Entity #" + e.getId() + " to the scenario.");
    }

    /**
     * crates a force when clicking and dragging.
     * @param applyTo
     * @param position
     * @param force
     * @return the created force
     */
    public Force createForce(Entity applyTo, Vector2D position, Vector2D force) {
        //TODO A verbose output flag should allow messages like the one below to be logged.
        //System.out.printf("Applied force (%.3f, %.3f) to Entity #%d%n", force.getX(), force.getY(), applyTo.getId());
        return new ForceGeneric(null, applyTo, force, position);
    }

    /**
     * attempts to select and entity at the pointer's position
     * @param position
     * @return found entity or null
     */
    public Entity selectAtPosition(Vector2D position) {
        System.out.println("Attempting to select an Entity.");
        Entity found = model.getScenario().selectAtPosition(position);

        if (found == null) {
            System.out.println("\tNo Entity Found.");
        } else {
            System.out.println("\tFound Entity #" + found.getId());
        }
        return found;
    }

    /**
     * gets last entity created
     * @return latest entity
     */
    public Entity getLatestEntity() {
        System.out.println("Selecting the most recently created Entity.");
        ArrayList<Entity> entities = model.getScenario().getEntities();
        int highest = -1;
        Entity latest = null;
        for (Entity e : entities) {
            if (e.getId() > highest) {
                latest = e;
            }
        }

        if (latest == null) {
            System.out.println("\tNo Entity Found.");
        } else {
            System.out.println("\tFound Entity #" + latest.getId());
        }
       return latest;
    }

    /**
     * creates a static object to represent the ground at the bottom of the screen
     * @param cameraScale
     */
    public void createGround(double cameraScale) {
        Entity e = new StaticEntity(new Vector2D(0,(-1 / cameraScale) - (2 / cameraScale)), new ConcreteShape(4, 3 / cameraScale));
        e.setRotation(Math.PI/4);
        model.addEntity(e);
        e = new StaticEntity(new Vector2D((1 / cameraScale) - (3.5 / cameraScale),(1 / cameraScale) - (1 / cameraScale)), new ConcreteShape(4, 2 / cameraScale));
        e.setRotation(Math.PI/4);
        model.addEntity(e);

        e = new StaticEntity(new Vector2D((1 / cameraScale) + (1.5 / cameraScale),0), new ConcreteShape(4, 2 / cameraScale));
        e.setRotation(Math.PI/4);
        model.addEntity(e);
        System.out.println("Added static Entity #" + e.getId() + " to the scenario.");
    }

    /**
     * clears all entities from the model.
     */
    public void clearEntities() {
        model.getScenario().clearEntities();
        System.out.println("Deleted all Entities from the scenario.");
    }

    /**
     * deletes the selected entity
     * @param entity
     */
    public void destroyEntity(Entity entity) {
        model.removeEntity(entity);
        System.out.println("Deleted Entity #" + entity.getId() + " from the scenario.");
    }

    /**
     * Accessor for this controller's model
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
