package com.hinterlong.kevin.cs126.freshest.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.hinterlong.kevin.cs126.freshest.R;
import com.hinterlong.kevin.cs126.freshest.database.DatabaseUtils;
import com.hinterlong.kevin.cs126.freshest.model.ScoreRecord;
import com.hinterlong.kevin.cs126.freshest.utils.SettingsUtils;
import com.hinterlong.kevin.cs126.freshest.viewholder.ScoreRecordViewHolder;

/**
 * A simple fragment holding the personal best and the top 100 players.
 */
public class LeaderboardsFragment extends Fragment {
	public static final String TAG = LeaderboardsFragment.class.getSimpleName();
	private static final int SCORES_TO_SHOW = 100;
	private FirebaseRecyclerAdapter<ScoreRecord, ScoreRecordViewHolder> firebaseRecyclerAdapter;
	private TextView personalBest;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_high_scores, container, false);
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.high_scores_list);
		recyclerView.setHasFixedSize(true);
		firebaseRecyclerAdapter = getFirebaseRecyclerAdapter(DatabaseUtils.getScoresTable());
		recyclerView.setAdapter(firebaseRecyclerAdapter);

		personalBest = (TextView) view.findViewById(R.id.personal_best);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		String name = SettingsUtils.getPlayerName(getContext());
		int score = SettingsUtils.getPlayerScore(getContext());
		personalBest.setText(getString(R.string.personal_best, name, score));
	}

	private FirebaseRecyclerAdapter<ScoreRecord, ScoreRecordViewHolder> getFirebaseRecyclerAdapter(DatabaseReference db) {
		return new FirebaseRecyclerAdapter<ScoreRecord, ScoreRecordViewHolder>(
				ScoreRecord.class,
				R.layout.score_record_list_item,
				ScoreRecordViewHolder.class,
				db.orderByChild("score").limitToFirst(SCORES_TO_SHOW)
		) {
			@Override
			protected void populateViewHolder(ScoreRecordViewHolder viewHolder, ScoreRecord model, int position) {
				//since the recyclerview is reversed, we need the complement of our position
				int leaderboardPosition = firebaseRecyclerAdapter.getItemCount() - position;
				Log.d(TAG, "Placing " + model.getName() + "(" + model.getScore() + ") at " + leaderboardPosition);

				viewHolder.updateModel(getContext(), model);

			}

			@Override
			public void onBindViewHolder(ScoreRecordViewHolder viewHolder, int position) {
				super.onBindViewHolder(viewHolder, position);
				int leaderboardPosition = firebaseRecyclerAdapter.getItemCount() - position;
				viewHolder.updatePosition(leaderboardPosition);
			}

			@Override
			protected void onChildChanged(ChangeEventListener.EventType type, int index, int oldIndex) {
				super.onChildChanged(type, index, oldIndex);
				firebaseRecyclerAdapter.notifyItemChanged(index);
				firebaseRecyclerAdapter.notifyItemChanged(oldIndex);
			}
		};
	}
}
