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
    public void addObserver(View view){
        this.view = view;
    }
    public String getMsg(){
        return msg;
    }
    public void changeMSG(String newMSG){
        msg = newMSG;
        notifyView();
    }
    public void notifyView(){
        view.update();
    }
}
