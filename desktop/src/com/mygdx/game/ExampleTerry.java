
package com.mygdx.game;

import static com.mygdx.game.Global.batch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
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
	ShapeRenderer shapes;

	GameState gameState = new GameState();

	Terry terry;

	World oWorld;

	Array<Body> arrBodies;

	Box2DDebugRenderer renderer;

	public void create () {
		AssetsTerry.load();
		shapes = new ShapeRenderer();
		batch = new SpriteBatch();

		Vector2 gravity = new Vector2(0, 0);
		oWorld = new World(gravity, true);
		arrBodies = new Array<>();
		renderer = new Box2DDebugRenderer();

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
		batch.begin();
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

		oWorld.step(delta, 8, 6);
		oWorld.getBodies(arrBodies);

		ScreenUtils.clear(Color.BLACK);
		shapes.begin(ShapeType.Filled);
		shapes.setColor(Color.DARK_GRAY);
		shapes.rect(0, 0, worldWidth, worldHeight);
		drawPlayer(gameState.player);
		shapes.end();
		batch.end();
	}

	void drawPlayer (Player player) {
		float x = player.position.x, y = player.position.y, dir = player.dir;
		State state = player.state;

		Sprite keyframe = AssetsTerry.idle;

		switch (state) {
			case idle:
				keyframe = AssetsTerry.idle;
				break;
			case walk:
				keyframe = AssetsTerry.walkingAnimation.getKeyFrame(0);
				break;
			case jumpUp:
				break;
			case jumpFall:
				break;
		}

		keyframe.draw(batch);
	}

	private void createTerry() {
		terry = new Terry(40, 50);
		BodyDef bd = new BodyDef();
		bd.position.x = terry.position.x;
		bd.position.y = terry.position.y;
		bd.type = BodyDef.BodyType.DynamicBody;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Terry.WIDTH, Terry.HEIGHT);

		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = shape;
		fixDef.restitution = 0;
		fixDef.density = 15;

		Body oBody = oWorld.createBody(bd);
		oBody.createFixture(fixDef);
		oBody.setUserData(terry);

		shape.dispose();
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
		config.setTitle("Example_Terry");
		config.setWindowedMode(1600, 900);
		new Lwjgl3Application(new ExampleTerry(), config);
	}
}
