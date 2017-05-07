package com.hinterlong.kevin.cs126.freshest.model;

/**
 * Holds the record for a player's game.
 */
public class ScoreRecord {
	private String name;
	private int score;

	public ScoreRecord(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public ScoreRecord() {
		//required empty constructor
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}