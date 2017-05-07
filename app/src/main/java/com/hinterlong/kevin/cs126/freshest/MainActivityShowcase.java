package com.hinterlong.kevin.cs126.freshest;

import android.graphics.Color;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.hinterlong.kevin.cs126.freshest.activities.MainActivity;

/**
 * Showcases the features of the {@link MainActivity}.
 */
public class MainActivityShowcase {
	private MainActivity mainActivity;
	private ShowcaseView showcaseView;
	private int counter;
	private int[] viewsToHighlight = {
			R.id.navigation,
			R.id.play_game,
			R.id.navigation_leaderboards,
			//show high scores
			R.id.navigation_help
	};
	private int[] fragmentsToShow = {
			R.id.navigation_home,
			R.id.navigation_home,
			R.id.navigation_leaderboards,
			//show high scores
			R.id.navigation_help
	};
	private int[] mainText = {
			R.string.showcase_bottom_navigation,
			R.string.showcase_play_game_button,
			R.string.showcase_high_scores,
			//show high scores
			R.string.showcase_help_screen
	};

	public MainActivityShowcase(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		counter = 0;
		initializeShowCaseView();
		highlightNextFeature();
	}

	/**
	 * Initializes the {@link ShowcaseView} on the {@link MainActivity}
	 */
	private void initializeShowCaseView() {
		TextPaint textPaint = new TextPaint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(64);
		showcaseView = new ShowcaseView.Builder(mainActivity)
				.hideOnTouchOutside()
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						highlightNextFeature();
					}
				})
				.withMaterialShowcase()
				.setStyle(R.style.AppTheme)
				.setContentTextPaint(textPaint)
				.build();

		//https://stackoverflow.com/questions/22760522/android-showcaseviews-buttonlayoutparams-wont-work
		RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// Set margins to the button, we add 16dp margins here
		int margin = ((Number) (mainActivity.getResources().getDisplayMetrics().density * 16)).intValue();
		//add extra bottom margin to show bottom navigation
		lps.setMargins(margin, margin, margin, 4 * margin);
		showcaseView.setButtonPosition(lps);
	}

	/**
	 * Sets the next view to be highlighed, updates the text, and prepares for the next feature to be shown
	 */
	private void highlightNextFeature() {
		if (moreToShow()) {
			int viewToHighlight = viewsToHighlight[counter];
			String textToDisplay = mainActivity.getString(mainText[counter]);

			showcaseView.setTarget(new ViewTarget(viewToHighlight, mainActivity));
			showcaseView.setContentText(textToDisplay);
			showcaseView.overrideButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					counter++;
					highlightNextFeature();
					moveToNextFragment();
				}
			});
		}
	}

	/**
	 * Checks if there are any more views to be highlighted.
	 *
	 * @return true if more views need to be shown
	 */
	private boolean moreToShow() {
		if (counter >= viewsToHighlight.length) {
			showcaseView.hide();
			return false;
		}
		return true;
	}

	/**
	 * Move to the next fragment which is being showcased.
	 */
	private void moveToNextFragment() {
		if (moreToShow()) {
			int fragmentToShow = fragmentsToShow[counter];
			switch (fragmentToShow) {
				case R.id.navigation_home:
					mainActivity.showHomeFragment();
					break;
				case R.id.navigation_help:
					mainActivity.showHelpFragment();
					break;
				case R.id.navigation_leaderboards:
					mainActivity.showLeaderboardsFragment();
					break;
				default:
					mainActivity.showHomeFragment();
			}
		}
	}
}
