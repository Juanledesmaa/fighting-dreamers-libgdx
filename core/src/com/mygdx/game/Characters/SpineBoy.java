package com.mygdx.game.Characters;

import static com.mygdx.game.Global.batch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.mygdx.game.Assets.AssetsSpineBoy;
import com.mygdx.game.Assets.AssetsTerry;
import com.mygdx.game.Player.Player;

public class SpineBoy extends Character {

	public SkeletonRenderer skeletonRenderer = new SkeletonRenderer();
	public Skeleton skeleton;
	public AnimationState animationState;

	public SpineBoy(float x, float y, int dir) {
		super(x, y, dir);
		AssetsSpineBoy.load();
		skeleton = new Skeleton(AssetsSpineBoy.spineBoySkeletonData);
		animationState = new AnimationState(AssetsSpineBoy.spineBoyAnimationData);
	}

	/**
	 * @param player
	 * @param delta
	 * @param stateTime
	 */
	@Override
	public void update(Player player, float delta, float stateTime) {
		this.stateTime = stateTime;
	}

	/**
	 * @param batch
	 * @param player
	 */
	@Override
	public void render(SpriteBatch batch, Player player) {
		position = player.position;
		dir = player.dir;

		switch (player.state) {
			case idle, jumpUp, jumpFall, punch, land, lightDamage -> {
				break;
			}
			case walk -> {
				animationState.setAnimation(0, "walk", true);
			}
			default -> throw new IllegalStateException("Unexpected value: " + player.state);
		}

		skeleton.getRootBone().setPosition(position.x - rectangle.getWidth()/2, position.y + rectangle.getHeight()/2);
		skeleton.setScale(0.5f,0.5f);
//		keyframe.setFlip(dir > -1, false);
//		rectangle.width = keyframe.getWidth();
//		rectangle.height = keyframe.getHeight();

		skeletonRenderer.draw(batch, skeleton);
	}

	public Rectangle getAccurateRectangle() {
		float spriteX = position.x - getWidth() / 2;
		float spriteY = position.y + getHeight() / 2;
		float spriteWidth = getWidth();
		float spriteHeight = getHeight();
		return new Rectangle(spriteX, spriteY, spriteWidth, spriteHeight);
	}
}

