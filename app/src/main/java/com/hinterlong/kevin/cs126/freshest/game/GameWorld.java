package com.hinterlong.kevin.cs126.freshest.game;

import android.graphics.Color;

import com.hinterlong.kevin.cs126.freshest.utils.Utils;

/**
 * Constants to be used while running the game.
 */
public class GameWorld {

	public static class Rules {
		public static final float[] FLOOR_COLOR = Utils.getColorsFromInt(Color.BLACK);
		public static final float[] PLAYER_COLOR = Utils.getColorsFromInt(Color.WHITE);
		public static final float[] OBSTACLE_COLOR = PLAYER_COLOR;

		public static final float GRAVITY = -30f;
		public static final float SCREEN_WIDTH = 18;
		public static final float GAME_MAP_SPEED = 6;

		public static final float PLAYER_WIDTH = .7f;
		public static final float PLAYER_HEIGHT = 1 * PLAYER_WIDTH;
		public static final float PLAYER_X_POSITION = -SCREEN_WIDTH / 2;
		public static final float PLAYER_Y_POSITION = -3;

		public static final float MAX_OBSTACLE_HEIGHT = 3;
		public static final float FLOOR_Y_POSITION = -7.5f;
	}
}
