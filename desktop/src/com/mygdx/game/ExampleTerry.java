
package com.mygdx.game;

import static com.mygdx.game.Global.batch;
import static com.mygdx.game.Global.polygonBatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Characters.SpineBoy;
import com.mygdx.game.Characters.Terry;
import com.mygdx.game.Player.Player;
import com.mygdx.game.Player.State;
import com.mygdx.game.Projectiles.BlueProjectile;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.sound.GeneralSounds;
import com.mygdx.game.utils.CountdownTimer;
import com.mygdx.game.utils.HealthBar;

import java.util.ArrayList;
import java.util.Iterator;

public class ExampleTerry extends ApplicationAdapter {
	static final float worldWidth = 1600, worldHeight = 900;
	Viewport viewport = new FitViewport(worldWidth, worldHeight);
	ShapeRenderer shapeRenderer;
	GameState gameState = new GameState();
	Terry terry;
	Terry enemy;
	SpineBoy spineBoy;
	Texture img;
	ArrayList<BlueProjectile> blueProjectiles;
	private float timeSinceLastBlueProjectile = 0f;
	private BitmapFont font;
	private float stateTime;
	private GlyphLayout glyphLayout;
	private CountdownTimer countdownTimer;
	private HealthBar healthBar;
	private HealthBar enemyHealthBar;

	public void create () {
		img = new Texture("Backgrounds/stages/figher_background.jpg");
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		polygonBatch = new PolygonSpriteBatch();
		stateTime = 0f;
		blueProjectiles = new ArrayList<>();
		font = new BitmapFont(Gdx.files.internal("Fonts/presstart.fnt"));
		setUpFont();
		createTerry();
		createEnemy();
//		createSpineBoy();
		setUpEnemyGameState();
		setUpCountDownTimer();
		createHealthBars();

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

		GeneralSounds.initializeSounds();
		GeneralSounds.BACKGROUND_MUSIC.playMusic(true);
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

			case Keys.Q -> {
			// TODO: Handle inputs here
				if (gameState.player.state.ground()) {
					gameState.player.state = State.punch;
					gameState.player.punchAnimationDuration = 0.15f;
				}
			}
		}
	}

	public void render () {
		Rectangle projectileBounds = null;

		float delta = Gdx.graphics.getDeltaTime();
		stateTime += delta;
		timeSinceLastBlueProjectile += delta;

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			if (gameState.player.state.ground()) {
				gameState.player.state = State.walk;
				gameState.player.dir = -1;
			}
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			if (gameState.player.state.ground()) {
				gameState.player.state = State.walk;
				gameState.player.dir = 1;
				gameState.playerSpineboy.state = State.walk;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (gameState.player.state.ground()) {
				if (gameState.player.state == State.walk) gameState.player.velocity.x = Player.jumpVelocityX * gameState.player.dir;
				gameState.player.velocity.y = Player.jumpVelocityY;
				gameState.player.state = State.jumpUp;
			}
		}

		gameState.player.update(delta, terry.getAccurateRectangle());
		gameState.enemyPlayer.update(delta, enemy.getAccurateRectangle());
		gameState.playerSpineboy.update(delta);

//		spineBoy.animationState.update(delta);
//		spineBoy.animationState.apply(spineBoy.skeleton);
//		spineBoy.skeleton.updateWorldTransform();

		batch.begin();
		batch.draw(img, 0, 0);

		// Draw text at position (x,y)
		drawText("Life: " + gameState.player.lifeTotal, 50, 850, Color.WHITE);
		drawText("Life: " + gameState.enemyPlayer.lifeTotal, worldWidth - 50, 850, Color.WHITE);

		healthBar.setHealthPercentage(healthBar.getLifePercentage(gameState.player.lifeTotal));
		enemyHealthBar.setHealthPercentage(enemyHealthBar.getLifePercentage(gameState.enemyPlayer.lifeTotal));

		drawProjectile(delta);
		handlePunchCollision();

		terry.update(gameState.player, delta, stateTime);
		terry.render(batch, gameState.player);

		enemy.update(gameState.enemyPlayer, delta, stateTime);
		enemy.render(batch, gameState.enemyPlayer);

//		spineBoy.update(gameState.playerSpineboy, delta, stateTime);
//		spineBoy.render(batch, gameState.playerSpineboy);
		countdownTimer.update();
		batch.end();

		// For Tests purposes only.
		renderShape(gameState.player.rectangle, Color.RED);
		renderShape(gameState.enemyPlayer.rectangle, Color.BLUE);
		renderHealthBars();
	}

	public void resize (int width, int height) {
		viewport.update(width, height, true);
		shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
	}

	static class GameState {
		Player player = new Player(new Vector2(200, 0));
		Player enemyPlayer = new Player(new Vector2(worldWidth - Player.width, 0));
		Player playerSpineboy = new Player(new Vector2(800, 0));
	}

	static public void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Example_Terry");
		config.setWindowedMode(1600, 900);
		new Lwjgl3Application(new ExampleTerry(), config);
	}

	// Private Methods

	private void createTerry () {
		terry = new Terry(0, 0, 1);
	}

//	private void createSpineBoy () {
//		spineBoy = new SpineBoy(0, 0, 1);
//		spineBoy.animationState.setAnimation(0, "idle", true);
//	}

	private void createEnemy () {
		enemy = new Terry(0, 0, -1);
	}

	private void createHealthBars() {
		healthBar = new HealthBar(gameState.player.lifeTotal, 50, Global.worldHeight - 130, 500, 30);
		enemyHealthBar = new HealthBar(gameState.enemyPlayer.lifeTotal,Global.worldWidth - 550, Global.worldHeight - 130, 500, 30);
	}

	private void setUpCountDownTimer() {
		countdownTimer = new CountdownTimer(100000, font, batch);
		countdownTimer.start();
	}

	private void setUpFont() {
		font.getData().setScale(1);
		glyphLayout = new GlyphLayout();
	}

	private void drawText(String text, float x, float y, Color color) {
		font.setColor(color);
		glyphLayout.setText(font, text);
		float textWidth = glyphLayout.width;
		float adjustedX = x;
		if (x + textWidth >= worldWidth) {
			adjustedX = x - textWidth;
		}
		font.draw(batch, text, adjustedX, y);
	}
	// TODO: Refactor into something more generic
	private void drawProjectile(float delta) {
		Rectangle projectileBounds = null;
		if (Gdx.input.isKeyPressed(Keys.R) && timeSinceLastBlueProjectile >= BlueProjectile.COOLDOWN) {
			if (gameState.player.state.ground()) {
				blueProjectiles.add(new BlueProjectile(gameState.player.position.x, (gameState.player.position.y + 200), gameState.player.dir));
				timeSinceLastBlueProjectile = 0f;
				GeneralSounds.BLUE_PROJECTILE.play(false);
			}
		}

		Iterator<BlueProjectile> iterator = blueProjectiles.iterator();
		while (iterator.hasNext()) {
			BlueProjectile blueProjectile = iterator.next();
			blueProjectile.update(delta, stateTime);
			projectileBounds = new Rectangle(blueProjectile.getX(), blueProjectile.getY(), blueProjectile.getWidth(), blueProjectile.getHeight());

			if (projectileBounds.overlaps(gameState.enemyPlayer.rectangle)) {
				gameState.enemyPlayer.lifeTotal -= BlueProjectile.damage;
				gameState.enemyPlayer.damageAnimationDuration = 0.30f;
				gameState.enemyPlayer.state = State.lightDamage;

				iterator.remove(); // Remove projectile safely
				GeneralSounds.SINGLE_HIT_1.play(false);

				// Exit the loop early as the collision is detected
				break;
			}
		}

		// Render remaining projectiles
		for (BlueProjectile blueProjectile : blueProjectiles) {
			blueProjectile.render(batch);
		}
	}

	// TODO: Refactor this into a `PunchHandler`
	private void handlePunchCollision() {
		// Check for punch collision and ensure it triggers only once
		if (!gameState.player.didHitPunch && gameState.player.rectangle.overlaps(gameState.enemyPlayer.rectangle)
				&& gameState.player.state == State.punch) {
			gameState.enemyPlayer.lifeTotal -= 10;
			gameState.enemyPlayer.damageAnimationDuration = 0.30f;
			gameState.enemyPlayer.state = State.lightDamage;
			GeneralSounds.SINGLE_HIT_2.play(false);
			gameState.player.didHitPunch = true;
		}

		// Reset the flag if players are no longer in contact or the punch action is completed
		if (!gameState.player.rectangle.overlaps(gameState.enemyPlayer.rectangle)
				|| gameState.player.state != State.punch) {
			gameState.player.didHitPunch = false;
		}
	}

	private void setUpEnemyGameState () {
		gameState.enemyPlayer.state = State.idle;
		gameState.enemyPlayer.setTintColor(Color.BLUE);
		gameState.enemyPlayer.dir = -1;
	}

	private void renderHealthBars() {
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		healthBar.draw(shapeRenderer);
		enemyHealthBar.draw(shapeRenderer);
		shapeRenderer.end();
	}

	// FOR TEST PURPOSES ONLY
	private void renderShape(Rectangle rectangle, Color color) {
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(color);
		shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.getWidth(), rectangle.getHeight());
		shapeRenderer.end();
	}
}
