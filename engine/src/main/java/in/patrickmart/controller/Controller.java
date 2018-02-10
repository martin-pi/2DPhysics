package in.patrickmart.controller;

import in.patrickmart.model.*;
import in.patrickmart.view.*;

public class Controller {
    Model model;
    double x;
    double y;

    public Controller(Model model) {
        this.model = model;
        x = 0;
        y = 0;
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
        model.createTriangle(x,y,new double[] {1 - (x*x),1 - (y*y),1 - x,1.0});
        if (x > 1) {
            x = -1;
            y = -1;
        } else {
            x += .1;
            y += .1;
        }
    }
}
