# 2D Physics Engine
Rick Martin and John Hewitt

## Downloading, Compiling, and Running
This project is compiled and tested with Apache Maven 3.5.2 and Java 1.8.0_151.
In order to clone, compile and run this project, enter the following commands in a terminal.
```
$ git clone https://github.com/Pharaoh439/capstone.git
$ cd engine
$ mvn compile exec:java -Dexec.mainClass=in.patrickmart.App
```
You may want to run our JUnit tests in order to confirm the functionality of our source code. The one liner to run all of the JUnit tests we have included is below.
```
$ mvn -Dtest=in.patrickmart.**.*Test test
```
If you are running an environment with no GUI, you may want to run the project headlessly. In this case, use the command line argument "headless" or just "h" to run the project without opening a viewing window. Usage of this argument is displayed below.
```
$ mvn compile exec:java -Dexec.mainClass=in.patrickmart.App -Dexec.args="-e"
```
## Running and Controlling a Simulation
|Input              |Action                                                   |Conditions                  |
|:------------------:|--------------------------------------------------------|----------------------------|
|`Esc`               |Close the application.                                  |                            |
|`Left Click`        |Select an entity. Selecting a blank space will deselect.|                            |
|`Left Click` + Drag |Apply a force to the selected entity.                   |An entity must be selected. |
|`Alt` + `Left Click`|Place an entity into the scenario.                      |                            |
|`Delete`            |Remove the selected entity, or reset the scenario.      |                            |
|`Right Click` + Drag|Move the camera around the scenario.                    |                            |
|`Arrow Keys`        |Move the camera around in steps.                        |                            |
|`Mouse Wheel Up`    |Zoom the camera in.                                     |                            |
|`Mouse Wheel Down`  |Zoom the camera out.                                    |                            |
|`L`                 |Launch an entity from the right side of the camera.     |                            |
|`B`                 |Toggle NBody-Like gravity simulation between entities.  |                            |
|`G`                 |Toggle Flat-Earth-Approximation gravity.                |                            |
|`V`                 |Create a static "floor" at the bottom of the camera.    |                            |
|`S`                 |Quickly select the most recently created entity.        |                            |

There are also many debug visualizations available.

|Input              |Action                                                           |
|:------------------:|----------------------------------------------------------------|
|`Alt` + `E`         |Toggles all debug visualizations on or off.                     |
|`Alt` + `B`         |Toggles visualization of entity bounding boxes.                 |
|`Alt` + `C`         |Toggles collision highlighting.|                                |
|`Alt` + `F`         |Toggles the drawing of individual forces acting on every entity.|
|`Alt` + `N`         |Toggles whether entities will draw the net force acting on them.|
|`Alt` + `A`         |Toggles the visualization of entities' acceleration vectors.    |
|`Alt` + `V`         |Toggles the visualization of entities' velocity vectors.        |


## Project Overview
This project seeks to create a 2 dimensional physics simulation consisting of n-dimensional rigid body polygons. This simulation could be useful to game developers as a way of representing their game’s objects in two dimensional space. The physics model should be able to run headlessly, without any visual representation of the events within. Because this isn’t immediately useful to most people, the project will also include a program to view and create simulations to see how our engine works. This can be used by people interested in using our library or anyone interested in learning how different objects will interact with each other and various forces that they will be able to control. The users that wish to learn from this program will need to be able to view and control the various aspects of the background physics such as information about the object as well as the forces acting upon it at any time. These users will probably also want to be able to control the simulation by having rewind, pause, and fast forward features as well as a detached camera capable of zooming in and out to change the user’s focus. 
#### A-Level Features
- [x] The program should be able to perform basic vector math operations such as dot and cross products.
- [x] The program should be able to represent shapes as collections of vectors.
- [x] The program should maintain an enum list of object materials such as concrete, iron, or ice.
- [x] The program should contain physics objects, which reference shapes and materials to give themselves a physical representation in their environment.
- [x] The program should have environments or scenarios, which hold collections of physics objects. These environments should be easily searchable in 2 dimensional space. This might be made easier with quadtrees.
- [ ] The program should be able to load and save environments to files, most likely json or csv files.
- [x] The program should be able to launch a window, which will draw the physics objects within a loaded environment.
- [ ] The program should be able to play, pause, or take individual “physics steps” on a loaded environment. Ideally the user should be able to control these actions through the rendering window.
- [x] The program should be able to define forces, which will act on physics objects. These forces should be part of the environment.
- [x] Physics objects should be able to create forces that act upon themselves or other physics objects in their environment.
- [x] Physics objects should be able to detect when they are intersecting with other physics objects. This collision detection should be done via a scan of the environment that the physics object is in.
- [x] Physics objects should be able to respond to collisions by generating reactionary forces such as normal forces.
- [x] When defining a physics environment, a user should be able to set a physics object’s properties, such as rotation-locked, non-colliding, or static objects.
- [ ] Physics objects should be able to signal other objects within their environment independently of the physics calculations that are happening. The program should allow for physics objects to display custom behavior upon receiving these signals, most likely via decorator or template method pattern.
- [ ] The user should be able to view an object’s properties and status from the viewing window.
- [x] The user should be able to control some small functions of objects via the viewing window, such as sending signals, or applying force.
#### B-Level Features
- [ ] The program should multithread certain actions such as collision checking/response
- [ ] Users should be able to create physics environments via a separate tool.
- [ ] Users should be able to create physics objects that could be loaded into simulations via a separate tool.
- [ ] Physics objects should be able to ray cast, allowing for lighting simulation as well as more potential features for game developers.
- [ ] Vector calculations should be done via GPU Acceleration. This will involve an overhaul of the vector math library.
- [ ] Physics objects should be optionally soft-body objects.
#### C-Level Features
- [ ] Users should be able to define objects and environments, and run those environments, from the same window.
- [ ] Environments should be able to define fluid sources and drains, which will generate simulated fluids.
- [ ] Environments should be able to simulate air or fluid currents
- [ ] Physics objects should be able to attach to one another via constraints and cables.

## Similar Existing Projects
There are a lot of C, C++ and javascript physics engines but most of the java engines are simply ports of other libraries. One such example is JBox2D, a port of a C++ engine called Box2D. Other similar projects such as Java 2D Physics have no plan for expanding upon their engine and are limited in capabilities. 

Dyn4j does everything we are planning to do but it has problems with scalability and hardware usage since it uses awt while we are planning on using OpenGL. Using OpenGL allows us to utilize the gpu to provide better graphical performance and gives us the ability to scale our framework to handle 3 dimensional physics without having to change the structure of our framework.

One of the issues we noticed with Box2D is the limitation on object sizes. They suggest you keep your models between 0.1 and 10 meters to keep large physics calculations from causing graphical performance issues. We believe that we can allow for more variation in size due to our use of OpenGL which will put the graphical computation load on the gpu leaving room on the cpu for larger and more complex computations. 

We also incorporate different material makeups for our objects and simulate the effect those materials will have on the various physics involved with collisions. None of the physics engines we have looked into so far have this capability especially when looking at the small number of java physics engines.

There are plenty of other engines listed below that are implemented in other languages, but most of the java implementations are surprisingly limited. It is also helpful for us specifically to use java due to its object-oriented nature as well as our familiarity with it.

* **Java**
	* [Dyn4j](http://www.dyn4j.org/about/)
	* [JBox2D](http://www.jbox2d.org/)
	* [Java 2D Physics](https://github.com/wilkystyle/java2dphysicsengine)
* **C, C++**
	* [Box2D](http://www.box2d.org/)
        
## Technology
* **Java** We will be writing everything for our project in Java with the exception of the xml file needed for Maven. 
* **[Maven](https://maven.apache.org/)** We plan on using maven to track our program’s dependencies and make compiling and testing easier. Maven works well alone, and works well with our planned development tools such as JUnit.
* **[IntelliJ](https://www.jetbrains.com/idea/)** IntelliJ has the capacity to check for style violations and lint during the programming process, which makes it a valuable tool to keep our code in line from the start.
* **[GLFW, OpenGL](http://www.glfw.org/)** In order to render our simulations, we will need a window to draw to. GLFW is a wrapper for OpenGL and a part of the larger Lightweight Java Game Library. As our project focuses more on the optimization of our physics than the optimization of our renderings, we will rely heavily on the work that has already been done for us in this library.
* **[JUnit](http://junit.org/junit5/)** In order to quickly test our program, we will be using automated unit tests created with JUnit that are run automatically by Maven. 

## Previous Experience 
#### Rick
I have some prior experience with game engines in general, as well as experience implementing programs structured around the Microsoft XNA Standard. More particularly, I have built a small number of animations and [games](http://patrickmart.in/projects/clicker/) which involve physics-like calculation using a javascript animation library called [P5](https://p5js.org/). I have also done a little bit of work with LWJGL, although not enough that I would say I know how to use it.
#### John
I have previous experience with the majority of the physics we will be implementing as well as applying some of the same physics to other projects. For example, I’m creating a python implementation of Pac Man using the [kivy](https://kivy.org/#home) framework which involves collision  checking and the creation of shapes from procedurally generated coordinates. I also worked over the summer creating user interfaces in javascript which can be applied  to one of our stretch goals involving the creation of a ‘playground’ where users can create objects and see how they interact using our engine.

## Risk Areas
Neither of us feel comfortable with our current knowledge of LWJGL, which we plan on using to render simulations. More specifically we are using GLFW to create our window as well as handle user input and OpenGL to draw our objects within that window. In order to gain more experience with these two libraries, besides just reading their documentation, we will build a few basic programs separate from our engine following the getting started tutorials provided by OpenGL and GLFW. These tutorials should cover everything we’ll need for this project since our graphics and user interface should be fairly minimal. 

We also will have to learn how to use Maven to handle the compilation, testing and running of our project. Maven’s getting started page provides us with all of the basic knowledge needed to create our project, compile it, run our JUnit tests and handle our dependencies. In case we run into issues while using Maven their is an archive of user questions found [here](https://www.mail-archive.com/users@maven.apache.org/) that will be able to help us with common issues encountered while using Maven.

JUnit testing is another area we don’t yet feel comfortable with and will have to learn so we can begin automated testing of the various features in our library. Maven’s getting started page will help us understand how to run the tests automatically but we’ll have to learn how to write our tests so that they work with Maven as well as thoroughly test our features. I have found a [tutorial](http://www.vogella.com/tutorials/JUnit/article.html#junittesting) that we will use that explains how to write JUnit tests that also contains information on proper naming conventions for use with Maven.

Another risk we are taking involves the size of our project and the need to complete the majority of our features in order to have a complete project to showcase at the end of the semester. This will require that we complete the various components of our project as scheduled or at least be able to work around unfinished features while maintaining the usability of our project. To reduce the impact of this risk we will always make sure that we prioritize essential features of the project that allow us to have some cohesive and functional product by the end even while missing unfinished features. 
## Preliminary UML
![A giant, unfinished, and confusing UML diagram](http://patrickmart.in/media/capstone/preliminaryuml.png)
## Other Questions and Answers
* ***I'd like to hear where you got this idea. Why did you choose it?***
I have built a few small [projects](http://patrickmart.in/projects/) with half-implemented and wholly inaccurate physics features. I wanted to build something more accurate and valuable than those simpler projects.
* ***Are you also a Physics major?***
Neither of us are physics majors, which may make things much more difficult for us.
* ***How did you hear about LWJGL?***
I learned about LWJGL when attempting to port something I had made in javascript to a portable java app last year. I have wanted to work with the library since then, and i am happy to finally have the chance.
* ***Why did you choose Java?***
We chose Java due to our familiarity with it, and the many java-specific tools and libraries available to us when working on a project of this type. There are also some benefits to the language itself in our case, such as its object-orientedness.
* ***Which tool did you use to create your UML Diagram?***
[draw.io](http://draw.io)
* ***Do you have prior experience building simulation systems like this?***
We have built a few simulations, but nothing on this scale or complexity.
* ***What do you want for the User Interface to look like? Could you make a crude sketch or mock-up for me?***
![A poorly photoshopped screenshot](http://patrickmart.in/media/capstone/preliminaryscreenshot.png)
* ***How will you define a scenario without editing tools? Will they be text files? Or just hard-coded into your minimum viable product?***
Ideally, we would like to be able to define scenarios as json files. We would then enable the program to accept those files as input or generate them as output.
* ***If you're concerned about performance and acceleration of the computation, have you considered writing this in a multithreaded way?***
I have considered multithreading, and there are a few small places in the project where I believe it would be very beneficial to multithread.
* ***How do you plan to test this project (automated testing, not manual)?***
JUnit testing will do the majority of the work. If we need to, we may also make some "test scenarios" for JUnit to run and determine the validity of.
