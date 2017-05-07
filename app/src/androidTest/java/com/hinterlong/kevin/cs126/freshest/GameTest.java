package com.hinterlong.kevin.cs126.freshest;

import android.graphics.PointF;
import android.support.test.runner.AndroidJUnit4;

import com.hinterlong.kevin.cs126.freshest.game.objects.BoundedObject;
import com.hinterlong.kevin.cs126.freshest.game.objects.Player;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by kevin on 5/1/2017.
 */
@RunWith(AndroidJUnit4.class)
public class GameTest {
	public static final double DELTA = 0.001;

	@Test
	public void checkGetSize() throws Exception {
		PointF size = new PointF(1, 1);
		Player player1 = new Player(new PointF(0, 0), size, null);
		assertEquals(1, size.x, DELTA);
		assertEquals(1, size.y, DELTA);
	}

	@Test
	public void checkCollisionTrue() throws Exception {
		PointF size1 = new PointF(1, 1);
		PointF size2 = new PointF(0.5f, 0.5f);
		Player player1 = new Player(new PointF(0, 0), size1, null);
		Player player2 = new Player(new PointF(1, 0), size2, null);
		assertTrue(BoundedObject.overlaps(player1, player2));
	}

	@Test
	public void checkCollisionFalse() throws Exception {
		Player player1 = new Player(new PointF(0, 0), new PointF(0, 0), null);
		Player player2 = new Player(new PointF(1, 1), new PointF(0, 0), null);
		assertFalse(BoundedObject.overlaps(player1, player2));
	}

	@Test
	public void checkSetPosition() throws Exception {
		Player player1 = new Player(new PointF(0, 0), new PointF(0, 0), null);
		PointF newPosition = new PointF(1, 1);
		player1.setPosition(newPosition);
		assertEquals(newPosition.x, player1.getPosition().x, DELTA);
		assertEquals(newPosition.y, player1.getPosition().y, DELTA);
	}
}
