package in.patrickmart.view;

import in.patrickmart.controller.Controller;
import in.patrickmart.model.Entity;
import in.patrickmart.model.*;
import in.patrickmart.model.Scenario;

import in.patrickmart.model.Vector2D;
import in.patrickmart.model.forces.Force;
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
        cameraScale = 0.125;

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

        // Determine the new camera location.
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(window,x,y);
        double mouse_x_prev = mouse_x;
        double mouse_y_prev = mouse_y;
        Force clickForce = null;
        mouse_x = x[0] / cameraScale;
        mouse_y = y[0] / cameraScale;
        if (mouse_rb_down) {
            camera.add(new Vector2D((mouse_x - mouse_x_prev) / 640, (mouse_y_prev - mouse_y) / 400));
        }

        if (selected != null && mouse_lb_down) {
            Vector2D forceEnd = getPointer();
            Vector2D position = new Vector2D(mouse_lb_initialX, mouse_lb_initialY);
            //System.out.println("Initial Pos: " + mouse_lb_initialX + ", " + mouse_lb_initialY);
            Vector2D force = new Vector2D(forceEnd.getX(), forceEnd.getY()).sub(position).mult(200);
            System.out.println("Click&Drag: Applying a force to the selected Entity.");

            //Vector2D position = selected.getPosition().add(new Vector2D(0, 10));
            //Vector2D force = selected.getPosition().add(new Vector2D(1000, 0));
            clickForce = controller.createForce(selected, position, force);
        }
        // Set the clear or "background" color.
        glClearColor(0.92f, 0.92f, 0.92f, 0.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Draw a grid to give the user a better sense of scale.
        glColor4d(0.6,0.6,0.9, 1);
        for (int i = 0; i < 11; i++) {
            glBegin(GL_LINES);
            glVertex2d(((i - 5) + camera.getX()) * cameraScale, ((-5) + camera.getY()) * cameraScale);
            glVertex2d(((i - 5) + camera.getX()) * cameraScale, ((5) + camera.getY()) * cameraScale);
            glEnd();
            glBegin(GL_LINES);
            glVertex2d(((-5) + camera.getX()) * cameraScale, ((i - 5) + camera.getY()) * cameraScale);
            glVertex2d(((5) + camera.getX()) * cameraScale, ((i - 5) + camera.getY()) * cameraScale);
            glEnd();
        }

        // Draw all entities in the model.
        for (Entity e : s.getEntities()) {
            //Figure out what color this entity is.
            if (e.isColliding() && showCollisions) {
                glColor4d(collisionColor[0],collisionColor[1],collisionColor[2],collisionColor[3]);
            } else {
                glColor4d(e.getColor()[0],e.getColor()[1], e.getColor()[2], e.getColor()[3]);
            }
            if (selected != null && e.equals(selected)) {
                glColor4d(0.4,0.8,0.4, 1);
            }

            // Draw this entity.
            if (showAll) {
                glBegin(GL_LINE_LOOP);
            } else {
                glBegin(GL_TRIANGLES);
            }
            for (int i = 0; i < e.getShape().getPoints().size(); i++) {
                // Draw triangles between the center of mass and the points making up the model.
                Vector2D v = e.getShape().getPoints().get(i);
                Vector2D w = e.getShape().getPoints().get((i + 1) % e.getShape().getPoints().size());

                glVertex2d((e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
                glVertex2d((v.getX() * cameraScale) + (e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (v.getY() * cameraScale) + (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
                glVertex2d((w.getX() * cameraScale) + (e.getPosition().getX() * cameraScale) + (camera.getX() * cameraScale), (w.getY() * cameraScale) + (e.getPosition().getY() * cameraScale) + (camera.getY() * cameraScale));
            }
            glEnd();

            // Draw the net force acting on this Entity.
            if(showNetForce) {
                if(e.getNetForce() != null) {
                    glBegin(GL_LINES);
                    glColor4d(0, 0, 0, 0);
                    glVertex3d((e.getPosition().getX() + camera.getX()) * cameraScale, (e.getPosition().getY() + camera.getY()) * cameraScale,1);
                    glVertex3d((e.getPosition().getX() + (e.getNetForce().normalize().getX() * e.getShape().getDiameter()/2) * 1.5 + camera.getX()) * cameraScale, ((e.getNetForce().normalize().getY()*e.getShape().getDiameter()/2) * 1.5 + e.getPosition().getY() + camera.getY()) * cameraScale,1);
                    glEnd();
                }
            }
            // Draw each individual force acting upon this entity.
            if(showForces){
                if (e.getForces() != null) {
                    {
                        for (Force f : e.getForces()) {
                            if(clickForce == null || !f.getPosition().equals(clickForce.getPosition())) {
                                glBegin(GL_LINES);
                                glDepthFunc(GL_NEVER);
                                glColor4d(0.4, 0.4, 0.4, 0);
                                glVertex2d((e.getPosition().getX() + camera.getX()) * cameraScale, (e.getPosition().getY() + camera.getY()) * cameraScale);
                                glVertex2d((e.getPosition().getX() + (f.getForce().normalize().getX()* e.getShape().getDiameter()/2) + camera.getX()) * cameraScale, (e.getPosition().getY() + (f.getForce().normalize().getY()*e.getShape().getDiameter()/2) + camera.getY()) * cameraScale);
                                glEnd();
                            }
                        }
                    }
                }
            }
            if(mouse_lb_down){

            }
            // Draw this Entity's velocity.
            if(showVelocity) {
                glBegin(GL_LINES);
                glColor4d(1, 0.1, 0.1, 0);
                glVertex2d((e.getPosition().getX() + camera.getX()) * cameraScale, (e.getPosition().getY() + camera.getY()) * cameraScale);
                glVertex2d((e.getPosition().getX() + (e.getVelocity().normalize().getX()*e.getShape().getDiameter()) + camera.getX()) * cameraScale, ( (e.getVelocity().normalize().getY()*e.getShape().getDiameter()) + e.getPosition().getY() + camera.getY()) * cameraScale);
                glEnd();
            }
            // Draw this Entity's acceleration.
            if(showAcceleration) {
                glBegin(GL_LINES);
                glColor4d(.6, 0, .9, 0);
                glVertex2d((e.getPosition().getX() + camera.getX()) * cameraScale, (e.getPosition().getY() + camera.getY()) * cameraScale);
                glVertex2d((e.getPosition().getX() + (e.getAcceleration().normalize().getX()*e.getShape().getDiameter()/2) * 1.75 + camera.getX()) * cameraScale, ( (e.getAcceleration().normalize().getY() * e.getShape().getDiameter()/2) * 1.75 + e.getPosition().getY() + camera.getY()) * cameraScale);
                glEnd();
            }
            // Draw the bounding box if bounding box debugging is enabled.
            if(showBoundingBox) {
                AABB b = e.getBounds();
                glBegin(GL_LINE_LOOP);
                glColor4d(0.3, 0.3, 1, .0003);
                glVertex2d((e.getPosition().getX() + b.getHalfWidth() + camera.getX()) * cameraScale, (e.getPosition().getY() + b.getHalfHeight() + camera.getY()) * cameraScale);
                glVertex2d((e.getPosition().getX() + b.getHalfWidth() + camera.getX()) * cameraScale, (e.getPosition().getY() - b.getHalfHeight() + camera.getY()) * cameraScale);
                glVertex2d((e.getPosition().getX() - b.getHalfWidth() + camera.getX()) * cameraScale, (e.getPosition().getY() - b.getHalfHeight() + camera.getY()) * cameraScale);
                glVertex2d((e.getPosition().getX() - b.getHalfWidth() + camera.getX()) * cameraScale, (e.getPosition().getY() + b.getHalfHeight() + camera.getY()) * cameraScale);
                glEnd();
            }
            if(clickForce != null) {
                glBegin(GL_LINES);
                glDepthFunc(GL_NEVER);
                glColor4d(0.4, 0.4, 0.4, 0);
                glVertex2d((clickForce.getPosition().getX() + camera.getX()) * cameraScale, (clickForce.getPosition().getY() + camera.getY()) * cameraScale);
                glVertex2d((clickForce.getPosition().getX() + clickForce.getForce().getX() / 200 + camera.getX()) * cameraScale, (clickForce.getPosition().getY() + clickForce.getForce().getY() / 200 + camera.getY()) * cameraScale);
                glEnd();
            }
        }

        // Draw overlays such as the mouse location.
        if (showAll) {
            glBegin(GL_LINE_LOOP);
            glColor4d(0.3, 0.3, 1, .0003);
            for (int i = 0; i < 20; i++) {
                double rotation = i * (Math.PI / 10);
                Vector2D point = new Vector2D(0.01, 0).rotate(rotation);
                Vector2D position = getPointer();
                glVertex2d(((position.getX() + camera.getX()) * cameraScale) + point.getX(), ((position.getY() + camera.getY()) * cameraScale) + point.getY());
            }
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
            if(!altKey) {
                if ((key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) || glfwWindowShouldClose(window)) {
                    closeWindow();
                }
                if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                    controller.viewEvent();
                }
                if (key == GLFW_KEY_G && action == GLFW_PRESS) {
                    controller.toggleFEA();
                }
                if (key == GLFW_KEY_B && action == GLFW_PRESS) {
                    controller.toggleGravity();
                }
                if (key == GLFW_KEY_V && action == GLFW_PRESS) {
                    controller.createGround(cameraScale);
                }
                if (key == GLFW_KEY_L && action == GLFW_PRESS) {
                    controller.launchBall(cameraScale);
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
                if (key == GLFW_KEY_DELETE && action == GLFW_PRESS) {
                    if(selected == null) {
                        controller.clearEntities();
                    }
                    else{
                        controller.destroyEntity(selected);
                    }
                    selected = null;
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
                Vector2D currentPos = getPointer();
                Vector2D originalPos = new Vector2D(mouse_lb_initialX, mouse_lb_initialY);
                if (currentPos.equals(originalPos)) {       // User has clicked and released the mouse.
                    System.out.println("Left Click: Attempting to select an Entity.");
                    Entity found = controller.selectAtPosition(currentPos);
                    if (found != null) {
                        selected = found;
                        System.out.println("\tFound Entity #" + selected.getId() + ".");
                    } else {
                        System.out.println("Attempt failed, deselecting currently selected Entity.");
                        selected = null;
                    }
                }
                mouse_lb_down = false;
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
