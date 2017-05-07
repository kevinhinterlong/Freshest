package com.hinterlong.kevin.cs126.freshest.game.objects;

import android.graphics.PointF;

import com.hinterlong.kevin.cs126.freshest.game.shapes.Rectangle;

/**
 * A game object which represents a rectangle and can be used as the floor for a map.
 */
public class Floor extends GameObject {
	private final float[] color;
	private Rectangle rectangle;


	public Floor(PointF position, PointF size, float[] color) {
		super(position, size);
		this.color = color;
	}

	@Override
	public void initialize() {
		PointF position = getPosition();
		PointF size = getSize();
		rectangle = new Rectangle(position.x, position.y, size.x, size.y, color);
	}

	@Override
	public void setVelocity(PointF velocity) {
		//the velocity cannot be changed
	}

	@Override
	public void draw(float[] mvpMatrix) {
		rectangle.draw(mvpMatrix);
	}

	@Override
	public float[] getModelMatrix() {
		return rectangle.mModelMatrix;
	}

	@Override
	public float getAngle() {
		return 0;
	}
}
