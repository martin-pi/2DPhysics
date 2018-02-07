package in.patrickmart.model;

import in.patrickmart.view.*;
import java.util.ArrayList;

public class Model {
    private String msg;
    ArrayList<View> observers;

    private Scenario scenario;

    public Model(){
        this.scenario = new Scenario();
        observers = new ArrayList<View>();
        msg = "Model Here";
    }
    public String getMsg(){
        return msg;
    }
    public void changeMSG(String newMSG){
        msg = newMSG;
        notifyObserver();
    }
    public void notifyObserver() {
        for (View v: observers) {
            v.update();
        }
    }
    public void addObserver(View view){
        observers.add(view);
    }
    public void removeObserver(View view){
        observers.remove(view);
    }
}
