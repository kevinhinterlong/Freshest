package com.hinterlong.kevin.cs126.freshest.game.shapes;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.hinterlong.kevin.cs126.freshest.game.GLHelper;
import com.hinterlong.kevin.cs126.freshest.game.Renderable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

/**
 * Create a rectangle in OpenGl with a variable color and size.
 * Mostly copied from https://developer.android.com/training/graphics/opengl/draw.html
 */
public class Rectangle implements Renderable {
	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3; //number of coordinates to be used for each vertex
	private static final float PADDLE_COORDS[] = {
			-1f, 1f, 0.0f,   // top left
			-1f, -1f, 0.0f,   // bottom left
			1f, -1f, 0.0f,   // bottom right
			1f, 1f, 0.0f}; // top right
	private static final String vertexShaderCode =
			"uniform mat4 uMVPMatrix;" +
					"attribute vec4 vPosition;" +
					"void main() {" +
					"  gl_Position = uMVPMatrix * vPosition;" +
					"}";
	private static final String fragmentShaderCode =
			"precision mediump float;" +
					"uniform vec4 vColor;" +
					"void main() {" +
					"  gl_FragColor = vColor;" +
					"}";
	private static final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
	private static final short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices
	private static int mProgram;
	public float[] mModelMatrix = new float[16];
	public float x;
	public float y;
	private FloatBuffer vertexBuffer;
	private ShortBuffer drawListBuffer;
	private float color[];

	public Rectangle(float side, float[] uColor) {
		this(side, side, uColor);
	}

	public Rectangle(float width, float height, float[] uColor) {
		this(0, 0, width, height, uColor);
	}

	public Rectangle(float ix, float iy, float width, float height, float[] uColor) {
		x = ix;
		y = iy;
		float[] paddleCoords = Arrays.copyOf(PADDLE_COORDS, PADDLE_COORDS.length);
		width = Math.abs(width);
		paddleCoords[0] *= width; //set sides to negative length
		paddleCoords[3] *= width; //set sides to
		paddleCoords[6] *= width;
		paddleCoords[9] *= width;

		height = Math.abs(height);
		paddleCoords[1] *= height; //set sides to negative length
		paddleCoords[4] *= height; //set sides to
		paddleCoords[7] *= height;
		paddleCoords[10] *= height;

		color = Arrays.copyOf(uColor, uColor.length);

		Matrix.setIdentityM(mModelMatrix, 0); //use for movement of shape

		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
				// (number of coordinate values * 4 bytes per float)
				paddleCoords.length * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		vertexBuffer.put(paddleCoords);
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);

		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(
				// (# of coordinate values * 2 bytes per short)
				drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

		// prepare shaders and OpenGL program
		int vertexShader = GLHelper.loadShader(
				GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = GLHelper.loadShader(
				GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

	}

	/**
	 * Encapsulates the OpenGL ES instructions for drawing this shape.
	 *
	 * @param mvpMatrix - The Model View Project matrix in which to draw
	 *                  this shape.
	 */
	public void draw(float[] mvpMatrix) {
		// Add program to OpenGL environment
		GLES20.glUseProgram(mProgram);

		// get handle to vertex shader's vPosition member
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

		// Enable a handle to the circle vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// Prepare the coordinate data
		GLES20.glVertexAttribPointer(
				mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false,
				vertexStride, vertexBuffer);

		// get handle to fragment shader's vColor member
		int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		// Set color for drawing the circle
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);

		// get handle to shape's transformation matrix
		int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		GLHelper.checkGlError("glGetUniformLocation");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		GLHelper.checkGlError("glUniformMatrix4fv");

		// Draw the circle
		GLES20.glDrawElements(
				GLES20.GL_TRIANGLES, drawOrder.length,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
