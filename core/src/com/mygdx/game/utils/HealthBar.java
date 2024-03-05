package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HealthBar {
	private float x, y;
	private float width, height;
	private float healthPercentage;
	private float lifeTotal;

	public HealthBar(float lifeTotal, float x, float y, float width, float height) {
		this.lifeTotal = lifeTotal;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.healthPercentage = 1.0f;
	}

	public void setHealthPercentage(float percentage) {
		healthPercentage = Math.max(0, Math.min(1, percentage / 100));
	}

	public void draw(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(x, y, width, height);

		// Draw foreground (health)
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(x, y, width * healthPercentage, height);
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getLifePercentage(float currentLife) {
		return (currentLife / lifeTotal) * 100;
	}
}
