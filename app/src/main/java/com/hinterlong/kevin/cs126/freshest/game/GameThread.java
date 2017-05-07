package com.hinterlong.kevin.cs126.freshest.game;

import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Main game loop which runs in a separate thread.
 */
public class GameThread extends Thread {
	public static final String TAG = GameThread.class.getSimpleName();
	public static final int FRAMES_PER_SECOND = 60;
	public static final int ONE_SECOND_IN_MILLISECONDS = (int) TimeUnit.SECONDS.toMillis(1);
	public static final long SKIP_TICKS = ONE_SECOND_IN_MILLISECONDS / FRAMES_PER_SECOND;

	private GameSurfaceView gameSurfaceView;
	private boolean running;
	private long startTime;

	public GameThread(GameSurfaceView gameSurfaceView) {
		this.gameSurfaceView = gameSurfaceView;
		gameSurfaceView.setFramesPerSecond(FRAMES_PER_SECOND);
		running = false;
	}

	@Override
	public void run() {
		super.run();
		Log.v(TAG, "Starting game thread");

		running = true;
		startTime = System.currentTimeMillis();

		long nextGameTick = getTickCount();
		long sleepTime = 0;

		while (running) {
			gameSurfaceView.updateGameState();
			gameSurfaceView.requestRender();

			nextGameTick += SKIP_TICKS;
			sleepTime = nextGameTick - getTickCount();
			if (sleepTime >= 0) { // Need to wait
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ignored) {
				}
			}
		}
		Log.v(TAG, "Stopped running game");
	}

	/**
	 * Finds ms since game start.
	 *
	 * @return ms since started game
	 */
	private long getTickCount() {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Check if the game is currently running.
	 *
	 * @return true if running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Pause the current game. This is called when the game is finished
	 */
	public void pause() {
		running = false;
	}
}
