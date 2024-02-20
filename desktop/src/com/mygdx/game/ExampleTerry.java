
package com.mygdx.game;

import static com.mygdx.game.Global.batch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Assets.AssetsTerry;
import com.mygdx.game.Characters.Terry;
import com.mygdx.game.Player.Player;
import com.mygdx.game.Player.State;

public class ExampleTerry extends ApplicationAdapter {
	static final float worldWidth = 1600, worldHeight = 900;

	Viewport viewport = new FitViewport(worldWidth, worldHeight);
	ShapeRenderer shapeRenderer;

	GameState gameState = new GameState();

	Terry terry;
	Array<Body> arrBodies;

	Texture img;

	private float stateTime;


	public void create () {
		AssetsTerry.load();
		img = new Texture("Backgrounds/stages/figher_background.jpg");
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		stateTime = 0f;

		Vector2 gravity = new Vector2(0, 0);

		arrBodies = new Array<>();

		Gdx.input.setInputProcessor(new InputAdapter() {
			public boolean keyDown (int key) {
				ExampleTerry.this.keyDown(key);
				return true;
			}

			public boolean keyUp (int key) {
				ExampleTerry.this.keyUp(key);
				return true;
			}
		});

		createTerry();
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
		// Update the animation state
		stateTime += Gdx.graphics.getDeltaTime();
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
		batch.begin();
		batch.draw(img, 0, 0);
		drawPlayer(gameState.player);
		batch.end();

		drawShape(gameState.enemyPlayer);
	}

	void drawPlayer (Player player) {
		float x = player.position.x, y = player.position.y, dir = player.dir;
		State state = player.state;

		Sprite keyframe = switch (state) {
			case idle -> AssetsTerry.idleAnimation.getKeyFrame(stateTime);
			case walk -> AssetsTerry.walkingAnimation.getKeyFrame(stateTime);
			case jumpUp -> AssetsTerry.jumpUp;
			case jumpFall -> AssetsTerry.falling;
			default -> AssetsTerry.idle;
		};

		keyframe.setPosition(x - Terry.DRAW_WIDTH / 2, y / 2 + .25f);
		keyframe.setSize(Terry.DRAW_WIDTH, Terry.DRAW_HEIGHT);
		keyframe.setFlip(dir > -1, false);

		keyframe.draw(batch);
	}

	void drawShape (Player player) {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		float x = player.position.x, y = player.position.y, dir = player.dir;
		State state = player.state;

		// Body.
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(x - Player.width / 2, y, Player.width, Player.height);

		// Face.
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(x + 20 * dir, y + 275, 10 * dir, -10);

		// Arm.
		shapeRenderer.setColor(Color.WHITE);
		switch (state) {
			case idle -> shapeRenderer.rect(x - 30 * dir, y + 200, 30 * dir, -100);
			case walk -> shapeRenderer.rect(x - 10 * dir, y + 200, 100 * dir, 30);
			case jumpUp -> shapeRenderer.rect(x - 30 * dir, y + 250, 30 * dir, 100);
			case jumpFall -> shapeRenderer.rect(x - 30 * dir, y + 230, 30 * dir, -30);
		}

		shapeRenderer.end();
	}

	private void createTerry () {
		terry = new Terry(40, 50);
	}

	public void resize (int width, int height) {
		viewport.update(width, height, true);
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
	}

	static class GameState {
		Player player = new Player(new Vector2(200, 0));
		Player enemyPlayer = new Player(new Vector2(worldWidth - Player.width, 0));
	}

	static public void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Example_Terry");
		config.setWindowedMode(1600, 900);
		new Lwjgl3Application(new ExampleTerry(), config);
	}
}
