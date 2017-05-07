package com.hinterlong.kevin.cs126.freshest;

import android.content.Context;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.hinterlong.kevin.cs126.freshest.utils.SettingsUtils;
import com.hinterlong.kevin.cs126.freshest.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by kevin on 5/1/2017.
 */
@RunWith(AndroidJUnit4.class)
public class UtilsTest {

	@Test
	public void getColorAsFloatArrayRed() throws Exception {
		float[] colors = Utils.getColorsFromInt(Color.RED);
		assertEquals(255f, colors[0]); //red
		assertEquals(0f, colors[1]); //green
		assertEquals(0f, colors[2]); //blue
		assertEquals(255f, colors[3]); //alpha
	}

	@Test
	public void getColorAsFloatArrayGreen() throws Exception {
		float[] colors = Utils.getColorsFromInt(Color.GREEN);
		assertEquals(0f, colors[0]); //red
		assertEquals(255f, colors[1]); //green
		assertEquals(0f, colors[2]); //blue
		assertEquals(255f, colors[3]); //alpha
	}

	@Test
	public void getColorAsFloatArrayBlue() throws Exception {
		float[] colors = Utils.getColorsFromInt(Color.BLUE);
		assertEquals(0f, colors[0]); //red
		assertEquals(0f, colors[1]); //green
		assertEquals(255f, colors[2]); //blue
		assertEquals(255f, colors[3]); //alpha
	}

	@Test
	public void getColorAsFloatArrayBlack() throws Exception {
		float[] colors = Utils.getColorsFromInt(Color.BLACK);
		assertEquals(0f, colors[0]); //red
		assertEquals(0f, colors[1]); //green
		assertEquals(0f, colors[2]); //blue
		assertEquals(255f, colors[3]); //alpha
	}

	@Test
	public void getGLES20() throws Exception {
		Context context = InstrumentationRegistry.getContext();
		assertTrue(Utils.hasGLES20(context));
	}

	@Test
	public void getID() throws Exception {
		Context context = InstrumentationRegistry.getContext();
		String key1 = SettingsUtils.getPlayerId(context);
		String key2 = SettingsUtils.getPlayerId(context);
		assertEquals(key1, key2);
		assertFalse(key1.isEmpty());
	}

	@Test
	public void getScore() throws Exception {
		Context context = InstrumentationRegistry.getContext();
		int score = SettingsUtils.getPlayerScore(context);
		assertTrue(score >= 0);
	}

	@Test
	public void getName() throws Exception {
		Context context = InstrumentationRegistry.getContext();
		String original = SettingsUtils.getPlayerName(context);
		assertFalse(original.isEmpty());
		String name = "";
		SettingsUtils.savePlayerName(context, name);
		assertEquals(original, SettingsUtils.getPlayerName(context));
	}
}
