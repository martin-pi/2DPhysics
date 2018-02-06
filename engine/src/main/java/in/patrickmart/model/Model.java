package in.patrickmart.model;

public class Model {
    String msg;

    public Model(){
        System.out.println("Model is loaded");
        msg = "model to view";
    }

    public String getMsg(){
        return msg;
    }
}
