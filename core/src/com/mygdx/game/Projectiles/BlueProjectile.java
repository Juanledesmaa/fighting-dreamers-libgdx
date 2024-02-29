package com.mygdx.game.Projectiles;

import static com.mygdx.game.Global.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets.AssetsProjectiles;

public class BlueProjectile {

	public static  final int SPEED = 1000;
	public static final float DRAW_WIDTH = 96;
	public static final float DRAW_HEIGHT = 64;
	public static final float FRAME_DURATION = 0.12f;
	public static float COOLDOWN = 0.5f; // Set your desired cooldown time in seconds
	public float stateTime = 0;

	public boolean remove = false;

	public Vector2 position;
	public Rectangle rectangle;

	public int dir = 1;

	private float timeSinceLastSpawn = 0f;


	public BlueProjectile(float x, float y, int dir) {
		AssetsProjectiles.load();
		this.position = new Vector2(x, y);
		this.dir = dir;
		this.rectangle = new Rectangle(position.x, position.y, 0, 0);
	}

	public void update(float deltaTime, float stateTime) {
		update(deltaTime, stateTime, null);
	}

	public void update(float delta, float stateTime, Rectangle boundingRectangle) {
		this.stateTime = stateTime;
		position.x += SPEED * delta;

		if (boundingRectangle != null) {
			rectangle = new Rectangle(getX(), getY(), boundingRectangle.getWidth(), boundingRectangle.getHeight());
		}

		if (position.x >= Gdx.graphics.getWidth()) {
			remove = true;
		}
	}

	public void render (SpriteBatch batch) {
		Sprite keyframe = AssetsProjectiles.blueProjectileAnimation.getKeyFrame(stateTime, true);
		keyframe.setPosition(position.x, position.y);
		keyframe.setScale(2);
		rectangle = new Rectangle(getX(), getY(), keyframe.getWidth(), keyframe.getHeight());
		keyframe.setFlip(dir > -1, false);

		keyframe.draw(batch);
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return rectangle.getWidth();
	}

	public float getHeight() {
		return rectangle.getHeight();
	}


}