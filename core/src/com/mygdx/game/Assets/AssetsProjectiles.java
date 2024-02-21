package com.mygdx.game.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.Characters.Terry;
public class AssetsProjectiles {

    private static final float FRAME_DURATION = 0.12f;

    public static TextureAtlas blueProjectileAtlas;
    public static Animation<Sprite> blueProjectileAnimation;

    public static void load() {

        // Walking
        blueProjectileAtlas = new TextureAtlas(Gdx.files.internal("Sprites/Projectiles/blue_projectile/blue_projectile.atlas"));
        blueProjectileAnimation = new Animation<Sprite>(AssetsProjectiles.FRAME_DURATION, blueProjectileAtlas.createSprites("Terry Bogard"), Animation.PlayMode.LOOP);
    }

    public static void dispose() {
        blueProjectileAtlas.dispose();

    }
}
