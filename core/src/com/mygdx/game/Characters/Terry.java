package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Vector2;


public class Terry {
    public static final float DRAW_WIDTH = 228;
    public static final float DRAW_HEIGHT = 696;
    public static final float FRAME_DURATION = 0.12f;
    public static final float WALK_SPEED = 3;
    public static final float JUMP_SPEED = 3;

    public static float stateTime = 0;

    public Vector2 position;
    public Vector2 velocity;

    public Terry(float x, float y) {
        position = new Vector2(x, y);
    }
}
