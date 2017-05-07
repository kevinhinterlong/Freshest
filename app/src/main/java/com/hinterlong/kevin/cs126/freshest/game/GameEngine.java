package com.hinterlong.kevin.cs126.freshest.game;

import android.graphics.PointF;
import android.opengl.Matrix;

import com.hinterlong.kevin.cs126.freshest.R;
import com.hinterlong.kevin.cs126.freshest.game.objects.Floor;
import com.hinterlong.kevin.cs126.freshest.game.objects.GameObject;
import com.hinterlong.kevin.cs126.freshest.game.objects.InteractiveGameObject;
import com.hinterlong.kevin.cs126.freshest.game.objects.Obstacle;
import com.hinterlong.kevin.cs126.freshest.game.objects.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.hinterlong.kevin.cs126.freshest.game.GameWorld.Rules;

/**
 * Handles all of the physics and updating of objects on the screen.
 */
public class GameEngine implements OnGameInteract {
	public static final String TAG = GameEngine.class.getSimpleName();
	private float framesPerSecond = 1;
	private List<GameObject> gameObjects;
	private InteractiveGameObject interactiveGameObject;
	private int tickCounter = 0;
	private GameListener listener;
	private Obstacle obstacle;
	private boolean gameOver = false;

	public GameEngine(GameListener listener) {
		gameObjects = new ArrayList<>();
		this.listener = listener;

		setInteractiveGameObject(new Player(
				new PointF(Rules.PLAYER_X_POSITION, Rules.PLAYER_Y_POSITION),
				new PointF(Rules.PLAYER_WIDTH, Rules.PLAYER_HEIGHT),
				Rules.PLAYER_COLOR
		));
		addGameObject(new Floor(
				new PointF(0, Rules.FLOOR_Y_POSITION),
				new PointF(Rules.SCREEN_WIDTH, Rules.PLAYER_HEIGHT),
				Rules.FLOOR_COLOR
		));
		obstacle = new Obstacle(
				new PointF(10, Rules.FLOOR_Y_POSITION + Rules.PLAYER_HEIGHT + Rules.MAX_OBSTACLE_HEIGHT),
				new PointF(Rules.PLAYER_WIDTH, Rules.MAX_OBSTACLE_HEIGHT),
				Rules.OBSTACLE_COLOR
		);
		addGameObject(obstacle);
	}

	public void addGameObject(GameObject gameObject) {
		gameObjects.add(gameObject);
	}

	public void setInteractiveGameObject(InteractiveGameObject gameObject) {
		interactiveGameObject = gameObject;
	}

	@Override
	public void onMove(float x, float y, float dx, float dy) {
		interactiveGameObject.onMove(x, y, dx, dy);
	}

	@Override
	public void onTouch(float x, float y) {
		interactiveGameObject.onTouch(x, y);
	}

	/**
	 * Setup background music and initialize all objects on the screen.
	 */
	public void initialize() {
		setBackgroundMusic();
		for (GameObject gameObject : gameObjects) {
			gameObject.initialize();
			gameObject.setVelocity(new PointF(-Rules.GAME_MAP_SPEED, 0));
			gameObject.draw(gameObject.getModelMatrix());
		}
		interactiveGameObject.initialize();
		interactiveGameObject.draw(interactiveGameObject.getModelMatrix());
	}

	/**
	 * Picks the background music to be played for the game.
	 */
	private void setBackgroundMusic() {
		Random random = new Random();
		int backgroundSong;
		if (random.nextInt(10) == 0) { //10% chance
			backgroundSong = R.raw.lazy;
		} else {
			backgroundSong = R.raw.stars;
		}
		GameSoundManager.getInstance().playSound(backgroundSong, true);
	}

	/**
	 * Updates all the objects on screen and updates the score.
	 *
	 * @param mMVPMatrix
	 */
	public void drawFrame(float[] mMVPMatrix) {
		tickCounter++;
		if (tickCounter % 400 == 0) { //TODO FIX THIS so we actually add obstacles :(
			obstacle.setPosition(new PointF(interactiveGameObject.getPosition().x + 35, obstacle.getPosition().y));
		}
		doPhysics();

		drawGameObjects(mMVPMatrix);
		listener.updateScore(getScore());
	}

	/**
	 * Updates the current position and angles of all the game objects.
	 *
	 * @param mMVPMatrix
	 */
	private void drawGameObjects(float[] mMVPMatrix) {
		float[] scratch = new float[16];

		Iterator<GameObject> gameObjectIterator = getAllGameObjects();
		while (gameObjectIterator.hasNext()) {
			GameObject gameObject = gameObjectIterator.next();
			Matrix.setRotateM(gameObject.getModelMatrix(), 0, gameObject.getAngle(), 0, 0, -1.0f);

			//move the circle by the velocity of its x and y components
			PointF position = gameObject.getPosition();
			Matrix.translateM(gameObject.getModelMatrix(), 0, -position.x, position.y, 0); // translation with velocity

			Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, gameObject.getModelMatrix(), 0);//apply transformations to the scratch matrix
			gameObject.draw(scratch);
		}
	}

	/**
	 * Calculates the new positions of all the objects and checks for collisions.
	 */
	private void doPhysics() {
		if (!gameOver) {
			for (GameObject gameObject : gameObjects) {
				update(gameObject);
				//and accleration

				//check and do collisions
				boolean collision = interactiveGameObject.checkAndDoCollision(gameObject);
				if (collision) {
					listener.onGameFinish();
					gameOver = true;
				}
			}
			interactiveGameObject.addVelocity(new PointF(0, Rules.GRAVITY / framesPerSecond));
			update(interactiveGameObject);
		}
	}

	/**
	 * Updates an objects position and velocity.
	 *
	 * @param gameObject
	 */
	private void update(GameObject gameObject) {
		PointF position = gameObject.getPosition();
		PointF velocity = gameObject.getVelocity();
		PointF newPosition = new PointF(position.x + velocity.x / framesPerSecond, position.y + velocity.y / framesPerSecond);
		gameObject.setPosition(newPosition);
	}

	/**
	 * Combines all game objects to be iterated over.
	 *
	 * @return iterator of all game objects
	 */
	public Iterator<GameObject> getAllGameObjects() {
		List<GameObject> all = new ArrayList<>();
		all.addAll(gameObjects);
		all.add(interactiveGameObject);
		return all.iterator();
	}

	/**
	 * Sets the FPS of the game.
	 *
	 * @param framesPerSecond FPS to run at.
	 */
	public void setFramesPerSecond(float framesPerSecond) {
		this.framesPerSecond = framesPerSecond;
	}

	/**
	 * Calculates a score for the current position of the player.
	 *
	 * @return the player's score.
	 */
	public int getScore() {
		return (int) ((int) (Rules.GAME_MAP_SPEED * tickCounter) / framesPerSecond);
	}
}
