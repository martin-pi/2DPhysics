package in.patrickmart.test;

public class ControllerTest {
    private ModelTest model;

    public ControllerTest(ModelTest model) {
        this.model = model;
    }

    public void event(ControllerEvent e) {
        switch (e) {
            case LOAD_SCENARIO:

                break;
            case SAVE_SCENARIO:

                break;
            case PLAY_SCENARIO:

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
