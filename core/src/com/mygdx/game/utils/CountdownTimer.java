package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Global;

public class CountdownTimer {
	private long startTime;
	private long countdownDurationMillis;
	private boolean isRunning;
	private BitmapFont font;
	private SpriteBatch batch;
	private GlyphLayout glyphLayout;

	public CountdownTimer(long durationMillis, BitmapFont font, SpriteBatch batch) {
		countdownDurationMillis = durationMillis;
		isRunning = false;
		this.font = font;
		this.batch = batch;
		this.glyphLayout = new GlyphLayout();
	}

	public void start() {
		startTime = TimeUtils.millis();
		isRunning = true;
	}

	public void stop() {
		isRunning = false;
	}

	public void update() {
		if (isRunning) {
			long elapsedTime = TimeUtils.timeSinceMillis(startTime);
			long remainingTime = countdownDurationMillis - elapsedTime;
			if (remainingTime <= 0) {
				isRunning = false;
				remainingTime = 0;
			}

			CharSequence text = String.valueOf(remainingTime / 1000);
			glyphLayout.setText(font, text);

			float x = (Global.worldWidth - glyphLayout.width) / 2;
			float y = (Global.worldHeight - glyphLayout.height * 2);
			font.draw(batch, glyphLayout, x, y);

		}
	}

	public boolean isRunning() {
		return isRunning;
	}
}
