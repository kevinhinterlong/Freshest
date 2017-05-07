package com.hinterlong.kevin.cs126.freshest;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * {@link Application} which overides all font to use a custom typeface
 */
public class FreshestApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(
				new CalligraphyConfig.Builder()
						.setDefaultFontPath("IndieFlower.ttf")
						.setFontAttrId(R.attr.fontPath)
						.build()
		);
	}
}
