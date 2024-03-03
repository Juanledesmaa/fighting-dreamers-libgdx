package com.mygdx.game.Characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Player.Player;

public abstract class Character {
	public float stateTime = 0;

	public Vector2 position;
	public Rectangle rectangle;
	public Vector2 velocity;
	public int dir = 1;

	public Character(float x, float y, int dir) {
		position = new Vector2(x, y);
		this.dir = dir;
		this.rectangle = new Rectangle(position.x, position.y, 0, 0);
	}

	public abstract void update(Player player, float delta, float stateTime);

	public abstract void render(SpriteBatch batch, Player player);

	public float getWidth() {
		return rectangle.getWidth();
	}

	public float getHeight() {
		return rectangle.getHeight();
	}
}
