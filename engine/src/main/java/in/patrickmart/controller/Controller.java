package in.patrickmart.controller;

import in.patrickmart.model.*;
import in.patrickmart.view.*;

public class Controller {
    Model model;
    int track; //this is just to track the updates from the view key event

    public Controller(Model model) {
        this.model = model;
        track = 0;
    }

    public Model getModel(){
        return model;
    }

    public void viewEvent(){
        track++;
        model.changeMSG("button pressed " + track + " times");
    }
}
