package finalproject.program;

/*FinalProject.java
 * CSS 355 Final Project
 * An Encapsulation of All Learned JOGL Techniques
 * This Project is the last hurrah for the Computer Graphics course. It is
 * a model of a museum gallery, not a real one mind you. Using a combo of 
 * 3D Shape generation, Input Registering, Texture Wrapping, and Lighting, 
 * a user is able to walk around and enjoy the various "artwork" 
 * laid about using the arrow keys on their keyboard.
 * 
 * Created using Eclipse Neon v.4.6.0, JRE v.1.5, JOGL v1.1.1
 * Additional Components: MyInputHandler.java, MyRenderer.java
 * finalproject/common folder, images folder
 * Created November 21, 2016
 * CS-355 Fall 2016
 * @author Olivia Brown
 * Copyright(c) Olivia Brown, All Rights Reserved
 *
 * Refer to PDF for questions and final reflections.
 */

import demos.common.GLDisplay;

public class CS355_FInalProject_OliviaBrown 
{	
	//The Main Method - Refers to other classes
	public static void main(String[] args)
	{
		GLDisplay neheGLDisplay = GLDisplay.createGLDisplay("Final Project: The Olivia Brown Gallery");
		MyRenderer renderer = new MyRenderer();											//calls the core of the program
		MyInputHandler inputHandler = new MyInputHandler(renderer, neheGLDisplay);		//Class that handles input.
		neheGLDisplay.addGLEventListener(renderer);										//Listener for Window
		neheGLDisplay.addKeyListener(inputHandler);										//Listener for keyboard
		neheGLDisplay.start();
	}
}
