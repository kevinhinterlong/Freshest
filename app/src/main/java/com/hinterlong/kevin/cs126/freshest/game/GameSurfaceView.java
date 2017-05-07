package com.hinterlong.kevin.cs126.freshest.game;

/**
 * Required methods for a game surface view to be run
 */
public interface GameSurfaceView {
	void requestRender();

	void updateGameState();

	void setFramesPerSecond(float framesPerSecondtep);
}
