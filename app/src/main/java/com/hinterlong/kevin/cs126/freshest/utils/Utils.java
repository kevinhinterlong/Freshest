package com.hinterlong.kevin.cs126.freshest.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Color;

/**
 * Various simple utility functions.
 */
public class Utils {
	private Utils() {
	}

	/**
	 * Check if the phone can use open GL ES 2.0
	 *
	 * @return true if has GLES20
	 */
	public static boolean hasGLES20(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return info.reqGlEsVersion >= 0x20000; //Must be at least OpenGL ES 2.0
	}

	/**
	 * Converts a hex color from #AARRGGBB format to a float array for OpenGL
	 *
	 * @param color hex color as integer
	 * @return float[] with rgba formatting for OpenGL
	 */
	public static float[] getColorsFromInt(int color) {
		float[] colorArray = new float[4];
		colorArray[0] = Color.red(color);
		colorArray[1] = Color.green(color);
		colorArray[2] = Color.blue(color);
		colorArray[3] = Color.alpha(color);
		return colorArray;
	}
}
