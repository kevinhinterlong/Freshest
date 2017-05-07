package com.hinterlong.kevin.cs126.freshest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hinterlong.kevin.cs126.freshest.R;
import com.hinterlong.kevin.cs126.freshest.activities.GameActivity;

/**
 * A simple fragment for starting the game
 */
public class HomeFragment extends Fragment {

	public HomeFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		Button startGame = (Button) view.findViewById(R.id.play_game);
		startGame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getContext(), GameActivity.class));
			}
		});
		return view;
	}
}
