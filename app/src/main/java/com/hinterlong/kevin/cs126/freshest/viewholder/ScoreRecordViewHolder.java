package com.hinterlong.kevin.cs126.freshest.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hinterlong.kevin.cs126.freshest.R;
import com.hinterlong.kevin.cs126.freshest.model.ScoreRecord;

/**
 * {@link RecyclerView.ViewHolder} for representing a {@link ScoreRecord}
 */
public class ScoreRecordViewHolder extends RecyclerView.ViewHolder {
	private TextView positionText;
	private TextView name;
	private TextView score;

	public ScoreRecordViewHolder(View itemView) {
		super(itemView);
		positionText = (TextView) itemView.findViewById(R.id.player_position);
		name = (TextView) itemView.findViewById(R.id.player_name);
		score = (TextView) itemView.findViewById(R.id.player_score);
	}

	/**
	 * Updates the current viewholder with the given information
	 *
	 * @param scoreRecord the record to be updated with
	 */
	public void updateModel(Context context, ScoreRecord scoreRecord) {
		name.setText(scoreRecord.getName());
		score.setText(context.getString(R.string.score, scoreRecord.getScore()));
	}

	/**
	 * Update the current position in the leaderboards.
	 *
	 * @param leaderboardPosition new position in leaderboards.
	 */
	public void updatePosition(int leaderboardPosition) {
		positionText.setText(String.format("%s.", String.valueOf(leaderboardPosition)));
	}
}
