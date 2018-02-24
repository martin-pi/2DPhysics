package in.patrickmart;

import in.patrickmart.controller.*;
import in.patrickmart.view.*;
import in.patrickmart.model.*;

import java.util.ArrayList;

/**
 * Sets up the MVC Structure of the application, gets things running.
 */
public class App
{
    public static boolean headless = false;

    public static void main( String[] args )
    {
        parseArgs(args);
        System.out.println( "Initializing..." );
        Model m = new Model();
        Controller c = new Controller(m);
        if(headless) {
            System.out.println("Headless Mode.");
        } else {
            Viewer v = new Viewer(c, m);
        }
        c.loop(-1);
    }

    public static ArrayList<String> parseArgs(String[] args) {
        ArrayList<String> processed = new ArrayList<>();

        for (String a : args) {
            a = a.toLowerCase().replace("-", "");
            //Check if any 'full word' args are present.
            if (a.contains("help")) {
                printUsage();
            } else if (a.contains("headless")) {
                processed.add("headless");
                headless = true;
            } else {
                //Check each char for flags.
                char[] flags = a.toCharArray();
                for (char f : flags) {
                    if (f == 'h') {
                        printUsage();
                    } else if(f == 'e') {
                        headless = true;
                    }
                }
            }
        }

        return processed;
    }

    static void printUsage() {
        System.out.println("Usage:\n" +
                "-h -help\t\tPrints command usage instead of running.\n" +
                "-e -headless\tRuns simulations without a view, allowing for command line usage\n" +
                "-d -debug\t\tPrints debug printouts and displays extra info while running simulations.\n");
        System.exit(0);
    }
}