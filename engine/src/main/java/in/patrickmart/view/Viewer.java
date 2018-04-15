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
import java.util.Vector;

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
    private boolean mouse_lb_down;
    private double mouse_rb_initialX;
    private double mouse_rb_initialY;
    private double mouse_lb_initialX;
    private double mouse_lb_initialY;
    private double mouse_x;
    private double mouse_y;

    private boolean showBoundingBox;
    private boolean showNetForce;
    private boolean showForces;
    private boolean showVelocity;
    private boolean showAcceleration;
    private boolean showCollisions;
    private boolean altKey;
    private boolean showAll;

    private Entity selected;

    private double[] collisionColor ;

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

        showBoundingBox = false;
        showNetForce = false;
        showForces = false;
        showVelocity = false;
        showAcceleration = false;
        showCollisions = false;
        altKey = false;
        showAll = false;

        collisionColor = new double[] {0.9,0.4,0.4,0.75}; // Red

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


        if (mouse_rb_down) { // Move the camera.
            camera.add(new Vector2D((mouse_x - mouse_x_prev) / 640, (mouse_y_prev - mouse_y) / 400));
        }

        // Set the clear or "background" color.
        //glClearColor(0.8f, 1.0f, 0.8f, 0.0f); // Green
        glClearColor(0.95f, 0.95f, 0.95f, 0.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Draw all entities in the model.
        for (Entity e : s.getEntities()) {
            // Draw this entity.
            glBegin(GL_TRIANGLES);
            for (int i = 0; i < e.getShape().getPoints().size(); i++) {
                // Draw triangles between the center of mass and the points making up the model.
                Vector2D v = e.getShape().getPoints().get(i);
                Vector2D w = e.getShape().getPoints().get((i + 1) % e.getShape().getPoints().size());

                if (e.isColliding() && showCollisions) {
                    glColor4d(collisionColor[0],collisionColor[1],collisionColor[2],collisionColor[3]);
                } else {
                    glColor4d(e.getColor()[0],e.getColor()[1], e.getColor()[2], e.getColor()[3]);
                }

                if (selected != null && e.equals(selected)) {
                    glColor4d(0.2,0.8,0.2, 1);
                }
                glVertex2d((e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
                glVertex2d((v.getX() * cameraScale) + (e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (v.getY() * cameraScale) + (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
                glVertex2d((w.getX() * cameraScale) + (e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (w.getY() * cameraScale) + (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
            }
            glEnd();

            // Draw the net force acting on this Entity.
            if(showNetForce) {
                glBegin(GL_LINES);
                glColor4d(0, 0, 0, 0);
                glVertex2d((e.getPosition().getX() + camera.getX()) * cameraScale, (e.getPosition().getY() + camera.getY()) * cameraScale);
                glVertex2d((e.getPosition().getX() + e.getNetForce().normalize().getX() * 10 + camera.getX()) * cameraScale, (e.getNetForce().normalize().getY() * 10 + e.getPosition().getY() + camera.getY()) * cameraScale);
                glEnd();
            }
            // Draw each individual force.
            if(showForces){
                for (Force f : e.getForces()) {
                    glBegin(GL_LINES);
                    glColor4d(0.4, 0.4, 0.4, 0);
                    glVertex2d((f.getPosition().getX() + camera.getX()) * cameraScale, (f.getPosition().getY() + camera.getY()) * cameraScale);
                    glVertex2d((f.getPosition().getX() + f.getForce().normalize().getX()*5 + camera.getX()) * cameraScale, ( f.getForce().normalize().getY()*5 + f.getPosition().getY() + camera.getY()) * cameraScale);
                    glEnd();
                }
            }
            // Draw this Entity's velocity.
            if(showVelocity) {
                glBegin(GL_LINES);
                glLineWidth(3);
                glColor4d(1, 0.1, 0.1, 0);
                glVertex2d((e.getPosition().getX() + camera.getX()) * cameraScale, (e.getPosition().getY() + camera.getY()) * cameraScale);
                glVertex2d((e.getPosition().getX() + e.getVelocity().normalize().getX() * 20 + camera.getX()) * cameraScale, ( e.getVelocity().normalize().getY() * 20 + e.getPosition().getY() + camera.getY()) * cameraScale);
                glEnd();
            }
            // Draw the Entity's acceleration.
            if(showAcceleration) {
                glBegin(GL_LINES);
                glColor4d(.6, 0, .9, 0);
                glVertex2d((e.getPosition().getX() + camera.getX()) * cameraScale, (e.getPosition().getY() + camera.getY()) * cameraScale);
                glVertex2d((e.getPosition().getX() + e.getAcceleration().normalize().getX() * 15 + camera.getX()) * cameraScale, ( e.getAcceleration().normalize().getY() * 15 + e.getPosition().getY() + camera.getY()) * cameraScale);
                glEnd();
            }
            // Draw the bounding box.
            if(showBoundingBox) {
                AABB b = e.getBounds();
                glBegin(GL_LINE_LOOP);
                glColor4d(0, 0, 1, .0003);
                glVertex2d(((e.getPosition().getX() + b.getHalfWidth()) + camera.getX()) * cameraScale, ((e.getPosition().getY() + b.getHalfHeight()) + camera.getY()) * cameraScale);
                glVertex2d(((e.getPosition().getX() + b.getHalfWidth()) + camera.getX()) * cameraScale, ((e.getPosition().getY() - b.getHalfHeight()) + camera.getY()) * cameraScale);
                glVertex2d(((e.getPosition().getX() - b.getHalfWidth()) + camera.getX()) * cameraScale, ((e.getPosition().getY() - b.getHalfHeight()) + camera.getY()) * cameraScale);
                glVertex2d(((e.getPosition().getX() - b.getHalfWidth()) + camera.getX()) * cameraScale, ((e.getPosition().getY() + b.getHalfHeight()) + camera.getY()) * cameraScale);
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
            if(!altKey) {
                if ((key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) || glfwWindowShouldClose(window)) {
                    closeWindow();
                }
                if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                    controller.viewEvent(); //TODO expand this "Command pattern?"
                }
                if (key == GLFW_KEY_G && action == GLFW_PRESS) {
                    controller.toggleFEA();
                }
                if (key == GLFW_KEY_B && action == GLFW_PRESS) {
                    controller.toggleGravity();
                }
                if (key == GLFW_KEY_L && action == GLFW_PRESS) {
                    //controller.launchBall(cameraScale);
                    controller.testRotationalForces();
                }
                if (key == GLFW_KEY_LEFT && action == GLFW_PRESS) {
                    camera.add(new Vector2D(1/cameraScale, 0));
                }
                if (key == GLFW_KEY_RIGHT && action == GLFW_PRESS) {
                    camera.add(new Vector2D(-1/cameraScale, 0));
                }
                if (key == GLFW_KEY_UP && action == GLFW_PRESS) {
                    camera.add(new Vector2D(0, -1/cameraScale));
                }
                if (key == GLFW_KEY_DOWN && action == GLFW_PRESS) {
                    camera.add(new Vector2D(0, 1/cameraScale));
                }
                if (key == GLFW_KEY_S && action == GLFW_PRESS) {
                    selected = controller.getLatestEntity();
                }
            }
            else{
                if (key == GLFW_KEY_B && action == GLFW_PRESS) {
                    showBoundingBox = !showBoundingBox;
                }
                if (key==GLFW_KEY_C && action == GLFW_PRESS) {
                    showCollisions = !showCollisions;
                }
                if (key==GLFW_KEY_F && action == GLFW_PRESS) {
                    showForces = !showForces;
                }
                if (key==GLFW_KEY_N && action == GLFW_PRESS) {
                    showNetForce = !showNetForce;
                }
                if (key==GLFW_KEY_V && action == GLFW_PRESS) {
                    showVelocity = !showVelocity;
                }
                if (key==GLFW_KEY_A && action == GLFW_PRESS) {
                    showAcceleration = !showAcceleration;
                }
                if (key==GLFW_KEY_E && action == GLFW_PRESS) {
                    showAll = !showAll;
                    showBoundingBox = showAll;
                    showNetForce = showAll;
                    showForces = showAll;
                    showVelocity = showAll;
                    showAcceleration = showAll;
                    showCollisions = showAll;
                }
            }
            if (key == GLFW_KEY_LEFT_ALT && action == GLFW_PRESS) {
                altKey = true;
            }
            if (key == GLFW_KEY_RIGHT_ALT && action == GLFW_PRESS) {
                altKey = true;
            }
            if (key == GLFW_KEY_LEFT_ALT && action == GLFW_RELEASE){
                altKey = false;
            }
            if (key == GLFW_KEY_RIGHT_ALT && action == GLFW_RELEASE){
                altKey = false;
            }
        });

        glfwSetMouseButtonCallback(window,(window, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                if(altKey){
                    controller.createEntityClick(getPointer(), cameraScale);
                }
                else {
                    mouse_lb_down = true;

                    Vector2D forceStart = getPointer();
                    mouse_lb_initialX = forceStart.getX();
                    mouse_lb_initialY = forceStart.getY();
                }
            }

            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
                if (mouse_lb_down && selected != null) {
                    Vector2D forceEnd = getPointer();
                    Vector2D position = new Vector2D(mouse_lb_initialX, mouse_lb_initialY);
                    Vector2D force = new Vector2D(forceEnd.getX() - mouse_lb_initialX, forceEnd.getY() - mouse_lb_initialY);
                    controller.createForce(selected, position, force);
                }
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

    private Vector2D getPointer() {
        double[] x = new double[1];
        double[] y = new double[1];
        int[] h = new int[1];
        int[] w = new int[1];
        glfwGetCursorPos(window,x,y);
        glfwGetWindowSize(window,h,w);
        double x_cursor = (x[0] - (h[0] / 2)) / (h[0] / 2);
        double y_cursor = ((w[0] / 2) - y[0])/ (w[0] / 2);

        x_cursor = (x_cursor / cameraScale) - (camera.getX());
        y_cursor = (y_cursor / cameraScale) - (camera.getY());
        return new Vector2D(x_cursor, y_cursor);
    }

}
