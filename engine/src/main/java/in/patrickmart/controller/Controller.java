package in.patrickmart.controller;

import in.patrickmart.model.*;
import in.patrickmart.view.*;
import java.util.Random;

public class Controller {
    Model model;

    public Controller(Model model) {
        this.model = model;
    }

    /**
     * accessor for model
     * @return model
     */
    public Model getModel(){
        return model;
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
