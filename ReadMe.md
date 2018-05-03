# 2D Physics Simulation
Rick Martin and John Hewitt

This is a 2 Dimensional physics simulation model, which runs in java. The physics model has no dependencies, and is completely self contained. There is a focus in the design of the model on making it easy to expand. While it is distributed as an MVC application, the controller and view can easily be removed, allowing for the model to be run as a part of another project. 

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

## Developing For This Project
The project is divided into View, Model and Controller packages. All of the components for simulation are within the Model package. The following UML Diagram is accurate to our current application's layout, and contains a map of some of our planned features.
![UML diagram of current project layout](http://patrickmart.in/media/capstone/ecs2.png)

In order to set up a programming environment to modify or expand this project, you must first install the basic dependencies that can be used to compile, test, and run the program. Once that is done, the program can be edited and compiled from source.
* **Java >=1.8** As our viewer makes use of Java's Lambda Expressions, you will need a minimum of java 8. As with every Java program, you need the [JRE](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) to run it, and the [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) to compile it.
* **[Maven](https://maven.apache.org/)** The application, as well as the dependencies for the view, are managed by maven. This simplifies compiling. When compiling, maven automatically imports GLFW and JUnit.
* **Editors** With these requirements met, you should be able to clone the repository (Find out how [Here](
##Downloading, Compiling, and Running)) and modify it with your favorite editor. As it stands, we have used Notepad++, Vim, and [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows) to develop the project. We recommend IntelliJ due to its extra capabilities such as debugging, linting, and built-in testing.

## Resources
[Project Proposal](https://github.com/Pharaoh439/capstone/blob/master/ProjectProposal.md)

[Technical Report](https://github.com/Pharaoh439/capstone/blob/master/TechnicalReport.md)
