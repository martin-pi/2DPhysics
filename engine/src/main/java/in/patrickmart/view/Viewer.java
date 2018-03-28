package in.patrickmart.view;

import in.patrickmart.controller.Controller;
import in.patrickmart.model.Entity;
import in.patrickmart.model.*;
import in.patrickmart.model.Scenario;

import in.patrickmart.model.Vector2D;
import in.patrickmart.model.forces.Force;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;

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
    private Vector2D camera;
    private double cameraScale;

    private boolean mouse_rb_down;
    private double mouse_rb_initialX;
    private double mouse_rb_initialY;
    private double mouse_x;
    private double mouse_y;

    /**
     * Constructor for Viewer objects.
     * @param c The MVC's controller object
     * @param m The MVC's model object
     */
    public Viewer(Controller c, Model m) {
        this.controller = c;
        this.model = m;

        mouse_rb_down = false;
        mouse_rb_initialX = 0;
        mouse_rb_initialY = 0;
        mouse_x = 0;
        mouse_y = 0;

        camera = new Vector2D(0,0);
        cameraScale = 1;

        openWindow();

        m.addObserver(this);
    }

    /**
     * Follows the LWJGL procedures to open and set up a window to render in.
     */
    public void openWindow() {
        System.out.println("Opening the Viewing Window.");

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
        // Get the mouse position.
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(window,x,y);
        double mouse_x_prev = mouse_x;
        double mouse_y_prev = mouse_y;
        mouse_x = x[0] / cameraScale;
        mouse_y = y[0] / cameraScale;

        if (mouse_rb_down) {
            camera.add(new Vector2D((mouse_x - mouse_x_prev) / 640, (mouse_y_prev - mouse_y) / 400));
        }

        // Set the clear or "background" color.
        //glClearColor(0.8f, 1.0f, 0.8f, 0.0f); // Green
        glClearColor(0.95f, 0.95f, 0.95f, 0.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Draw all entities in the model.
        for (Entity e : s.getEntities()) {
            /*draw the bounding box
            AABB b = e.getBounds();
            glBegin(GL_LINE_LOOP );
            glColor4d(0,0,1,.0003);
            glVertex2d(e.getPosition().getX() + b.getHalfWidth(), e.getPosition().getY() + b.getHalfHeight());
            glVertex2d(e.getPosition().getX() + b.getHalfWidth(), e.getPosition().getY() - b.getHalfHeight());
            glVertex2d(e.getPosition().getX() - b.getHalfWidth(), e.getPosition().getY() - b.getHalfHeight());
            glVertex2d(e.getPosition().getX() - b.getHalfWidth(), e.getPosition().getY() + b.getHalfHeight());
            glEnd();
            */
            // Draw this entity.
            for (int i = 0; i < e.getShape().getPoints().size(); i++) {
                // Draw triangles between the center of mass and the points making up the model.
                Vector2D v = e.getShape().getPoints().get(i);
                Vector2D w = e.getShape().getPoints().get((i + 1) % e.getShape().getPoints().size());
                glBegin(GL_TRIANGLES);
                glColor4d(e.getColor()[0], e.getColor()[1], e.getColor()[2], e.getColor()[3]);
                glVertex2d((e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
                glVertex2d((v.getX() * cameraScale) + (e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (v.getY() * cameraScale) + (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
                glVertex2d((w.getX() * cameraScale) + (e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (w.getY() * cameraScale) + (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
                glEnd();
            }

            // Draw the net force acting on this Entity.
            glBegin(GL_LINES);
            glColor4d(0,0,0,0);
            glVertex2d((e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
            glVertex2d(((e.getPosition().getX() + e.getNetForce().getX()*10) * cameraScale) + (camera.getX() * cameraScale), ((10*e.getNetForce().getY() + e.getPosition().getY()) * cameraScale) + (camera.getY() * cameraScale));
            glEnd();
            // Draw the net force acting on this Entity.
            for (Force f : e.getForces()) {
                glBegin(GL_LINES);
                glColor4d(0.4, 0.4, 0.4, 0);
                glVertex2d((e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
                glVertex2d(((e.getPosition().getX() + f.getForce().getX() * 10) * cameraScale) + (camera.getX() * cameraScale), ((10 * f.getForce().getY() + e.getPosition().getY()) * cameraScale) + (camera.getY() * cameraScale));
                glEnd();
            }
            // Draw this Entity's velocity.
            glBegin(GL_LINES);
            glColor4d(1,0.1,0.1,0);
            glVertex2d((e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
            glVertex2d(((e.getPosition().getX() + e.getVelocity().getX()*100) * cameraScale) + (camera.getX() * cameraScale), ((100*e.getVelocity().getY() + e.getPosition().getY()) * cameraScale) + (camera.getY() * cameraScale));
            glEnd();
        }

        glfwSwapBuffers(window); // Place the frame that we just rendered onto the window.

        glfwPollEvents(); // Poll for window events. This utilizes the callbacks created in createCallbacks()
    }

    /**
     * Packs up the GLFW window and closes it out elegantly.
     */
    public void closeWindow() {
        // Free the window callbacks and destroy the window
        controller.stop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        System.out.println("Closed the Viewing Window.");

        System.exit(1);
    }

    /**
     * Creates callbacks that define the program's response to user input.
     */
    private void createCallbacks() {
        glfwSetKeyCallback(window, (window, key, scancode, action, modes) -> {
            if ((key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) || glfwWindowShouldClose(window)) {
                closeWindow();
            }
            if ( key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                controller.viewEvent(); //TODO expand this "Command pattern?"
            }
            if ( key == GLFW_KEY_G && action == GLFW_PRESS) {
                controller.toggleFEA();
            }
            if ( key == GLFW_KEY_B && action == GLFW_PRESS) {
                controller.toggleGravity();
            }
            if ( key == GLFW_KEY_L && action == GLFW_PRESS) {
                controller.launchBall();
            }
            if (key == GLFW_KEY_LEFT && action == GLFW_PRESS) {
                camera.add(new Vector2D(1,0));
            }
            if (key == GLFW_KEY_RIGHT && action == GLFW_PRESS) {
                camera.add(new Vector2D(-1,0));
            }
            if (key == GLFW_KEY_UP && action == GLFW_PRESS) {
                camera.add(new Vector2D(0,-1));
            }
            if (key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
                camera.add(new Vector2D(0,1));
            }
        });

        glfwSetMouseButtonCallback(window,(window, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                double[] x = new double[1];
                double[] y = new double[1];
                int[] h = new int[1];
                int[] w = new int[1];
                glfwGetCursorPos(window,x,y);
                glfwGetWindowSize(window,h,w);
                double x_cursor = (x[0] - (h[0] / 2)) / (h[0] / 2);
                double y_cursor = ((w[0] / 2) - y[0])/ (w[0] / 2);

                Vector2D modelCoords = translateCoordsToModel(new Vector2D(x_cursor, y_cursor));
                controller.clickEvent(modelCoords.getX(), modelCoords.getY());
            }

            if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS) {
                mouse_rb_down = true;

                double[] x = new double[1];
                double[] y = new double[1];
                glfwGetCursorPos(window,x,y);
                mouse_rb_initialX = x[0] / cameraScale;
                mouse_rb_initialY = y[0] / cameraScale;
            }

            if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE) {
                mouse_rb_down = false;
            }
        });

        glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
            if (yoffset > 0) { // Zoom in.
                cameraScale = cameraScale * 2;
            } else if (yoffset < 0) { // Zoom out.
                cameraScale = cameraScale / 2;
            }
        });

        glfwSetWindowCloseCallback(window,(window) ->
            closeWindow()
        );
    }

    private Vector2D translateCoordsToModel(Vector2D viewCoords) {
        double x = (viewCoords.getX() / cameraScale) - (camera.getX());
        double y = (viewCoords.getY() / cameraScale) - (camera.getY());
        return new Vector2D(x, y);
    }

}
