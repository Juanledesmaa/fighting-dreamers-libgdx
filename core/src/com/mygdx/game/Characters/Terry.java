package com.mygdx.game.Characters;

import static com.mygdx.game.Global.batch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets.AssetsTerry;
import com.mygdx.game.Player.Player;
import com.mygdx.game.Player.State;

public class Terry {
    public static final float DRAW_WIDTH = 278;
    public static final float DRAW_HEIGHT = 696;
    public static final float FRAME_DURATION = 0.12f;
    public static final float WALK_SPEED = 3;
    public static final float JUMP_SPEED = 3;

    public float stateTime = 0;

    public Vector2 position;
    public Vector2 velocity;

    public int dir = 1;

    // Define a constant for punch duration
    private static final float PUNCH_DURATION = 1.5f; // Adjust the duration as needed

    // Define a variable to keep track of the time spent in punch state
    private float timeInPunchState = 0f;

    public Terry(float x, float y, int dir) {
        AssetsTerry.load();
        position = new Vector2(x, y);
        this.dir = dir;
    }

    public void update(Player player, float delta, float stateTime) {
        this.stateTime = stateTime;

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if (player.state.ground() || player.state == State.walk) {
                // Start the punch animation
                player.state = State.punch;
                // Reset the time spent in punch state
                timeInPunchState = 0f;
            }
        }
//
        // Check if the player is in punch state
        if (player.state == State.punch) {
            // Increment the time spent in punch state
            timeInPunchState += delta;
            // Check if the punch duration has been reached
            if (timeInPunchState >= PUNCH_DURATION) {
                // Transition back to idle state or walk state
                if (player.state == State.punch && player.state.ground()) {
                    player.state = State.idle;
                } else if (player.state == State.punch && player.state == State.walk) {
                    player.state = State.walk;
                }
                // Reset the time spent in punch state
                timeInPunchState = 0f;
            }
        }
    }

    public void render(SpriteBatch batch, Player player) {
        position = player.position;
        dir = player.dir;

        Sprite keyframe = switch (player.state) {
            case idle -> AssetsTerry.idleAnimation.getKeyFrame(stateTime);
            case walk -> AssetsTerry.walkingAnimation.getKeyFrame(stateTime);
            case jumpUp -> AssetsTerry.jumpUp;
            case jumpFall -> AssetsTerry.falling;
            case punch -> AssetsTerry.lightPunch1Animation.getKeyFrame(stateTime);
            default -> AssetsTerry.idle;
        };

        keyframe.setPosition(position.x - Terry.DRAW_WIDTH / 2, position.y + 100);
        keyframe.setScale(2);
        keyframe.setFlip(dir > -1, false);
        keyframe.draw(batch);
    }
}
