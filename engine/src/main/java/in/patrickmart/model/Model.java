package in.patrickmart.model;

import in.patrickmart.view.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Model extends Subject{
    ArrayList<Observer> observers;

    private Scenario scenario;

    public Model(){
        this.scenario = new Scenario();
        observers = new ArrayList<Observer>();
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
        updateObservers();
    }

    public void removeEntity(Entity e) {
        scenario.removeEntity(e);
    }


    /**
     * adds a new view to list of observers.
     * @param view
     */
    public void addObserver(View view){
        observers.add(view);
        updateObservers();
    }

    /**
     * removes specified view from observer list.
     * @param view
     */
    public void removeObserver(View view){
        observers.remove(view);
    }

    public void step() {
        //System.out.println("Model: Stepped.");
        updateObservers();
    }

    /**
     * notifies Observers of changes.
     */
    public void updateObservers() {
        for (Observer o: observers) {
            o.update(scenario);
        }
    }
}
