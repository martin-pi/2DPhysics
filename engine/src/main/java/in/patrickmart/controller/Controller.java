package in.patrickmart.controller;

import in.patrickmart.model.*;
import in.patrickmart.view.*;

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
    }
}
