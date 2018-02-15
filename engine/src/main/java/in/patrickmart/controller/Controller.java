package in.patrickmart.controller;

import in.patrickmart.model.*;
import in.patrickmart.view.*;
import java.util.Random;

public class Controller {
    Model model;
    boolean running;

    public Controller(Model model) {
        this.model = model;
        this.running = false ;
    }

    /**
     * accessor for model
     * @return model
     */
    public Model getModel(){
        return model;
    }

    public void stop()
    {
        running = false;
    }

    public void loop() {
        this.running = true;
        // Similar to code found at https://stackoverflow.com/questions/18283199/java-main-game-loop
        long initialTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final int updatesPerSecond = 30;
        final long updateTime = 1000000000 / (updatesPerSecond); // 1000000000 nanoseconds in a second.
        long nextUpdateTime = initialTime + updateTime;
        int ticks = 0;

        while (running) {
            long currentTime = System.nanoTime();

            if (currentTime >= nextUpdateTime) {
                step();
                ticks++;
                nextUpdateTime += updateTime + ((nextUpdateTime - currentTime) * 2);
            }
        }
    }

    public void step()
    {
        model.step();
    }
    /**
     * a view event that gets handled by the controller.
     */
    public void viewEvent(){
        //TODO for now this is hardcoded to add an object at a random location. this should interpret commands from View
        Random r = new Random();
        double x = (r.nextDouble() * 2) - 1;
        double y = (r.nextDouble() * 2) - 1;
        int sides = r.nextInt((10 - 3) + 1) + 3;
        model.addEntity(new Entity(new Vector2D(x,y), new Model2D(sides ,.5)));
    }
}
