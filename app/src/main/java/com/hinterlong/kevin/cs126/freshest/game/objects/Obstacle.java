package com.hinterlong.kevin.cs126.freshest.game.objects;

import android.graphics.PointF;

import com.hinterlong.kevin.cs126.freshest.game.shapes.Rectangle;

/**
 * Rectangle which can be used to represent obstacles that the {@link Player} shouldn't run into.
 */
public class Obstacle extends GameObject {
	private final float[] color;
	private Rectangle rectangle;

	public Obstacle(PointF position, PointF size, float[] color) {
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
