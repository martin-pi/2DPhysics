package in.patrickmart.view;

import in.patrickmart.controller.Controller;
import in.patrickmart.model.Entity;
import in.patrickmart.model.Model;
import in.patrickmart.model.Scenario;

import in.patrickmart.model.Vector2D;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Acts as an OpenGL viewer for scenarios. Implements the Observer interface and can act as an MVC's 'View'.
 */
public class Viewer implements Observer {
    private Model model;
    private Controller controller;
    private long window; // Handle for GLFW window

    /**
     * Constructor for Viewer objects.
     * @param c The MVC's controller object
     * @param m The MVC's model object
     */
    public Viewer(Controller c, Model m) {
        this.controller = c;
        this.model = m;

        openWindow();

        m.addObserver(this);
    }

    /**
     * Follows the LWJGL procedures to open and set up a window to render in.
     */
    public void openWindow() {
        // Point GLFW to System.err to log its errors, if there are any.
        GLFWErrorCallback.createPrint(System.err).set();

        // Attempt to initialize GLFW.
        if ( !glfwInit() ) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Set windowhints to modify behavior of future GLFW windows.
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // Create a new window.
        window = glfwCreateWindow(1280, 800, "Simulation Viewer", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Create callbacks to handle key presses from the user.
        createCallbacks();

        // Move the window around on the screen. https://stackoverflow.com/questions/45555227
        // GLFW's origins in C show through here, as pWidth and pHeight are "pointers".
        try ( MemoryStack stack = stackPush() ) { // Get the thread stack and push a new frame
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(window); // Point OpenGL to our new window?
        glfwSwapInterval(1); // Enable VSync.
        glfwShowWindow(window); // Now that the window is fully built, show it to the user.
    }

    /**
     * Updates the content of the window with information from the model.
     * @param s The model's currently loaded scenario
     */
    public void update(Scenario s) {
        GL.createCapabilities();

        // Set the clear or "background" color.
        glClearColor(0.8f, 1.0f, 0.8f, 0.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Draw all entities in the model.
        for (Entity e : s.getEntities()){
            // Draw this entity.
            for (int i = 0; i < e.getModel().getPoints().size(); i++) {
                // Draw triangles between the center of mass and the points making up the model.
                Vector2D v = e.getModel().getPoints().get(i);
                Vector2D w = e.getModel().getPoints().get((i + 1) % e.getModel().getPoints().size());
                glBegin(GL_TRIANGLES);
                glColor4d(e.getColor()[0],e.getColor()[1],e.getColor()[2],e.getColor()[3]);
                glVertex2d(e.getPosition().getX(), e.getPosition().getY());
                glVertex2d(v.getX() + e.getPosition().getX(), v.getY() + e.getPosition().getY());
                glVertex2d(w.getX() + e.getPosition().getX(), w.getY() + e.getPosition().getY());
                glEnd();
            }
        }

        glfwSwapBuffers(window); // Place the frame that we just rendered onto the window.

        glfwPollEvents(); // Poll for window events. This utilizes the callbacks created in createCallbacks()
    }

    /**
     * Packs up the GLFW window and closes it out elegantly.
     */
    public void closeWindow() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Creates callbacks that define the program's response to user input.
     */
    private void createCallbacks() {
        glfwSetKeyCallback(window, (window, key, scancode, action, modes) -> {
            if ((key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) || glfwWindowShouldClose(window)) {
                closeWindow();
                controller.stop();
                System.out.println("Stopped the model, closed the Viewing window.");
            }
            if ( key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                System.out.println("Added an entity to the model.");
                controller.viewEvent();
            }
        });
    }
}
