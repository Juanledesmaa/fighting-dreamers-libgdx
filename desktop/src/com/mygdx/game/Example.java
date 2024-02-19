
package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Player.Player;
import com.mygdx.game.Player.State;

public class Example extends ApplicationAdapter {
	static final float worldWidth = 1600, worldHeight = 900;

	Viewport viewport = new FitViewport(worldWidth, worldHeight);
	ShapeRenderer shapes;

	GameState gameState = new GameState();

	public void create () {
		shapes = new ShapeRenderer();

		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean keyDown (int key) {
				Example.this.keyDown(key);
				return true;
			}

			public boolean keyUp (int key) {
				Example.this.keyUp(key);
				return true;
			}
		});
	}

	void keyDown (int key) {
	}

	void keyUp (int key) {
		switch (key) {
		case Keys.LEFT -> {
			if (gameState.player.state == State.walk && gameState.player.dir == -1) gameState.player.state = State.idle;
		}
		case Keys.RIGHT -> {
			if (gameState.player.state == State.walk && gameState.player.dir == 1) gameState.player.state = State.idle;
		}
		}
	}

	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			if (gameState.player.state.ground()) {
				gameState.player.state = State.walk;
				gameState.player.dir = -1;
			}
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			if (gameState.player.state.ground()) {
				gameState.player.state = State.walk;
				gameState.player.dir = 1;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (gameState.player.state.ground()) {
				if (gameState.player.state == State.walk) gameState.player.velocity.x = Player.jumpVelocityX * gameState.player.dir;
				gameState.player.velocity.y = Player.jumpVelocityY;
				gameState.player.state = State.jumpUp;
			}
		}

		gameState.player.update(delta);

		ScreenUtils.clear(Color.BLACK);
		shapes.begin(ShapeType.Filled);
		shapes.setColor(Color.DARK_GRAY);
		shapes.rect(0, 0, worldWidth, worldHeight);
		drawPlayer(gameState.player);
		shapes.end();
	}

	void drawPlayer (Player player) {
		float x = player.position.x, y = player.position.y, dir = player.dir;
		State state = player.state;

		// Body.
		shapes.setColor(Color.RED);
		shapes.rect(x - Player.width / 2, y, Player.width, Player.height);

		// Face.
		shapes.setColor(Color.GREEN);
		shapes.rect(x + 20 * dir, y + 275, 10 * dir, -10);

		// Arm.
		shapes.setColor(Color.WHITE);
		switch (state) {
		case idle -> shapes.rect(x - 30 * dir, y + 200, 30 * dir, -100);
		case walk -> shapes.rect(x - 10 * dir, y + 200, 100 * dir, 30);
		case jumpUp -> shapes.rect(x - 30 * dir, y + 250, 30 * dir, 100);
		case jumpFall -> shapes.rect(x - 30 * dir, y + 230, 30 * dir, -30);
		}
	}

	public void resize (int width, int height) {
		viewport.update(width, height, true);
		shapes.setProjectionMatrix(viewport.getCamera().combined);
	}

	static class GameState {
		Player player = new Player();
	}

	static public void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Example");
		config.setWindowedMode(1600, 900);
		new Lwjgl3Application(new Example(), config);
	}
}
