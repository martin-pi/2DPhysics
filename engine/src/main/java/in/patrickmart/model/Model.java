package in.patrickmart.model;

import in.patrickmart.view.*;

public class Model {
    private String msg;
    View view;

    private Scenario scenario;

    public Model(){
        this.scenario = new Scenario();
        msg = "Model Here";
    }
    public String getMsg(){
        return msg;
    }
    public void changeMSG(String newMSG){
        msg = newMSG;
        notifyObserver();
    }
    public void notifyObserver(){
        view.update();
    }
    public void addObserver(View view){
        this.view = view;
    }
}
