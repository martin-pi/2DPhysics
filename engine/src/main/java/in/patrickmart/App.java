package in.patrickmart;

import in.patrickmart.controller.*;
import in.patrickmart.view.*;
import in.patrickmart.model.*;
/**
 * Sets up the MVC Structure of the application, gets things running.
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Starting Simulation..." );
        Model m = new Model();
        Controller c = new Controller(m);
        if(args.length >= 1 && args[0].equals("headless")) {
            System.out.println("Headless Mode");
        } else {
            View view = new View(c, m);
            view.runView();
        }
    }
}