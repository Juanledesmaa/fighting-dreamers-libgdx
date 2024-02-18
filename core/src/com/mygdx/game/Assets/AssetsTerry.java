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
    public static Sprite idle;


    public static Animation<Sprite> idleAnimation;
    public static Animation<Sprite> walkingAnimation;

    public static void load() {
        // Idle
        idleSingleAtlas = new TextureAtlas(Gdx.files.internal("Sprites/terry_bogard/idle/terry_idle_single.txt"));
        idle = idleSingleAtlas.createSprite("Terry Bogard");

        // Idle
        idleAtlas = new TextureAtlas(Gdx.files.internal("Sprites/terry_bogard/idle/terry_idle.txt"));
        idleAnimation = new Animation<Sprite>(0.066f, idleAtlas.createSprites("Terry Bogard"), Animation.PlayMode.LOOP);


        // Walking
        walkingAtlas = new TextureAtlas(Gdx.files.internal("Sprites/terry_bogard/walking/terry_walking.txt"));
        walkingAnimation = new Animation<Sprite>(Terry.WALK_FRAME_DURATION, walkingAtlas.createSprites("Terry Bogard"), Animation.PlayMode.LOOP);
    }

    public static void dispose() {
        idleAtlas.dispose();
        walkingAtlas.dispose();
    }
}
