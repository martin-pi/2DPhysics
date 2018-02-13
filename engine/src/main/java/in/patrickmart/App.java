package in.patrickmart;


import in.patrickmart.test.*;
/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "App Started." );
        ModelTest m = new ModelTest();
        ControllerTest c = new ControllerTest(m);
        if(args.length >= 1 && args[0].equals("headless")) {
            System.out.println("Headless Mode");
        } else {
            ViewTest view = new ViewTest(c, m);
            view.run();
        }

        c.loop();
    }
}