# SPH Simulation of Biphasic Fluid Flow Through Porous Media

##Overview
This software was developed during my master's course on Computer Graphics. It uses the 
SPH method to simulate the flow of biphasic fluids, such as the mixture of water and oil,
through porous media like rocks.

##Technology
Programmed in Java using the [LWJGL](https://www.lwjgl.org/) library for OpenGL integration.

##How it works
This program supports multiple computer graphics "apps", encapsulating the interface codes.
In order to run it, you must choose the *SPHAppSettings.settings* file from the File -> Open
menu when prompted.

Controls are as follows:

**Left mouse button:** rotates the camera;
**Right mouse button:** moves the camera;
**Mouse wheel:** zooms in and out;
**Space bar:** loads an app from its .settings file;
**Enter:** runs the app;
**Up arrow:** resets the camera position;