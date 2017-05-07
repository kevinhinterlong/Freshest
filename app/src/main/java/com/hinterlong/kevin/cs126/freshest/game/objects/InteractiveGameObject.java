package com.hinterlong.kevin.cs126.freshest.game.objects;

import android.graphics.PointF;

import com.hinterlong.kevin.cs126.freshest.game.OnGameInteract;

/**
 * Objects which can receive calls from {@link OnGameInteract}.
 */
public abstract class InteractiveGameObject extends GameObject implements OnGameInteract {
	public InteractiveGameObject(PointF position, PointF size) {
		super(position, size);
	}
	//todo add helper methods for on touch, on hit wall, etc
}
