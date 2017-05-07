package com.hinterlong.kevin.cs126.freshest.game.objects;

import android.graphics.PointF;

import com.hinterlong.kevin.cs126.freshest.game.Renderable;

/**
 * A game object which can be drawn on the screen
 */
public abstract class GameObject extends BoundedObject implements Renderable {
	private final PointF size;
	private final boolean collideable;
	private PointF position;
	private PointF velocity;

	public GameObject(PointF position, PointF size) {
		this(position, size, true);
	}

	/**
	 * Constructs a game object with basic collision.
	 *
	 * @param position    the starting position
	 * @param size        the width and height of the object
	 * @param collideable whether or not this object can be collided with
	 */
	public GameObject(PointF position, PointF size, boolean collideable) {
		this.position = new PointF(position.x, position.y);
		this.size = new PointF(size.x, size.y);
		System.out.println("Creating object with " + size.x + "," + size.y);
		this.collideable = collideable;
		velocity = new PointF(0, 0);
	}

	/**
	 * Adds two vectors together
	 *
	 * @param one first vector
	 * @param two second vector
	 * @return resulting vector
	 */
	protected static PointF add(PointF one, PointF two) {
		return new PointF(one.x + two.x, one.y + two.y);
	}

	/**
	 * Negates the velocity of the object when it collides with something
	 */
	public void onCollide() {
		velocity.negate();
	}

	@Override
	public boolean collides(BoundedObject that) {
		return collideable && super.collides(that);
	}

	/**
	 * Checks if there is a collission and calls {@link #onCollide()}.
	 *
	 * @param other the bounding box to collide with
	 * @return true if collision.
	 */
	public boolean checkAndDoCollision(BoundedObject other) {
		if (collides(other)) {
			onCollide();
			return true;
		}
		return false;
	}

	public PointF getPosition() {
		return position;
	}

	public void setPosition(PointF position) {
		this.position = position;
	}

	public PointF getVelocity() {
		return velocity;
	}

	public void setVelocity(PointF velocity) {
		this.velocity = velocity;
	}

	public void addVelocity(PointF velocity) {
		this.velocity = add(this.velocity, velocity);
	}

	public void addPosition(PointF pointF) {
		position = add(position, pointF);
	}

	public PointF getSize() {
		return size;
	}

	public abstract float[] getModelMatrix();

	/**
	 * In this method, you can create your OpenGl objects
	 */
	public abstract void initialize();

	/**
	 * Moves the current game object above the other one.
	 *
	 * @param other bounding box to be set above
	 */
	public void setAbove(BoundedObject other) {
		position.y = other.getMaxY() + size.y;
	}

	/**
	 * The angle the object should be displayed at in degrees from x axis
	 *
	 * @return rotation to be drawn at
	 */
	public abstract float getAngle();

	@Override
	public float getMaxX() {
		return position.x + size.x;
	}

	@Override
	public float getMaxY() {
		return position.y + size.y;
	}

	@Override
	public float getMinX() {
		return position.x - size.x;
	}

	@Override
	public float getMinY() {
		return position.y - size.y;
	}
}
