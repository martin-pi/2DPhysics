package in.patrickmart.controller;

import in.patrickmart.model.*;
import in.patrickmart.model.forces.*;

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
    public void stop()
    {
        running = false;
        System.out.println("Stopping the Controller Loop.");
    }

    /**
     * Steps the model.
     */
    public void step()
    {
        model.step();
    }

    /**
     * A view event that gets handled by the controller.
     */
    public void viewEvent(){
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
    public void toggleFEA(){
        model.getScenario().toggleFEAgravity();
    }

    /**
     * toggle gravity of objects
     */
    public void toggleGravity(){
        model.getScenario().toggleGravity();
    }

    public void launchBall(double cameraScale){
        Entity e = new ConcreteEntity(new Vector2D(1 / cameraScale,-.5 / cameraScale), new ConcreteShape(8 ,.1/cameraScale));
        e.setVelocity(new Vector2D(-.2 / cameraScale,.1 / cameraScale));
        e.setMass(500 / cameraScale * 2);
        model.addEntity(e);
        System.out.println("Added launch entity #" + e.getId() + " to the model.");
    }
    public void createEntityClick(Vector2D position, double cameraScale){
        System.out.println("click at: " + position.getX() + ", " + position.getY());
        int sides = 12;
        Entity e = new ConcreteEntity(new Vector2D(position.getX(),position.getY()), new ConcreteShape(sides ,.1 / cameraScale));
        //e.setVelocity(new Vector2D((r.nextDouble() - .5) * .01,(r.nextDouble() - .5) * .01));
        //TODO: remove this. forcing mass to be large here.
        e.setMass(500 / cameraScale * 2);
        model.addEntity(e);
        System.out.println("Added random entity #" + e.getId() + " to the model.");
    }

    public void clickSelect(Vector2D position)
    {
        model.getScenario().pointCollisionCheck(position);
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
