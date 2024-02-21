package com.mygdx.game.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Characters.Terry;

public class AssetsTerry {

    public static TextureAtlas idleAtlas;
    public static TextureAtlas walkingAtlas;
    public static TextureAtlas idleSingleAtlas;
    public static TextureAtlas jumpUpAtlas;
    public static TextureAtlas fallingAtlas;
    // Punches
    public static TextureAtlas lightPunch1Atlas;
    public static Sprite idle;
    public static Sprite jumpUp;
    public static Sprite falling;
    public static Animation<Sprite> idleAnimation;
    public static Animation<Sprite> walkingAnimation;
    public static Animation<Sprite> lightPunch1Animation;

    public static void load() {
        // Idle
        idleSingleAtlas = new TextureAtlas(Gdx.files.internal("Sprites/terry_bogard/idle/terry_idle_single.txt"));
        idle = idleSingleAtlas.createSprite("Terry Bogard");

        // Idle
        idleAtlas = new TextureAtlas(Gdx.files.internal("Sprites/terry_bogard/idle/terry_idle.txt"));
        idleAnimation = new Animation<Sprite>(Terry.FRAME_DURATION, idleAtlas.createSprites("Terry Bogard"), Animation.PlayMode.LOOP);

        // Walking
        walkingAtlas = new TextureAtlas(Gdx.files.internal("Sprites/terry_bogard/walking/terry_walking.txt"));
        walkingAnimation = new Animation<Sprite>(Terry.FRAME_DURATION, walkingAtlas.createSprites("Terry Bogard"), Animation.PlayMode.LOOP);

        // Jump Up
        jumpUpAtlas = new TextureAtlas(Gdx.files.internal("Sprites/terry_bogard/JumpUp/jump_up.txt"));
        jumpUp = jumpUpAtlas.createSprite("Terry Bogard");

        // Falling
        fallingAtlas = new TextureAtlas(Gdx.files.internal("Sprites/terry_bogard/falling/falling.txt"));
        falling = fallingAtlas.createSprite("Terry Bogard");

        // Punches
        lightPunch1Atlas = new TextureAtlas(Gdx.files.internal("Sprites/terry_bogard/punch/punch.atlas"));
        lightPunch1Animation = new Animation<Sprite>(Terry.FRAME_DURATION, lightPunch1Atlas.createSprites("Terry Bogard"), Animation.PlayMode.LOOP);

    }

    public static void dispose() {
        idleAtlas.dispose();
        walkingAtlas.dispose();
        idleSingleAtlas.dispose();
        jumpUpAtlas.dispose();
        fallingAtlas.dispose();
        lightPunch1Atlas.dispose();
    }
}
