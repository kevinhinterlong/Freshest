package com.hinterlong.kevin.cs126.freshest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hinterlong.kevin.cs126.freshest.R;

/**
 * A simple view holding the help information.
 */
public class HelpFragment extends Fragment {
	public HelpFragment() {

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_help, container, false);
	}
}
