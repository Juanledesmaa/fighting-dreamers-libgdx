package com.mygdx.game.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

public class AssetsSpineBoy {
	public static TextureAtlas spineBoyAtlas;
	public static SkeletonJson spineBoyJson;
	public static SkeletonData spineBoySkeletonData;
	public static AnimationStateData spineBoyAnimationData;
	public static void load() {
		// Idle
		spineBoyAtlas = new TextureAtlas(Gdx.files.internal("spine/SpineBoy/spineboy.atlas"));
		spineBoyJson = new SkeletonJson(spineBoyAtlas);
		spineBoySkeletonData = spineBoyJson.readSkeletonData(Gdx.files.internal("spine/SpineBoy/spineboy-ess.json"));
		spineBoyAnimationData = new AnimationStateData(spineBoySkeletonData);
	}

	public static void dispose() {
		spineBoyAtlas.dispose();
	}
}
