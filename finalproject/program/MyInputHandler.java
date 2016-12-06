package finalproject.program;

/*MyInputHandler.java
 * CS355 Final Project
 * Keyboard Handles located here.
 * To see this code in action, run FinalProject.java
 * 
 * Created using Eclipse Neon v.4.6.0, JRE v.1.5, JOGL v1.1.1
 * Additional Components: FinalProject.java, MyRenderer.java
 * finalproject/common folder, images folder
 * Created November 21, 2016
 * CS-355 Fall 2016
 * @author Olivia Brown
 * Copyright(c) Olivia Brown, All Rights Reserved
 */
import java.awt.event.*;

import demos.common.GLDisplay;

public class MyInputHandler extends KeyAdapter 
{
	/*KeyAdapter is a better choice over KeyListener because with Adapter, 
	 * there is no need to defined all methods like in Listener, Adapter overwrites
	 * the ones you  want overwritten. 
	 * */
	 private MyRenderer renderer; //the render object is made to connect to the programe
	 
	 //Constructor class for the Inputs, takes a renderer object and GLDisplay object as arguments
	 public MyInputHandler(MyRenderer renderer, GLDisplay display)
	 {
		 this.renderer = renderer;	 
	 }
	 
	 public void keyPressed(KeyEvent e)
	 {
		 processKeyEvent(e, true);
	 }
	 
	 public void keyReleased(KeyEvent e)
	 {
		processKeyEvent(e, false);
	 }
	 
	 //method to take each key pressed and tell it what to do.
	 private void processKeyEvent(KeyEvent e, boolean pressed)
	 {
		 switch (e.getKeyCode()) {
		 	case KeyEvent.VK_UP:
		 		//walkForward
		 		renderer.walkForward(pressed);
		 		break;
		 	case KeyEvent.VK_DOWN:
		 		//walkBackward
		 		renderer.walkBackward(pressed);
		 		break;
		 	case KeyEvent.VK_LEFT:
		 		//walkLeft
		 		renderer.turnLeft(pressed);
		 		break;
		 	case KeyEvent.VK_RIGHT:
		 		//walkRight
		 		renderer.turnRight(pressed);
		 		break;
		 }
	 } 
	 
}
