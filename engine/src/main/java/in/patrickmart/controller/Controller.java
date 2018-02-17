package in.patrickmart.controller;

import in.patrickmart.model.*;
import in.patrickmart.view.*;

import java.util.Optional;
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
     * The main program loop. Containst the code to start and run the loop.
     */
    public void loop(Integer loops) {
        loops = loops != null ? loops : -1;

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
                System.out.println("TPS: " + actualTicksPerSecond + " (Attempting " + ticksPerSecond + "TPS)");

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
        model.addEntity(new Entity(new Vector2D(x,y), new Model2D(sides ,.5)));
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
