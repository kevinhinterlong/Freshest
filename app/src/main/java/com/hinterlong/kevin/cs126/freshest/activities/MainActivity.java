package com.hinterlong.kevin.cs126.freshest.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hinterlong.kevin.cs126.freshest.MainActivityShowcase;
import com.hinterlong.kevin.cs126.freshest.R;
import com.hinterlong.kevin.cs126.freshest.fragments.HelpFragment;
import com.hinterlong.kevin.cs126.freshest.fragments.HomeFragment;
import com.hinterlong.kevin.cs126.freshest.fragments.LeaderboardsFragment;
import com.hinterlong.kevin.cs126.freshest.game.GameSoundManager;
import com.hinterlong.kevin.cs126.freshest.utils.SettingsUtils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Creates the home page of the app showing leaderboards, help, and home screen.
 */
public class MainActivity extends AppCompatActivity {
	private LeaderboardsFragment leaderboardsFragment;
	private HomeFragment homeFragment;
	private HelpFragment helpFragment;
	private BottomNavigationView navigation;
	private int counter = 0;
	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_help:
					switchFragment(helpFragment, R.string.title_help);
					return true;
				case R.id.navigation_home:
					switchFragment(homeFragment, R.string.title_home);
					return true;
				case R.id.navigation_leaderboards:
					switchFragment(leaderboardsFragment, R.string.title_leaderboards);
					return true;
			}
			return false;
		}

	};

	public void showHelpFragment() {
		navigation.setSelectedItemId(R.id.navigation_help);
	}

	public void showHomeFragment() {
		navigation.setSelectedItemId(R.id.navigation_home);
	}

	public void showLeaderboardsFragment() {
		navigation.setSelectedItemId(R.id.navigation_leaderboards);
	}

	private void switchFragment(Fragment fragment, int stringID) {
		getSupportActionBar().setTitle(stringID);
		getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		leaderboardsFragment = new LeaderboardsFragment();
		homeFragment = new HomeFragment();
		helpFragment = new HelpFragment();

		navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
		navigation.setSelectedItemId(R.id.navigation_home);

		if (SettingsUtils.isFirstLaunch(this)) {
			new MainActivityShowcase(this);
		}
		GameSoundManager.getInstance().initialize(this);
	}

	@Override
	protected void attachBaseContext(Context context) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GameSoundManager.getInstance().shutdown();
	}
}
