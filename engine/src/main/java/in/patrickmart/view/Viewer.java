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

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Viewer implements Observer {
    private Model model;
    private Controller controller;
    private long window; // Handle for GLFW window

    public Viewer(Controller c, Model m) {
        this.controller = c;
        this.model = m;

        openWindow();

        m.addObserver(this);
    }

    public void openWindow() {
        System.out.println(Version.getVersion()); //TODO remove.

        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() ) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(300, 300, "Window Title", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, modes) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                closeWindow();
                controller.stop();
                System.out.println("Stopped the model, closed the Viewing window.");
            }
            if ( key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                glClearColor(1.0f, 0.8f, 0.8f, 0.0f);
                controller.viewEvent();
                System.out.println("Added an entity to the model.");
            }
        });

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    public void update(Scenario s) {
        System.out.println("View:  Updated.");

        GL.createCapabilities();

        // Set the clear color
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

        glfwSwapBuffers(window);

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    public void closeWindow() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
