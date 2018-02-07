package in.patrickmart;

import in.patrickmart.controller.*;
import in.patrickmart.view.*;
import in.patrickmart.model.*;
/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Model m = new Model();
        Controller c = new Controller(m);
        View view = new View(c,m);
        view.runTest();
    }
}