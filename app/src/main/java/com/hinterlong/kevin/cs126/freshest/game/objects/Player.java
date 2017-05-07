package com.hinterlong.kevin.cs126.freshest.game.objects;

import android.graphics.PointF;

import com.hinterlong.kevin.cs126.freshest.R;
import com.hinterlong.kevin.cs126.freshest.game.GameSoundManager;
import com.hinterlong.kevin.cs126.freshest.game.shapes.Rectangle;

/**
 * A player which has the ability to jump to avoid obstacles.
 */
public class Player extends InteractiveGameObject {
	public static final String TAG = Player.class.getSimpleName();
	private static final int MAX_JUMPS = 3;
	private static final int[] jumpSounds = new int[]{
			R.raw.noot0,
			R.raw.noot1,
			R.raw.noot2
	};
	private static final int crashSound = R.raw.boom;
	private float[] color;
	private Rectangle rectangle;
	private int jumps = MAX_JUMPS;

	public Player(PointF position, PointF size, float[] color) {
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
	public float getAngle() {
		return 0;
	}

	/**
	 * Make the player object move upwards and play a jump sound
	 */
	public void jump() {
		if (jumps < MAX_JUMPS) {
			GameSoundManager.getInstance().playSound(jumpSounds[jumps]);
			getVelocity().y = Math.max(getVelocity().y, 0);
			addVelocity(new PointF(0, 20));
			jumps++;
		}
	}

	/**
	 * Reset the players number of jumps and stop them from moving downwards
	 */
	public void onLand() {
		getVelocity().y = 0;
		jumps = 0;
	}

	@Override
	public float[] getModelMatrix() {
		return rectangle.mModelMatrix;
	}

	@Override
	public void draw(float[] mvpMatrix) {
		rectangle.draw(mvpMatrix);
	}

	@Override
	public void onMove(float x, float y, float dx, float dy) {

	}

	@Override
	public void onTouch(float x, float y) {
		jump();
	}

	@Override
	public void onCollide() {
		GameSoundManager.getInstance().playSound(crashSound);
	}

	/**
	 * Overrides the default behavior of colliding with another object to respond
	 * differently when different objects are collided with.
	 *
	 * @param other the bounding box to collide with
	 * @return true if collision
	 */
	@Override
	public boolean checkAndDoCollision(BoundedObject other) {
		//todo visitor pattern
		if (other instanceof Floor) {
			if (overlaps(other)) {
				PointF velocity = getVelocity();
				if (velocity.y < 0) {
					setAbove(other);
					onLand();
					return false;
				}
			}
		} else {
			return super.checkAndDoCollision(other);
		}
		return false;
	}
}
