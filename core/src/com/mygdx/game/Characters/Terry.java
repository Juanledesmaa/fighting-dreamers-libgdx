package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Terry {

    public static final float WIDTH = 128;
    public static final float HEIGHT = 896;

    public static final float DRAW_WIDTH = 128;
    public static final float DRAW_HEIGHT = 896;
    public static final float WALK_FRAME_DURATION = 0.066f;
    public static final float WALK_SPEED = 3;
    public static final float JUMP_SPEED = 3;

    public boolean isJumping;
    public boolean isWalking;
    public boolean isDucking;
    public boolean isFalling;

    public boolean didDuck;
    public boolean didJump;

    public static float stateTime = 0;

    public Vector2 position;
    public Vector2 velocity;

    public Terry(float x, float y) {
        position = new Vector2(x, y);
    }

    public void update(Body body, float delta, float accelX) {
        position.x = body.getPosition().x;
        position.y = body.getPosition().y;
        velocity = body.getLinearVelocity();

        updateDuckingIfNeeded();
        updateJumpingIfNeeded();
        updateWalkingIfNeeded(accelX);
        updateJumpingFalling();

        body.setLinearVelocity(velocity);
        stateTime+=delta;
    }

    private void updateDuckingIfNeeded() {
        isDucking = false;

        if(didDuck) {
            isDucking = true;
            didDuck = false;
            stateTime = 0;
        }
    }

    private void updateJumpingIfNeeded() {
        isJumping = false;

        if(didJump) {
            isJumping = true;
            didJump = false;
            stateTime = 0;
            velocity.y = JUMP_SPEED;
        }
    }

    private void updateWalkingIfNeeded(float accelX) {
        if (accelX == -1) {
            velocity.x = -WALK_SPEED;
            isWalking = !isJumping && !isFalling;
        } else if (accelX == 1) {
            velocity.x = WALK_SPEED;
            isWalking = !isJumping && !isFalling;
        } else {
            velocity.x = 0;
            isWalking = false;
        }
    }

    private void updateJumpingFalling() {
        if (isJumping) {
            if (velocity.y <= 0) {
                isJumping = false;
                isFalling = true;
                stateTime = 0;
            }
        } else if (isFalling) {
            if (velocity.y >= 0) {
                isFalling = false;
                stateTime = 0;
            }
        }
    }

    public void jump() {
        if(!isJumping) {
            didJump = true;
        }
    }

    public void duck() {
        if (!isJumping) {
            didDuck = true;
        }
    }
}
