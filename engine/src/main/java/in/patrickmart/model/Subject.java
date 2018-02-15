package in.patrickmart.model;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import in.patrickmart.view.Observer;

/**
 * This Subject class is based off of code found here:
 * https://www.techyourchance.com/thread-safe-observer-design-pattern-in-java/
 *
 * As said in the article, it is much better to use classes that have already been tested to be threadsafe, so this
 * class uses concurrent sets.
 */
abstract class Subject {
    protected final Set<Observer> observers = Collections.newSetFromMap(new ConcurrentHashMap<Observer, Boolean>(0));

    /**
     * Subscribes an observer to this subject.
     * @param observer the observer to subscribe to updates from this subject.
     */
    public void addObserver(Observer observer) {
        if (observer != null) {
            observers.add(observer); //Safe due to threadsafe Set.
        }
    }

    /**
     * Removes an observer from the updates provided by this subject.
     * @param observer the observer to unsubscribe from updates from this subject.
     */
    public void removeObserver(Observer observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    /**
     * Updates all currently subscribed observers.
     */
    abstract void updateObservers();
}