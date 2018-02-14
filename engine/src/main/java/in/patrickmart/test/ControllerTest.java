package in.patrickmart.test;

public class ControllerTest {
    private boolean running;
    private ModelTest model;

    public ControllerTest(ModelTest model) {
        this.model = model;
        this.running = true;
    }

    public void loop() {
        // Similar to code found at https://stackoverflow.com/questions/18283199/java-main-game-loop
        long initialTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final int updatesPerSecond = 30;
        final long updateTime = 1000000000 / (updatesPerSecond); // 1000000000 nanoseconds in a second.
		long nextUpdateTime = initialTime + updateTime;
        int ticks = 0;

        while (running) {
            long currentTime = System.nanoTime();

            if (currentTime >= nextUpdateTime) {
                step();
                ticks++;
				nextUpdateTime += updateTime + ((nextUpdateTime - currentTime) * 2);
            } else {
				attempts++;
			}
        }

        /*double timeElapsed = (System.currentTimeMillis() - timer) * 0.001;
        System.out.println("Completed 1000 cycles in " + timeElapsed + " seconds.");
        System.out.println("Each update should have taken " + updateTime * 0.000000001 + " seconds.");
        System.out.println("1000 cycles should have taken " + updateTime * 0.000000001 * 1000 + " seconds.");
		*/
    }

    public void step() {
        model.step();
    }

    public void event(ControllerEvent e) {
        switch (e) {
            case LOAD_SCENARIO:

                break;
            case SAVE_SCENARIO:

                break;
            case RUN_SCENARIO:
                this.running = true;
                break;
            case STOP_SCENARIO:

                break;
            case CREATE_ENTITY:

                break;
            case REMOVE_ENTITY:

                break;
            default:
                System.out.println("Invalid event sent to controller.");
        }
    }
}
