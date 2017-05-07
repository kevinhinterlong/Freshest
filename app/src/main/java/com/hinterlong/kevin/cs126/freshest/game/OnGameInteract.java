package com.hinterlong.kevin.cs126.freshest.game;

/**
 * Listener for handling when the screen is touched and when a player drags their finger on the screen
 * This could probably be replaced with {@link android.view.View.OnClickListener}
 */
public interface OnGameInteract {
	/**
	 * Handles when the user moves their finger on the screen
	 *
	 * @param x  x position touched
	 * @param y  y position touched
	 * @param dx moved dx in x direction
	 * @param dy moved dy in y direction
	 */
	void onMove(float x, float y, float dx, float dy);

	/**
	 * Handles when the user touches the screen.
	 *
	 * @param x x position touched
	 * @param y y position touched
	 */
	void onTouch(float x, float y);
}
