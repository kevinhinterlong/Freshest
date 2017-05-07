package com.hinterlong.kevin.cs126.freshest.game;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all the sounds to be played in the game.
 */
public class GameSoundManager {
	private static GameSoundManager gameSoundManager;
	private Map<Integer, SimpleMediaPlayer> addedSounds;
	private Context context;

	private GameSoundManager() {
		addedSounds = new HashMap<>();
	}

	public static synchronized GameSoundManager getInstance() {
		if (gameSoundManager == null) {
			gameSoundManager = new GameSoundManager();
		}
		return gameSoundManager;
	}

	/**
	 * This must be called so that songs can be loaded from resources.
	 *
	 * @param context the application's current context.
	 */
	public void initialize(Context context) {
		this.context = context;
	}

	public void addSound(int id) {
		MediaPlayer mediaPlayer = MediaPlayer.create(context, id);
		addedSounds.put(id, new SimpleMediaPlayer(mediaPlayer));
	}

	/**
	 * Adds a new song and playes it.
	 *
	 * @param id id of song in resources
	 */
	public void playSound(int id) {
		playSound(id, false);
	}

	/**
	 * Adds a new song and plays it.
	 *
	 * @param id   id of song in resources.
	 * @param loop should the song loop
	 */
	public void playSound(int id, boolean loop) {
		if (!addedSounds.containsKey(id)) {
			addSound(id);
		}
		SimpleMediaPlayer mediaPlayer = addedSounds.get(id);
		mediaPlayer.start(loop);
	}

	/**
	 * Remove all stopped media and pause playing media.
	 */
	public void pause() {
		for (int id : addedSounds.keySet()) {
			SimpleMediaPlayer mp = addedSounds.get(id);
			if (!mp.isPlaying()) {
				mp.stopAndRelease();
				addedSounds.remove(id);
				mp = null;
			}
			if (mp != null) {
				mp.pause();
			}
		}
	}

	/**
	 * Resume all songs at their previous state.
	 */
	public void resume() {
		for (SimpleMediaPlayer mp : addedSounds.values()) {
			mp.resume();
		}
	}

	/**
	 * Clears all resources and stops music.
	 */
	public void shutdown() {
		for (SimpleMediaPlayer mp : addedSounds.values()) {
			mp.stopAndRelease();
		}
		addedSounds.clear();
		gameSoundManager = null;
	}

	/**
	 * Simple wrapper class for {@link MediaPlayer} for saving state and
	 */
	private class SimpleMediaPlayer {
		private MediaPlayer mediaPlayer;
		private int currentPosition;

		public SimpleMediaPlayer(MediaPlayer mediaPlayer) {
			this.mediaPlayer = mediaPlayer;
		}

		public void start(boolean looping) {
			mediaPlayer.start();
			mediaPlayer.setLooping(looping);
		}

		public void pause() {
			mediaPlayer.pause();
			currentPosition = mediaPlayer.getCurrentPosition();
		}

		public void resume() {
			mediaPlayer.seekTo(currentPosition);
			mediaPlayer.start();
		}

		public void stopAndRelease() {
			mediaPlayer.stop();
			mediaPlayer.release();
		}

		public boolean isPlaying() {
			return mediaPlayer.isPlaying();
		}
	}
}
