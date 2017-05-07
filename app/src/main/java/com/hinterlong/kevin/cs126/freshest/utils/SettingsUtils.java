package com.hinterlong.kevin.cs126.freshest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.FirebaseDatabase;
import com.hinterlong.kevin.cs126.freshest.database.DatabaseUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Handler for reading and writing to app settings.
 */
public class SettingsUtils {
	public static final String SETTINGS_NAME = "settings";
	public static final String FIRST_LAUNCH = "first_launch";
	public static final String PLAYER_NAME = "player_name";
	public static final String DEFAULT_PLAYER_NAME = "Player";
	public static final String PLAYER_SCORE = "player_score";
	public static final String PLAYER_ID = "player_id";

	/**
	 * Checks if this is the first time the application has been launched.
	 *
	 * @param context application context
	 * @return true if first launch
	 */
	public static boolean isFirstLaunch(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
		boolean firstLaunch = preferences.getBoolean(FIRST_LAUNCH, true);
		if (firstLaunch) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean(FIRST_LAUNCH, false);
			editor.apply();
		}
		return firstLaunch;
	}

	/**
	 * Gets the player's saved name
	 *
	 * @param context application context
	 * @return player's saved name
	 */
	public static String getPlayerName(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
		return preferences.getString(PLAYER_NAME, DEFAULT_PLAYER_NAME);
	}

	/**
	 * Saves a new name for the player.
	 *
	 * @param context application context
	 * @param name    new name to be saved
	 */
	public static void savePlayerName(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
		if (!name.isEmpty()) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(PLAYER_NAME, name);
			editor.apply();
		}
	}

	/**
	 * Gets the player's highest score.
	 *
	 * @param context application context
	 * @return highest score
	 */
	public static int getPlayerScore(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
		return preferences.getInt(PLAYER_SCORE, 0);
	}

	/**
	 * Saves player's max score of their last saved or the new one.
	 *
	 * @param context application context
	 * @param score   score to be saved
	 */
	public static void savePlayerScore(Context context, int score) {
		SharedPreferences preferences = context.getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
		int scoreToSave = Math.max(getPlayerScore(context), score);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(PLAYER_SCORE, scoreToSave);
		editor.apply();
	}

	/**
	 * Gets the player's id in the {@link FirebaseDatabase}.
	 *
	 * @param context application context
	 * @return player's id.
	 */
	public static String getPlayerId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(SETTINGS_NAME, MODE_PRIVATE);
		String playerId = preferences.getString(PLAYER_ID, "");
		if (playerId.isEmpty()) {
			playerId = DatabaseUtils.getPlayerId(DatabaseUtils.getScoresTable());
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(PLAYER_ID, playerId);
			editor.apply();
		}
		return playerId;
	}
}
