package in.patrickmart.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import in.patrickmart.controller.*;
import in.patrickmart.view.View;

public class ModelTest
        extends TestCase{


    protected void setUp(){

    }
    public void testObservable() {
        Model m = new Model();
        Controller c = new Controller(m);
        View v = new View(c, m);
        assertTrue(true);
    }
}
