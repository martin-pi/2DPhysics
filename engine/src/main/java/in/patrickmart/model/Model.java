package in.patrickmart.model;

import in.patrickmart.view.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Model extends Subject{

    private Scenario scenario;

    public Model(){
        this.scenario = new Scenario();
    }

    /**
     * Step the model forward. TODO implement delta time to allow the model to step by variable amounts.
     */
    public void step() {
		scenario.step();
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

    /**
     * Add a new entity to the model.
     * @param e the entity to add to the model
     */
    public void addEntity(Entity e) {
        scenario.addEntity(e);
    }

    /**
     * Remove a specific Entity from the Model.
     * @param e The Entity to remove
     */
    public void removeEntity(Entity e) {
        scenario.removeEntity(e);
    }

    /**
     * Remove an Entity from the Model by searching for its ID.
     * @param id the ID of the Entity to remove
     */
    public void removeEntity(int id) {
        ArrayList<Entity> entities = scenario.getEntities();
        for (Entity e : entities) {
            if (e.getId() == id) {
                scenario.removeEntity(e);
            }
        }
    }

    /**
     * Accessor for the current Scenario.
     * @return The Scenario as it currently exists
     */
    public Scenario getScenario(){
        return scenario;
    }
}
