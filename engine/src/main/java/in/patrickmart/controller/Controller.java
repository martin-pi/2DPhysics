package in.patrickmart.controller;

import in.patrickmart.model.*;

public class Controller {
    Model model;

    public Controller() {
        this.model = new Model();

        System.out.println("Ayy Controllers Lmao");
    }

    public Model getModel(){
        return model;
    }
}
