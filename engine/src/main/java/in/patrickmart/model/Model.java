package in.patrickmart.model;

import in.patrickmart.view.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Model {
    ArrayList<View> observers;

    private Scenario scenario;

    public Model(){
        this.scenario = new Scenario();
        observers = new ArrayList<View>();
    }

    /**
     * accessor for the current frame/scenario.
     * @return current scenario
     */
    public Scenario getScenario(){
        return scenario;
    }

    public void addEntity(Entity e) {
        scenario.addEntity(e);
        notifyObservers();
    }

    public void removeEntity(Entity e) {
        scenario.removeEntity(e);
    }

    /**
     * creates a triangle.
     * TODO Remove this.
     *
    public void createTriangle(double x, double y,) {

    }*/


    /**
     * adds a new view to list of observers.
     * @param view
     */
    public void addObserver(View view){
        observers.add(view);
        notifyObservers();
    }

    /**
     * removes specified view from observer list.
     * @param view
     */
    public void removeObserver(View view){
        observers.remove(view);
    }

    /**
     * notifies Observers of changes.
     */
    public void notifyObservers() {
        for (View v: observers) {
            v.update(scenario);
        }
    }
}
