package in.patrickmart.test;

public class ViewTest implements Runnable, Observer {
    private int updates;

    public ViewTest(ControllerTest c, ModelTest m) {
        System.out.println("Creating view.");
        m.addObserver(this);
    }

    public void run() {
        System.out.println("View thread initialized.");
    }

    public void update() {
        System.out.println("View: " + this.updates);
        this.updates++;
    }
}
