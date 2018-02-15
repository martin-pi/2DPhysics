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
        observers = new ArrayList<>();
    }

    /**
     * Step the model forward. TODO implement delta time to allow the model to step by variable amounts.
     */
    public void step() {
        System.out.println("Model: Stepped.");
        updateObservers();
    }

    /**
     * notifies Observers of changes.
     */
    public void updateObservers() {
        for (Observer o: observers) {
            o.update(scenario);
            System.out.println("Model: Updated an observer.");
        }
    }

    /**
     * Add a new entity to the model.
     * @param e the entity to add to the model
     */
    public void addEntity(Entity e) {
        scenario.addEntity(e);
    }

    /**
     * Remove a specific entity from the model. TODO remove entities by id
     * @param e The entity to remove from the model
     */
    public void removeEntity(Entity e) {
        scenario.removeEntity(e);
    }

    /**
     * accessor for the current frame/scenario.
     * @return current scenario
     */
    public Scenario getScenario(){
        return scenario;
    }
}
