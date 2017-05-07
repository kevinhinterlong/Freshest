package com.hinterlong.kevin.cs126.freshest.game;

/**
 * Listener for updating the score and handling {@link OnGameFinish}
 */
public interface GameListener extends OnGameFinish {
	void updateScore(int score);
}
