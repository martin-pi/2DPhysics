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

    /**
     * accessor for the current frame/scenario.
     * @return current scenario
     */
    public Scenario getFrame(){
        //TODO: return scenario.getObjects();
        return scenario;
    }

    /**
     * notifies Observers of changes.
     */
    public void notifyObserver() {
        for (View v: observers) {
            v.update();
        }
    }

    /**
     * adds a new view to list of observers.
     * @param view
     */
    public void addObserver(View view){
        observers.add(view);
    }

    /**
     * removes specified view from observer list.
     * @param view
     */
    public void removeObserver(View view){
        observers.remove(view);
    }
}
