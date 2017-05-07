package com.hinterlong.kevin.cs126.freshest.game;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Sets up the GLRenderer and gamethread. Also handles touch events.
 */
public class GameSurface extends GLSurfaceView implements GameSurfaceView {
	public static final String TAG = GameSurface.class.getSimpleName();
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private GLRenderer renderer;
	private GameThread thread;
	private GameListener gameListener;
	private PointF previousPoint;

	public GameSurface(Context context, GameListener listener) {
		super(context);
		setEGLContextClientVersion(2); // Create an OpenGL ES 2.0 context.
		this.gameListener = listener;

		renderer = new GLRenderer(new float[]{0, 0, 0, 0}, new GameListener() {
			@Override
			public void updateScore(int score) {
				gameListener.updateScore(score);
			}

			@Override
			public void onGameFinish() {
				gameListener.onGameFinish();
				thread.pause();
			}
		});
		thread = new GameThread(this);
	}

	/**
	 * Initilizes the GLRenderer for this surface view.
	 */
	public void initialize() {
		setRenderer(renderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent e) {
		if (thread.isRunning()) {
			requestRender();
		}
		return true;
	}

	@Override
	public void updateGameState() {
		requestRender();
	}

	@Override
	public void setFramesPerSecond(float framesPerSecondtep) {
		renderer.setFramePerSecond(framesPerSecondtep);
	}

	/**
	 * Calls the gamethread to start running.
	 */
	public void startGame() {
		Log.d(TAG, "Game starting");
		thread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (!thread.isRunning()) {
			return true;
		}

		switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE: //handles the finger being moved
				float dx = e.getX() - previousPoint.x;
				float dy = e.getY() - previousPoint.y;

				dx *= TOUCH_SCALE_FACTOR; //make movement smaller and correct direction
				dy *= -TOUCH_SCALE_FACTOR;
				if (Math.abs(dx) > 1) { //check if dx is too big
					dx = dx / Math.abs(dx) * (float) 1;
				}
				if (Math.abs(dy) > 1) { //check if dx is too big
					dy = dy / Math.abs(dy) * (float) 1;
				}

				renderer.onMove(e.getX(), e.getY(), dx, dy);

				requestRender();
				break;
			case MotionEvent.ACTION_DOWN: //handles screen being touched
				renderer.onTouch(e.getX(), e.getY());
				break;
		}
		previousPoint = new PointF(e.getX(), e.getY());
		return true;
	}
}
