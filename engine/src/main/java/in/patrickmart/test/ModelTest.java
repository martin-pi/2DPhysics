package in.patrickmart.test;

public class ModelTest extends Subject {
    public void step() {
        //System.out.println("Model: Stepped.");
        updateObservers();
    }

}
