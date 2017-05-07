package com.hinterlong.kevin.cs126.freshest.game;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Renderer which sets up the game engine for computing and configures
 * the Model View Projection matrix for displaying the objects.
 * Code borrowed from https://developer.android.com/training/graphics/opengl/projection.html
 */
public class GLRenderer implements GLSurfaceView.Renderer, OnGameInteract {
	private final float[] mvpMatrix = new float[16];// "Model View Projection Matrix"
	private final float[] projectionMatrix = new float[16];
	private final float[] viewMatrix = new float[16];
	private float[] backgroundColor = null;
	private GameEngine gameEngine;

	public GLRenderer(float[] bgColor, GameListener listener) {
		backgroundColor = Arrays.copyOf(bgColor, bgColor.length);
		gameEngine = new GameEngine(listener);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background frame color
		GLES20.glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);

		// Set the camera position (View matrix)
		Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

		// enable texture + alpha blending
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		gameEngine.initialize();

		try { //let everything initialize
			Thread.sleep(100);
		} catch (InterruptedException ignored) {
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Adjust the viewport based on geometry changes,
		// such as screen rotation
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		Matrix.frustumM(projectionMatrix, 0, -8 * ratio, 8 * ratio, -8, 8, 3, 7);
		Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0); // Calculate the projection and view transformation
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Clear the Background color in order to updateModel screen
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		gameEngine.drawFrame(mvpMatrix);

	}

	@Override
	public void onMove(float x, float y, float dx, float dy) {
		gameEngine.onMove(x, y, dx, dy);
	}

	@Override
	public void onTouch(float x, float y) {
		gameEngine.onTouch(x, y);
	}

	public void setFramePerSecond(float timeStep) {
		gameEngine.setFramesPerSecond(timeStep);
	}
}
