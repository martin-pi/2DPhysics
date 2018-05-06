# Physics Simulator Technical Report
John Hewitt, Patrick Martin, 5/7/2018

## Abstract
Our project aims to create a 2D Java Physics engine that is capable of outperforming JBox2D in terms of object scale and ease of use. We aim to create a framework that can be easily adapted by others for their own needs. These needs may include games, or specialized mechanical simulations. We utilize the MVC pattern so that users are able to attach their own controller and view to our model. This also allows the model to run independently of any viewer. We have included our own Vector2D library. This means that our model is self contained; It requires no dependencies or external libraries to run. This makes it extremely easy to use our model in one’s own project. Future versions of the Vector2D library may also allow for GPU Acceleration, significantly increasing the speed of simulations.

We have not yet been able to implement as wide a range of physics features as we would like, but we have a framework in place that can be easily expanded upon by us and our users in the future. The physics engine in its current state is able to manage an environment full of physics objects and efficiently apply various forces such as gravity, flat earth approximation gravity, and collision forces. The model also allows for the application of generic forces, which allow users to create their own customized forces. We also have the components in place to expand the models capabilities such as materials and the ability to attach multiple views to the same model. 

**Tags:** `Physics`, `Mechanics`, `Vector`, `Linear Algebra`, `Forces`, `Collision Detection`, `Collision Response`
## Table of Contents
* [Abstract](#abstract)
* [Table Of Contents](#table-of-contents)
* [Project Overview](#project-overview)
	* [Background Information](#background-information)
	* [Objectives](#objectives)
	* [Users](#users)
	* [Features](#features)
* [Project Development](#project-development)
	* [Design](#design)
	* [Development](#development)
	* [Testing](#testing)
* [Results](#results)
	* [Accuracy of Estimates](#accuracy-of-estimates)
	* [Image Examples](#image-examples)
* [Conclusions and Future Work](#conclusions-and-future-work)
* [References](#references)


## Project Overview
This project seeks to create a two-dimensional physics simulation consisting of n-dimensional rigid body polygons that are able to be represented at a smaller and larger scale than JBox2D. Most other java based physics simulations are simply ports of engines written in other languages. JBox2D for instance is just a java port of the popular Box2D physics game engine written in C++. Most other physics implementations define themselves as engines, meant to be used and not expanded. This sentiment, combined with the limitations that these projects have, left us wanting something more open. These engines are also not the easiest thing to use and can seem daunting to new users, so we decided to focus on usability and simplicity rather than focusing on implementing every individual aspect of the physics mechanics. We wanted to focus on creating a framework that would allow those aspects to be added in the future along with anything else users wished. While initially slowing our development, this allowed us to implement concepts more easily as the project progressed, and will allow for more expansion in the future.

### Background information 
* **Vector**: a line in a graphical space pointing to a position starting from the origin that has a magnitude (the length) and direction.
* **Forces**: something that acts upon an object causing it to change its velocity. 
* **Shape**: a visual representation of our physics objects made up of vectors pointing from its center to each corner of the shape. 
* **Entity**: a physics object which is acted upon by forces and represented by a shape. 
* **Entity-Component System**: System in which an entity is formed from multiple sub-entities.
* **Scenario**: our representation of an environment in which the entities live. 
* **Collision Detection**: The detection of overlapping shapes in the scenario.
* **Collision Response**: The resulting reactions and forces created after a collision involving two entities. 
* **MVC Pattern**: a framework that divides the responsibilities of an application into three parts. The model represents the application core like a database. The view displays the data held in the model and updates itself whenever the model changes and sends user input to the controller for processing. The controller enacts changes in the model based on the user input received.



### Objectives
As a base goal we wanted to create a physics engine that solved the issue of usability as described above, but was still able to perform basic physics simulations. This meant we needed to efficiently represent our physics objects in a straightforward way so that anyone can draw the objects with whatever graphics library they choose. We wanted to make sure that we implemented various forces and the ability for users to easily create and apply various custom forces. Collisions detection and response was another major goal we wanted to implement since you can’t really make a game without having some interaction between objects. 
Our goal of making a simulation that can be easily expanded upon drove us toward a focus on useful design patterns. Because the model was developed along with a controller and viewer as a part of an MVC application, there is an innate separation of responsibilities. This separation helps keep the model independent from the features that some users might consider irrelevant. Other patterns, such as an entity-component system, allow for the quick addition of different types of entities comprised of various sub-entities, or in our case shapes. Additionally, we wanted to implement physics calculations in a way that is accurate and recognizable to someone with some base level knowledge of mechanical physics. The MVC pattern also helps keep the model transparent to users meaning that any information about the entity is readily available for their use. 

### Users
Our potential user base consists mostly of early physics students and amateur game developers. These are people who might benefit from a small, yet easily expandable simulation.
There are plenty of other physics engines that are implemented in other languages, but most of the java implementations are surprisingly limited. Their code bases are also rather large and unapproachable to those who might want to use, modify, or expand them. We also intend for this to be a teaching tool to those searching for a basic physics simulation that can help them understand and visualize the impact different forces have on objects in a controlled space. 

### Features
The project exists as an MVC application, with all of the physics calculations contained within the model. The model is the main concern of the project, but in an effort to ensure approachability, we have also included an exemplary viewer and controller for running simulations.The program currently has the capacity to perform vector mathematics calculations, and it builds on that capacity to implement physics concepts. Entities with physical properties and representations can be simulated and even interact with one another, or not in the case of static entities. We also implemented the ability for users to create their own generic forces to represent artificial forces that might be used in physics problems. Our provided view/controller has various debug tools that help explain how we draw our shapes and how we represent our forces, velocities, and accelerations in the model. It also comes loaded with controls that can be used to demo the implemented features such as a gravity toggle and a create 'floor' option to contain entities within the current screen. 

## Project Development
### Design
To develop our project, we started out with an outline of the general design. While this began as sketches and ethereal concepts, we quickly decided that we needed a UML Diagram to work from. This UML diagram became our main reference when designing, and a great tool when developing. As we built the application, the diagram grew and changed, simple things being replaced with more complex ones, and unused concepts being removed to make things more coherent. It was important to us that the design remained adjustable throughout the project, as many problems that we encountered required revision. Draw.io was an invaluable tool for this, allowing not only revision, but collaboration on the design.
![Gravity](http://patrickmart.in/media/capstone/ecs2.png)
We started the project by laying out the source code, determining where things belonged. We took influences from IntelliJ's default project structure, as well as the Maven standard directory layout [2].
Eventually we ended up with a design layout detailing the current and future state of the project.

### Development
We developed the program using scrum, an agile framework. We chose a few features, and attempted to have them completed within our two week sprints. This worked well, although as we encountered roadblocks and delays, we ended up carrying a few features such as collision detection and collision response over through multiple sprints. These features required a lot of research and development, and we wanted to take the time to get them right.
Once we had designed the structure of the program, we started implementing the classes we had outlined. The program was divided into three sections, the model, view, and controller. The model and controller were mostly finished within the first couple of iterations of development, but the model had to be built slowly, over many agile iterations.

**Model**
* **Vector 2D**: The vector library is the base component of pretty much every other component of our project. It handles the representation of vectors as well as all the operations needed to perform higher level operations. Vectors are represented as x and y coordinates stored in doubles. These are then used in various operations such as dot product, cross product, multiplication, and divisions. The vector library also contains several vector level helper methods used to simplify more complex operations such as getting a perpendicular vector, rotating by a given angle, getting the angle between vectors, and getting a normal vector.
* **Shape**: Shape is the next step up from Vector2D that uses a list of vectors to represent points that are offsets from the center position of the shape which is also represented as a vector. For example, see attached image. Shape also contains methods to assist with higher level operations such as rotating the object which involves changing the position of each point and project which projects the shape onto an axis and returns the minimum and maximum points of that projection. Shape also handles operations involved in collision detection such as calculating the bounding box for rough collisions and intersectsShape which determines if a given shape is overlapping and returns a minimum translation vector, or mtv, which is used to move the shapes to a point where they are no longer overlapping. The mtv is split in half for each entity in the collision and moves both entities the same amount in opposite directions. 
* **Entity**: Entities are the highest level representation of objects in our model. Entities are visually represented by shapes and have a mass, material makeup, position (shape’s position is always the same), and a bounding box. The entity changes its position based on its previous velocity and its new acceleration due to forces from its list of current forces being applied to the entity that step. Entities can also be static meaning they don’t react to any applied forces but will still apply collision forces to the other entity. Static Entities are the only entities that can overlap each other and since static entities never move they cause any other non-static entities to move the full length of the mtv. 
* **Material**: Material is an enum containing all the properties associated with each material. These properties include density, color (to visually distinguish between materials), and buoyancy. Currently we aren’t using materials when creating entities since none of the implemented features require density or buoyancy in their calculations, but they are in place now as an example of their use in later implementations. 
* **AABB**: AABB is a bounding box created around each entity used in our rough collision checking to determine if we need to perform a more intensive collision detection.  It also determines if a point passed is contained within the box in order to determine which entity is selected by a click. AABB has a Vector2D position vector pointing to its center and a half width and half height used in our collision algorithm. AABB’s center should always be the same position as the shape and entity center. 
* **Scenario**: Scenario is essentially the environment being represented at the time. It contains a list of entities currently existing, a list of selected entities, booleans for each environmental force such as gravity to determine when to apply them, and a list of unique collisions that is changed each step to reflect current collisions. The scenario iterates through its list of collisions and resolves them. 
* **Collision Data**: Collision Data is responsible for holding information about which entities are involved in the collision and determining how to resolve the collision based on the properties of each entity. Currently when two entities are colliding we calculate their velocities after the collision and apply an impulse force to each entity based on their initial momentum and the time it takes to resolve the collision. Also, if gravity or FEAgravity are turned on then they apply normal forces to each other as well. 
* **Model**: The model class is responsible for keeping track of the scenario and its entities as well as updating the view or any other observers of changes to anything contained within the model. 

**Controller**
The controller is responsible for controlling the main loop of our program and making sure each step doesn’t exceed 60 fps so that our calculations can factor in the change in time between steps. If the fps drops below 60 our timing will be slower since we don’t get the actual elapsed time since the last step yet.     However, we have only observed noticeable frame rate drop when exceeding 300 entities so the effects from this shouldn’t be noticeable. 
During each step in the loop the controller calls the step function in the model which calls the scenarios step function and updates its observers after that step has finished. The step function in scenario applies each type of gravity to every entity if they are toggled on and calls each entities calculate method for acceleration, velocity, and position as well as its step method. 
Controller also is in charge of processing the user input sent to it from the view. Currently we have toggles for debugging views and  two types of gravity as well as several entity creation events. The entity creation events create new entity objects and add them to the model which adds them to the current scenario. Other actions include a create ground event which adds static entities in a cup like configuration so that you can contain your objects, a select event which determines if the clicked position is an entity then selects that entity, a create force event that applies a force to the entity based on the click and drag from the user, and several other similar events listed below. 

**View**
The view we have currently is just intended to demonstrate how the model and controller work. It is an observer of the model that is updated every time the model changes which happens every step. The entity takes the shape object and draws the shape representation from the provided position and point vectors. It does this by iterating through the list of points and drawing a triangle from the position vector to the current and next point around the shape until it reaches the first point again. The view also draws different debugging views depending on the various toggles that were listed above in the controller section.

* showCollisions changes the color of the entities to red when they are colliding.
* showNetForce draws a line representing the net force vector.
* showForces draws each individual force vector.
* showVelocity draws the velocity vector.
* showAcceleration draws the acceleration vector.
* showBoundingBox draws the AABB box.
* showAll obviously shows all. 

If a click and drag force is being applied currently it draws a line between the origin position and the mouse cursor. 
The view is currently the only component of the program with any dependencies aside from JUnit. We wanted to be able to render our model quickly and efficiently in our viewing program, so we chose to use OpenGL [4] to do so. OpenGL is not written in Java, however there are some options for interacting with OpenGL from within a Java application. We chose to go with GLFW [3], a window manager and OpenGL wrapper, distributed as part of LWJGL [5]. We also required more of the libraries offered by LWJGL, as we may expand the viewer to implement more of the potential features made possible by them. One important feature that we plan to implement is sound.

### Testing
For the most part our testing approach was simple. We decided to use JUnit [6] out of familiarity, although we had never done any test driven development before. We created J-unit tests for each mathematical function in our Vector library and entity that simply made sure the answers we got back from each operation matched the expected answer. We implemented more tests as we progressed, orienting these tests less toward specific parts of our code, and more towards testing specific features that needed to be implemented. Testing the controller was much more difficult. We had to keep track of the time between ticks and send the number of ticks between each step to ensure that the controller was running at 60 steps per second. 

## Results
We are pretty happy with the results of our efforts so far. While the full application is not yet fully functional, the model is at a point where further expansion has been made simple. The functions that have been implemented are implemented robustly, and have been tested.
* The program is able to perform basic vector math operations such as dot and cross products. This feature is entirely implemented and fully tested. The library is surprisingly versatile, and allowed us to build the rest of the model out of vectors.
* The program can represent shapes as collections of vectors. Shapes are implemented and drawn as triangle fans. Because the shapes are also representations of physics objects with volume, shapes automatically generate a center of gravity when they are created, and they shift their positions to center around that center of gravity.
* The program maintains an enum list of object materials such as concrete, iron, and ice.
* The program has physics objects, which reference shapes and materials to give themselves a physical representation in their environment. This is part of a pattern referred to as an entity component system.
* The program has environments or scenarios, which hold collections of physics objects. 
* The program launches a window when started, which will draw the physics objects within a loaded environment. This window also allows the user to provide input to the model, allowing for commands such as creating new physics objects.
* The program can define forces, which will act on physics objects. These forces are processed through the environment, allowing forces to be passed to any number of physics objects, or all of them.
* Physics objects can create forces that act upon themselves or other physics objects in their environment. This enables objects to gravitationally attract each other.
* Physics objects are able to detect when they are intersecting with other physics objects. This collision check is done in two steps, a rough check using axis-aligned bounding box collisions, and a fine check using the shapes of each object.
* Physics are able to respond to collisions by generating reactionary forces such as normal forces. This is currently done as an approximation due to time constraints, but is close to being accurate.
* When defining creating a new physics object, the object can be decorated with features such as being static. We plan on implementing more of these decorators, such as rotation-only objects, or non-rotating objects.
* The user should be able to control some small functions of objects via the viewing window, such as or applying forces. This can be done by clicking and dragging the mouse, to make things more simple.

### Accuracy of Estimates
Our early estimates were roughly accurate. We ended up only missing a few of our basic, required features. Most of these features became infeasible due to our requirement of integrating them into the viewing window. This was mainly due to restrictions resulting from using OpenGL to render the viewing window. As powerful and efficient as OpenGL is, it also requires a lot of setup to get started. It takes a lot of extra time to enable functionality that many other java based drawing libraries have available by default.
* The program should be able to load and save environments to files, most likely json or csv files.
* The program should be able to play, pause, or take individual “physics steps” on a loaded environment. Ideally the user should be able to control these actions through the rendering window.
* Physics objects should be able to signal other objects within their environment independently of the physics calculations that are happening. The program should allow for physics objects to display custom behavior upon receiving these signals, most likely via decorator or template method pattern.
* The user should be able to view an object’s properties and status from the viewing window.

### Image Examples
Our viewing window is simple, partially to make it approachable, and partially to keep the overhead from GLFW manageable.
![Some entities](http://patrickmart.in/media/capstone/Screenshot_5.png)
OpenGL allows us to render our entities in different ways. This is a wireframe view that draws many of the vectors that are present in the simulation, from forces and accelerations to the points that make up entity shapes.
![Debug view](http://patrickmart.in/media/capstone/Screenshot_4.png)
As a demonstration of the potential applications for our model, we have implemented a "Flat Earth Approximation" mode, which simulates an environment that might be present in a side-scroller, or similar game.
![Gravity](http://patrickmart.in/media/capstone/Screenshot_6.png)

## Conclusions and Future Work
We originally set out with the goal to create a better physics engine that could easily be used by others for various needs. We wanted to detach our controller and view from the model to give those users the flexibility to use our model in any way they like. We also wanted to beat the limited scale that JBox2D had. Our approach to meeting these two goals was to utilize the MVC pattern so that the view and controller could be exchanged for someone’s own implementations utilizing our model. The scaling issue just relied upon us creating an efficient representation of entities. For this we used vector representations for everything from shapes to forces to simplify as much as possible. We were able to establish the MVC pattern so that we could accomplish the flexibility goal and the structure of our model allowed us to meet the second goal by letting users work on  larger and smaller scale than is currently possible in other JBox2D. We are lacking in accuracy currently and have limited features but we have implemented enough for users to be able to create games using our model and adapting their own view and controller. However, this lack of accuracy means that our physics engine isn’t as useful of a teaching tool as we initially intended. Basic behaviors and general understanding can still be gleaned from our simulation making a viable option to teach a base level of understanding. 

For future work, we plan on improving the efficiency of our simulation by utilizing GPU acceleration in our Vector2D class and finishing our implementation of quadtrees and b-trees. The quadtrees will be used to sort our entities by proximity so we don’t have to check every entity against every other entity each and every step which is causing us to slow down when creating hundreds of entities. B-trees will help us search for entities by ID so we don’t have to search through our entire entity list when users request a specific entity after deletions have been made. Besides efficiency, we also wish to focus on implement more advanced features and improving the ones we currently have. Collision response needs to be fixed so that it can properly apply collision forces for non pseudo spherical objects. We also want to add the ability to create complex shape entities utilizing the entity-component system we have in place. Various other features such as friction, drag, and cables/pulleys are also future features that can be implemented with the framework we have in place. Friction and drag can be implemented by using/expanding the materials enum we created. We also wanted to implement entities that utilize the decorator pattern to dynamically create entities that are static, move along tracks, or any other specific behavior required by the user. 


## References
[1] [JBox2D](http://www.jbox2d.org/): A prominent alternative Physics Engine implemented in Java.

[2] [Maven Standard Directory Layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html): Our project structure is based off of these helpful guidelines.

[3] [GLFW](http://www.glfw.org/): A java wrapper for openGL, which we use to manage our rendering window.

[4] [OpenGL](https://www.opengl.org/): A powerful renderer, which we use to render our simulations.

[5] [LWJGL](https://www.lwjgl.org/): A collection of useful libraries for games and simulation applications

[6] [JUnit](https://junit.org/junit5/): An easy to use testing library.

[7] [IntelliJ](https://www.jetbrains.com/idea/documentation/): A complex IDE which we used to manage everything from compilation and testing, to github commits.

[8] [Draw.io](https://www.draw.io/): A web application which enables users to collaboratively draw and diagram.
