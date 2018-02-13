
package in.patrickmart.view;

import in.patrickmart.model.*;
import in.patrickmart.controller.*;

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
import org.lwjgl.opengl.GL;


public class View implements ModelObserver {
    private Model model;
    private Controller c;
    private long window;
    private boolean hasChanged;
    private Scenario scenario;

    public View(Controller c, Model model){
        this.model = model;
        this.c = c;
        model.addObserver(this);
    }

    /**
     * method to run the view initialization and loop.
     */
    public void runView() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * called by model to notify view of changes.
     */
    public void update(Scenario s){
        this.scenario = s;
    }

    /**
     * initializes the view components and sets up events.
     */
    private void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(700, 500, "Simulation View", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            //toggle the color with spacebar
            if ( key == GLFW_KEY_SPACE && action == GLFW_RELEASE ){
                glClearColor(0.8f, 1.0f, 0.8f, 0.0f);
            }
            if ( key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                glClearColor(1.0f, 0.8f, 0.8f, 0.0f);
                c.viewEvent();
            }
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
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

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(window);
    }

    /**
     * main drawing loop for the view
     */
    private void loop() {
        GL.createCapabilities();
        glClearColor(0.8f, 1.0f, 0.8f, 0.0f);

        while ( !glfwWindowShouldClose(window) ) {
            glfwPollEvents(); //checks for keyboard events

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            for (Entity e : scenario.getEntities()){
                //Draw this entity.
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

            glfwSwapBuffers(window);
        }
    }
}
