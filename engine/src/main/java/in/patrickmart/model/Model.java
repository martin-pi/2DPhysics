package in.patrickmart.model;

import in.patrickmart.view.*;

public class Model {
    String msg;
    public Model(){
        System.out.println("Model is loaded");
        msg = "model to view";
    }

    public String getMsg(){
        return msg;
    }
}
