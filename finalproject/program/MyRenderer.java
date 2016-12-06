package finalproject.program;

/*MyRenderer.java
 * CS355 Final Project
 * All drawing located here.
 * To see this code in action, run FinalProject.java
 * 
 * Created using Eclipse Neon v.4.6.0, JRE v.1.5, JOGL v1.1.1
 * Additional Components: MyInputHandler.java, FinalProject.java
 * finalproject/common folder, images folder
 * Created November 21, 2016
 * CS-355 Fall 2016
 * @author Olivia Brown
 * Copyright(c) Olivia Brown, All Rights Reserved
 */

import java.io.*;
import java.util.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import demos.common.ResourceRetriever;
import demos.common.TextureReader;

class MyRenderer implements GLEventListener 
{
	private final float PI_180 = (float) (Math.PI/180.0);
	private GLU glu = new GLU();	//GLU: simple mapping and drawing between screen and real world coordinates.
	private Sector sector1;			//Object to hold the museum building
	private GLUquadric quadric;		//Variable for the quadratic artwork shapes
	
	private float heading;
	private float xpos;
	private float yrot;
	private float zpos = 2.0f;
	
	private boolean walkForward;
	private boolean walkBackward;
	private boolean turnRight;
	private boolean turnLeft;
	
	private int[] textures = new int[5];	//Array that holds my image generated textures
	
	//Method to read coordinates from a text document to create the the museum building
	private void planMuseum() throws IOException {
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(ResourceRetriever.getResourceAsStream("images/Museum.txt")));
			String line = null;
			
			//while the line is not empty
			while((line = in.readLine()) != null)
			{
				/*if this condition is fulfilled
				 *conditions OR:
				 *no spaces when the line is trimmed (spaces taken out)
				 *the line begins with '//'
				 */
				if (line.trim().length() == 0 || line.trim().startsWith("//"))
					continue;
				
				//If statement to determine how many triangles will be drawn in the window
				if (line.startsWith("TRITOT"))
				{
					int triNum;
					triNum = Integer.parseInt(line.substring(line.indexOf("TRITOT") + "TRITOT".length() + 1));
					sector1 = new Sector(triNum);	
					break;
				}		
			}
			
			for (int i = 0; i < sector1.totaltri; i++)
			{
				for (int vert = 0; vert < 3; vert++){
					
					while ((line = in.readLine()) != null){
						if (line.trim().length() == 0 || line.trim().startsWith("//"))
							continue;
						break;
					}
					
					/*if the line is not empty, then
					 * create string tokenizer object for the line, space is delimiter - what separates each value
					 * grabs x, y, z, u, v and converts to float
					 */
					if (line != null) {
						StringTokenizer st = new StringTokenizer(line, " ");
						
						sector1.triangles[i].vertex[vert].x = Float.valueOf(st.nextToken()).floatValue();
						sector1.triangles[i].vertex[vert].y = Float.valueOf(st.nextToken()).floatValue();
						sector1.triangles[i].vertex[vert].z = Float.valueOf(st.nextToken()).floatValue();
						sector1.triangles[i].vertex[vert].u = Float.valueOf(st.nextToken()).floatValue();
						sector1.triangles[i].vertex[vert].v = Float.valueOf(st.nextToken()).floatValue();	
					}
				}
			}
			
			
		} finally {
			if (in != null)		//Closes the BufferedReader if the line is empty
				in.close();
		}
		
		
	}
	
	//Method to load images and assign them to be textures
	private void loadTextures(GL gl, GLU glu) throws IOException {
		
	        String[] textureNames = new String[]{
	            "images/david.png",
	            "images/column.png",
	            "images/floor.jpeg",
	            "images/marble.jpeg",
	            "images/gold.jpeg"
	        };

	        gl.glGenTextures(4, textures, 0);					// Create The Texture
	        
	        for (int loop = 0; loop < 5; loop++)				// Loop Through 5 Textures
	        {
	            String textureName = textureNames[loop];
	            TextureReader.Texture texture = TextureReader.readTexture(textureName);
	            gl.glBindTexture(GL.GL_TEXTURE_2D, textures[loop]);
	            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, texture.getWidth(), texture.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, texture.getPixels());
	            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
	            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
	        }
	    }

	public void display(GLAutoDrawable glDrawable)
	{
		walking();
		GL gl = glDrawable.getGL(); 			//GL declaration cannot be changed
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);	//Clearing the color and depth buffer
	
		gl.glLoadIdentity();
		
		float x = 0, y = 0, z = 0, u = 0, v = 0;
		
		//the actual screen rendering here.
		float xMove = -xpos;
		float zMove = -zpos;
		float yMove = -0.5f;
		float yRotateWorld = 360.0f - yrot;
		
		gl.glRotatef(yRotateWorld, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(xMove, yMove, zMove);
		
		//draw each triangle. the sector holds the entire museum
		//it draws all the triangles present by grabbing the x, y, and z
		//the u, v holds info necessary for the text wrapper
		for (int i = 0; i < sector1.totaltri; i++)
		{
			gl.glBindTexture(GL.GL_TEXTURE_2D, textures[3]);
			gl.glBegin(GL.GL_TRIANGLES);
			gl.glNormal3f(0.0f, 0.0f, 1.0f);
			x = sector1.triangles[i].vertex[0].x;
			y = sector1.triangles[i].vertex[0].y;
			z = sector1.triangles[i].vertex[0].z;
			u = sector1.triangles[i].vertex[0].u;
			v = sector1.triangles[i].vertex[0].v;
			
			gl.glTexCoord2f(u, v);
			gl.glVertex3f(x, y, z);
			
			x = sector1.triangles[i].vertex[1].x;
			y = sector1.triangles[i].vertex[1].y;
			z = sector1.triangles[i].vertex[1].z;
			u = sector1.triangles[i].vertex[1].u;
			v = sector1.triangles[i].vertex[1].v;
			gl.glTexCoord2f(u, v);
			gl.glVertex3f(x, y, z);
			
			x = sector1.triangles[i].vertex[2].x;
			y = sector1.triangles[i].vertex[2].y;
			z = sector1.triangles[i].vertex[2].z;
			u = sector1.triangles[i].vertex[2].u;
			v = sector1.triangles[i].vertex[2].v;
			gl.glTexCoord2f(u, v);
			gl.glVertex3f(x, y, z);
			gl.glEnd();
		}
		
		constructFloor(gl);
		
		//pillars to hold the artwork
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[1]);   /* choose the texture to use.*/
		pillarConstructor(glDrawable, -1.0f, 0.0f, -1.7f, 0.2f, 0.4f, 0.2f);
		pillarConstructor(glDrawable, -1.5f, 0.0f,  1.7f, 0.2f, 0.4f, 0.2f);
		pillarConstructor(glDrawable, 1.5f, 0.0f, -1.7f, 0.2f, 0.4f, 0.2f);
		pillarConstructor(glDrawable, 1.5f, 0.0f,  1.7f, 0.2f, 0.4f, 0.2f);
		
		//the masterpiece
		gl.glPushMatrix();
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);   /* choose the texture to use.*/
		pillarConstructor(glDrawable, 0.0f, 0.0f, 0.0f, 0.2f, 0.7f, 0.2f);
		gl.glPopMatrix();
		
		//shapes - next job
	        
	        //sphere commands
	        gl.glPushMatrix();
			gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);
	        gl.glTranslatef(-1.0f, 0.6f, -1.7f);
	        gl.glScalef(0.3f, 0.4f, 0.4f);
	        glu.gluSphere(quadric, 0.5f, 20, 20);
	        gl.glPopMatrix(); 
	        
	        
	        //cylinder commands
	        gl.glPushMatrix();
	        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);
	        gl.glTranslatef(-1.5f, 0.4f, 1.7f);
	        gl.glScalef(0.2f, 0.1f, 0.2f);
	        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
	        glu.gluCylinder(quadric, 0.5f, 0.5f, 2.0f, 32, 32);
	        gl.glPopMatrix();
	        
	        //disk commands
	        gl.glPushMatrix();
	        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);
	        gl.glTranslatef(1.5f, 0.55f, -1.7f);
	        gl.glScalef(0.3f, 0.3f, 0.3f);
	        glu.gluDisk(quadric, 0.2f, 0.5f, 32, 32);
	        gl.glPopMatrix();
	        
	        //cone commands
	        gl.glPushMatrix();
	        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);
	        gl.glTranslatef(1.5f, 0.6f, 1.7f);
	        gl.glScalef(0.4f, 0.4f, 0.4f);
	        gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
	        glu.gluCylinder(quadric, 0.3f, 0.0f, 0.5f, 32, 32);
	        gl.glPopMatrix();
	        
	}
	
	//1. initial methods when the window is displayed
	public void init(GLAutoDrawable glDrawable)
	{
		GL gl = glDrawable.getGL();
		
		//Try-catch to load textures and museum, if these fail, the program throws a runtime exception
		try {
			loadTextures(gl, glu);
			planMuseum();		
		} catch (IOException e){
			throw new RuntimeException(e);
		}
		
		//initialization for glu
		quadric = glu.gluNewQuadric();						//pointer to quadric object
		glu.gluQuadricNormals(quadric, GLU.GLU_SMOOTH);		//smooth (normals) texture for glu objects
		glu.gluQuadricTexture(quadric, true);				//create texture coordinates for glu objects
		
		gl.glEnable(GL.GL_TEXTURE_2D);					//Fire up the texture capabilities
		gl.glEnable(GL.GL_LIGHTING);					//Fire up lighting capabilities
		gl.glEnable(GL.GL_LIGHT0);						//Fire up a light
		gl.glEnable(GL.GL_LIGHT1);						//Fire up a second light
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
		gl.glShadeModel(GL.GL_SMOOTH);					//Enable a Smooth shading model
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);								
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);	
		gl.glEnable( GL.GL_NORMALIZE );
		
		
		float[] ambientLux = {1.0f, 1.0f, 1.0f, 1.0f};			//Color array for Ambient Lighting
		float[] ambientLux2 = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] diffusionLux = {0.8f, 0.8f, 0.8f, 1.0f};
		float[] diffusionLux2 = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] lightPos1 = {-2.0f, 1.0f, -.5f};				//Position of the light source
		float[] lightDirect = {0.0f, 1.0f, 0.0f};				//Direction the light rays 
		
		//setting up lighting to show up in the program
		//general environment lighting
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLux, 0);		//ambient light to set the mood
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffusionLux, 0);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, lightDirect,0);
		
		//lighting for one "bulb"
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, ambientLux2, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, diffusionLux2, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos1, 0);
		
	}
	
	//2. method to be called when the window is reshaped - when cursor adjusts the size of the window
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height)
	{
		final GL gl = glDrawable.getGL();
		
		if (height <= 0)		//If the height of the window apporaches less than one, then make the height equal to 1
		{
			height = 1;
		}
		
		final float h = (float) width/ (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0, h, 1.0, 20.0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
	}
	
	//these series of methods act as switches for the input. when the key is pressed then the action is
	//is set to true and the walking function carries out the movement along the 
	//the invisible axis that the world is set upon.
	public void walkForward(boolean step) {
		walkForward = step;
	}
	
	public void walkBackward(boolean step) {
		walkBackward = step;
	}
	
	public void turnLeft(boolean turn) {
		turnLeft = turn;
	}
	
	public void turnRight(boolean turn) {
		turnRight = turn;
	}
	
	//4. Method for walking, changes the postion and displays accordingly as the switches are pressed
	private void walking() {
		
		if (walkForward) {
			xpos -= (float) Math.sin(heading * PI_180) * 0.05f;
			zpos -= (float) Math.cos(heading * PI_180) * 0.05f;
		}
		
		if (walkBackward) {
			xpos += (float) Math.sin(heading * PI_180) * 0.05f;
			zpos += (float) Math.cos(heading * PI_180) * 0.05f;	
		}
		
		if (turnLeft) {
			heading += 1.0f;
			yrot = heading;
		}
		
		if (turnRight) {
			heading -= 1.0f;
			yrot = heading;
		}
		
	}
	

	
	/* Method to draw the pillars to display the "artwork shapes.
	 * 1. the dimensions of the quad
	 * 2. location of the pillar as arguments
	 */
	public void pillarConstructor(GLAutoDrawable glDrawable, float xloc, float yloc, float zloc,  float w, float h, float d)
	{
		GL gl = glDrawable.getGL();
		
		gl.glPushMatrix();
       
        gl.glTranslatef(xloc, yloc, zloc);

		gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-w, -0, d);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(w, -0, d);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(w, h, d);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-w, h, d);
        
        // Back Face
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-w, -0, -d);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-w, h, -d);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(w, h, -d);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(w, -0, -d);
        
        // Top Face
        gl.glVertex3f(-w, h, -d);
        gl.glVertex3f(-w, h, d);
        gl.glVertex3f(w, h, d);
        gl.glVertex3f(w, h, -d);
        
        // Bottom Face
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-w, 0, -d);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(w, 0, -d);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(w, -0, d);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-w, -0, d);
        
        // Right face
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(w, -0, -d);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(w, h, -d);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(w, h, d);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(w, -0, d);
        
        // Left Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-w, -0, -d);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-w, -0, d);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-w, h, d);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-w, h, -d);
        gl.glEnd();
		
        gl.glPopMatrix();       
	}
	
	//Method to construct the floor of the Museum by drawing a quad
	private void constructFloor(GL gl)
	{
		gl.glPushMatrix();
		gl.glBindTexture(GL.GL_TEXTURE_2D, textures[2]);	//texture 0 is selected
		gl.glBegin(GL.GL_QUADS);				//draw quad
		gl.glNormal3f(0.0f, 1.0f, 0.0f);		//normal vector points up
		gl.glTexCoord2f(0.0f, 5.0f);			//bottom left of the texture
		gl.glVertex3f(-3.5f, 0.0f, 4.0f);		//bottom left corner of floor
		
		gl.glTexCoord2f(0.0f, 0.0f);			//top left of texture
		gl.glVertex3f(-3.5f, 0.0f, -4.0f);		//top left corner of floor
		
		gl.glTexCoord2f(5.0f, 0.0f);			//top right of texture
		gl.glVertex3f(3.5f, 0.0f, -4.0f);		//top right corner of the floor
		
		gl.glTexCoord2f(5.0f, 5.0f);			//bottom right of the texture
		gl.glVertex3f(3.5f, 0.0f, 4.0f);		//bottom right of the texture
		gl.glEnd();
		gl.glPopMatrix();
	}
	
	//Method actions for when the display is changed somehow, with booleans tracking if the mode changes
	//or even the display itself
	public void displayChanged(GLAutoDrawable glDrawable, boolean modeChanged, boolean deviceChanged){}
		
	
	/*************Classes******************/
	private static class Vertex
	{
		public float x, y, z;
		public float u, v;
	}
	
	private static class Triangle {
		public Vertex[] vertex = new Vertex[3];
		
		public Triangle() {
			for (int i = 0; i < 3; i++) 
				vertex[i] = new Vertex();
			}	
		}
	
	private static class Quad {
		public Vertex[] vertex = new Vertex[4];		//array holds 4 vertices
		
		public Quad() {					//square class
		for (int i = 0; i < 4; i++)			//for each thing in array, create new vertex object
			vertex[i] = new Vertex();
		}
	}
	
	
	
	public static class Sector {
		
		public int totaltri;
		public int totalsqr;
		public Quad[] quads;
		public Triangle[] triangles;
		
		public Sector(int sh)
		{
			totaltri = sh;
			triangles = new Triangle[sh];
			for (int i = 0; i < sh; i++)
			{
				triangles[i] = new Triangle();
			}
			
			//total quads: create quad object array and assign and declare each object depending on how many quads wanted
			totalsqr = sh;
			quads = new Quad[sh];
			for (int i = 0; i < sh; i++)
			{
				quads[i] = new Quad();
			}
		}
		
	}
}





