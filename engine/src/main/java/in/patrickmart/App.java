package in.patrickmart;

import in.patrickmart.controller.*;
import in.patrickmart.view.*;
/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Controller c = new Controller();
        View v = new View(c.getModel(),c);
        TestGraphics t = new TestGraphics();
    }
}