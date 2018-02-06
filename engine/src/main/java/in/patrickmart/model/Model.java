package in.patrickmart.model;

public class Model {
    private String msg;

    private Scenario scenario;

    public Model(){
        this.scenario = new Scenario();
    }

    public String getMsg(){
        return "Ayy Lmodel";
    }
}
