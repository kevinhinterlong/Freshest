package com.hinterlong.kevin.cs126.freshest.game.objects;

/**
 * A square object which can check collisions and bounding with other objects
 */
public abstract class BoundedObject {

	/**
	 * Determines whether two boxes are overlapping or not
	 *
	 * @param thisObject first bounding box
	 * @param thatObject second bounding box
	 * @return true if the boxes are overlapping
	 */
	public static boolean overlaps(BoundedObject thisObject, BoundedObject thatObject) {
		if (thatObject.getMaxX() <= thisObject.getMinX() || thatObject.getMinX() >= thisObject.getMaxX()) {
			return false;
		}
		if (thatObject.getMaxY() <= thisObject.getMinY() || thatObject.getMinY() >= thisObject.getMaxY()) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if this object collides with that
	 *
	 * @param that other bounding box
	 * @return true if overlapping
	 */
	public boolean collides(BoundedObject that) {
		return this.overlaps(that);
	}

	/**
	 * Checks if this object is overlapping with another
	 *
	 * @param that other bounding box
	 * @return true if this overlaps that
	 */
	public boolean overlaps(BoundedObject that) {
		return overlaps(this, that);
	}

	/**
	 * Get the maximum x value for this bounding box
	 *
	 * @return maximum x position
	 */
	public abstract float getMaxX();

	/**
	 * Get the maximum y value for this bounding box
	 *
	 * @return maximum y position
	 */
	public abstract float getMaxY();

	/**
	 * Get the minimum x value for this bounding box
	 *
	 * @return minimum x position
	 */
	public abstract float getMinX();

	/**
	 * Get the minimum y value for this bounding box
	 *
	 * @return minimum y position
	 */
	public abstract float getMinY();
}
