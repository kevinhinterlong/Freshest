package com.hinterlong.kevin.cs126.freshest.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hinterlong.kevin.cs126.freshest.R;
import com.hinterlong.kevin.cs126.freshest.database.DatabaseUtils;
import com.hinterlong.kevin.cs126.freshest.game.GameListener;
import com.hinterlong.kevin.cs126.freshest.game.GameSoundManager;
import com.hinterlong.kevin.cs126.freshest.game.GameSurface;
import com.hinterlong.kevin.cs126.freshest.model.ScoreRecord;
import com.hinterlong.kevin.cs126.freshest.utils.SettingsUtils;
import com.hinterlong.kevin.cs126.freshest.utils.Utils;

import java.util.ConcurrentModificationException;

/**
 * Allows the game to be started and stopped and presents the score on the screen
 * as the user is playing.
 */
public class GameActivity extends AppCompatActivity implements GameListener {
	public static final String TAG = GameActivity.class.getSimpleName();
	public static final int MAX_NAME_LENGTH = 8;
	private GameSurface glSurfaceView;
	private TextView scoreView;
	private int score = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Log.d(TAG, "Initializing Game Activity");
		if (Utils.hasGLES20(this)) {
			glSurfaceView = new GameSurface(this, this);
			loadGame();
		} else {
			Toast.makeText(this, "Your device doesn't support OpenGL ES 2.0", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Set up for the OpenGL Surface View to be created
	 */
	private void loadGame() {
		setContentView(glSurfaceView);
		glSurfaceView.requestFocus();
		glSurfaceView.setFocusableInTouchMode(true);

		glSurfaceView.initialize();
		glSurfaceView.startGame();
		addScoreView();
		Log.d(TAG, "Game Started");
	}

	/**
	 * Adds the textview onto the screen showing the score
	 */
	private void addScoreView() {
		scoreView = new TextView(this);
		scoreView.setTextColor(Color.WHITE);
		scoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
		scoreView.setTypeface(Typeface.createFromAsset(getAssets(), "IndieFlower.ttf"));
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.START;
		addContentView(scoreView, params);
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
		GameSoundManager.getInstance().resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		glSurfaceView.onPause();
		try {
			GameSoundManager.getInstance().pause();
		} catch (ConcurrentModificationException ignored) {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void updateScore(int newScore) {
		score = newScore;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				scoreView.setText(getResources().getString(R.string.score, score));
			}
		});
	}

	@Override
	public void onGameFinish() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				promptEndGame();
			}
		});
	}

	@Override
	public void onBackPressed() {
		onPause();
		new AlertDialog.Builder(this)
				.setTitle(R.string.game_paused)
				.setCancelable(false)
				.setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						onResume();
					}
				})
				.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						dialog.dismiss();
					}
				})
				.show();
	}

	/**
	 * Prompts the user to play again, quit, or change their name
	 */
	public void promptEndGame() {
		Log.d(TAG, "Game finished, letting user save score");
		saveScore();
		AlertDialog saveScore = new AlertDialog.Builder(this)
				.setTitle(R.string.save_score)
				.setMessage(getString(R.string.save_score_of, score))
				.setCancelable(false)
				.setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						recreate();
					}
				})
				.setNeutralButton(R.string.change_name, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				})
				.create();
		saveScore.show();
		saveScore.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				promptChangeName();
			}
		});
	}

	/**
	 * Saves the user's current score to the database.
	 */
	private void saveScore() {
		String name = SettingsUtils.getPlayerName(getApplicationContext());
		ScoreRecord scoreRecord = new ScoreRecord(name, score);

		String playerId = SettingsUtils.getPlayerId(getApplicationContext());
		DatabaseUtils.getScoresTable().child(playerId).setValue(scoreRecord);

		SettingsUtils.savePlayerScore(this, score);
	}

	/**
	 * Allow the user to save a new user name.
	 * Overrides the default Dialog click listener
	 * https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked#7636468
	 */
	public void promptChangeName() {
		String name = SettingsUtils.getPlayerName(getApplicationContext());
		final EditText input = new EditText(this);
		InputFilter[] filterArray = new InputFilter[]{new InputFilter.LengthFilter(MAX_NAME_LENGTH)};
		input.setFilters(filterArray);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		input.setText(name);
		final AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle(R.string.enter_a_name)
				.setMessage(R.string.input_username_for_leaderboards)
				.setCancelable(false)
				.setPositiveButton("Save", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.setView(input)
				.create();
		dialog.show();

		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = input.getEditableText().toString().trim();
				if (!name.isEmpty()) {
					SettingsUtils.savePlayerName(getApplicationContext(), name);
					saveScore();
					dialog.dismiss();
				} else {
					Toast.makeText(getApplicationContext(), R.string.requires_nonemtpy_name, Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
}
