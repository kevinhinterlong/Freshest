package com.hinterlong.kevin.cs126.freshest.game;

/**
 * An object which can be drawn on the screen using OpenGL
 */
public interface Renderable {
	/**
	 * Draws an OpenGL object using the given "Model View Projection" matrix
	 *
	 * @param mvpMatrix the "Model View Projection" matrix representing a transformation.
	 */
	void draw(float[] mvpMatrix);
}
