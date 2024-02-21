package com.mygdx.game.Projectiles;

import static com.mygdx.game.Global.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets.AssetsProjectiles;
import com.mygdx.game.Characters.Terry;

public class BlueProjectile {

	public static  final int SPEED = 1000;
	public static final float DRAW_WIDTH = 96;
	public static final float DRAW_HEIGHT = 64;
	public static final float FRAME_DURATION = 0.12f;
	public static float COOLDOWN = 0.5f; // Set your desired cooldown time in seconds
	public float stateTime = 0;

	public boolean remove = false;

	public Vector2 position;

	public int dir = 1;

	private float timeSinceLastSpawn = 0f;


	public BlueProjectile(float x, float y, int dir) {
		AssetsProjectiles.load();
		this.position = new Vector2(x, y);
		this.dir = dir;
	}

	public void update(float deltaTime, float stateTime) {
		this.stateTime = stateTime;
		position.x += SPEED * deltaTime;

		if (position.x >= Gdx.graphics.getWidth()) {
			remove = true;
		}
	}

	public void render (SpriteBatch batch) {
		Sprite keyframe = AssetsProjectiles.blueProjectileAnimation.getKeyFrame(stateTime, true);
		keyframe.setPosition(position.x, position.y);
		keyframe.setScale(2);
		keyframe.setFlip(dir > -1, false);

		keyframe.draw(batch);
	}


}