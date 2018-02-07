package in.patrickmart.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import in.patrickmart.controller.*;

public class ModelTest
        extends TestCase{


    protected void setUp(){

    }
    public void testChangeMsgNoObserver() {
        Model m = new Model();
        m.changeMSG("test");
        assertTrue(m.getMsg().equals("test"));
    }
    public void testChangeMsgWithObserver() {
        Model m = new Model();
        Controller c = new Controller(m);
        m.changeMSG("test");
        assertTrue(m.getMsg().equals("test"));
        assertTrue(m.view != null);
    }
}
