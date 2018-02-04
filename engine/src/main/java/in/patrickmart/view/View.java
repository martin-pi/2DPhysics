package in.patrickmart.view;

import in.patrickmart.model.*;
import in.patrickmart.controller.*;

public class View {
    Model model;

    public View(Model model, Controller controller){
        this.model = model;
        System.out.println(model.getMsg());
    }
}
