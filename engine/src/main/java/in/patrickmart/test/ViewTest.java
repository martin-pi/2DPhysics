package in.patrickmart.test;

public class ViewTest implements Runnable, Observer {
    private String print;
    private boolean updated;
    private int updates;

    public ViewTest() {
        this.updated = false;
        this.print = "View: ";
        System.out.println("Creating view.");
    }

    public void run() {
        System.out.println("View thread initialized.");

        loop();
    }

    public void loop() {
        while (true) {
            if (this.updated) {
                this.updated = false;
                System.out.println(this.print + this.updates);
            }
        }
    }

    public void update() {
        this.updated = true;
        this.updates++;
    }
}
