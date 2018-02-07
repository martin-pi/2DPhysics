
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




public class View implements ModelObserver{
    Model model;
    Controller c;
    long window;
    boolean hasChanged;

    public View(Controller c, Model model){
        this.model = model;
        this.c = c;
        System.out.println(model.getMsg());
        model.addObserver(this);
    }

    public void runTest() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void update(){
        hasChanged = true;
    }

    public void pullFrame(){
        System.out.println(model.getMsg());
    }

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
                glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
            }
            if ( key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
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
    private void loop() {
        //
        GL.createCapabilities();
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        while ( !glfwWindowShouldClose(window) ) {
            if (hasChanged) {
                pullFrame();
                hasChanged = false;
            }
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glBegin(GL_TRIANGLES);  //begin drawing
                glColor4f(0,0,0, 0);
                glVertex2f(-0.5f,0.5f);
                glVertex2f(0.5f,0.5f);
                glVertex2f(0.5f, -0.5f);
            glEnd();            //stop drawing

            glfwSwapBuffers(window);
        }
    }
}
