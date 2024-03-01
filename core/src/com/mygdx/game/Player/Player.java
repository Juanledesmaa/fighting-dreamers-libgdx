package com.mygdx.game.Player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    // TODO: Refactor this to use a global value, right now taking this from Example.java cause a cyclic dependency.
    public static final float worldWidth = 1600;
    public static final float width = 100;
    public static final float height = 300;
    public static final float walkSpeed = 450;
    public static final float gravity = 1680;
    public static final float jumpVelocityX = 450;
    public static final float jumpVelocityY = 950;

    public float lifeTotal = 100;

    public Vector2 position;
    public Rectangle rectangle;
    public Vector2 velocity = new Vector2();
    public int dir = 1;
    public State state = State.idle;

    public float punchAnimationDuration = 0.15f;
    public boolean didHitPunch = false;
    public float landAnimationDuration = 0.15f;

    private Color tintColor = null;

    // Constructor
    public Player(Vector2 position) {
        this.position = position;
        this.rectangle = new Rectangle(position.x, position.y, 0, 0);
    }

    public void update(float delta, Rectangle boundingRectangle) {
        if (state.ground()) {
            if (state == State.walk) position.x += walkSpeed * delta * dir;
        } else {
            if (velocity.y < 0 && state == State.jumpUp && state != State.jumpFall) state = State.jumpFall;
            velocity.y -= (gravity * delta * 1.2); // Increase gravity factor as needed

            collideX(delta);
            collideY(delta);
            position.add(velocity.x * delta, velocity.y * delta);
        }

        position.x = Math.max(position.x, width / 2);
        position.x = Math.min(position.x, worldWidth - width / 2);

        if (boundingRectangle != null) {
            rectangle = new Rectangle(boundingRectangle.getX(), boundingRectangle.getY(), boundingRectangle.getWidth(), boundingRectangle.getHeight());
        }
    }

    public void update(float delta) {
        // Call the main update method with default values for boundingRectangle
        update(delta, null); // Or set default rectangle if needed
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return rectangle.getWidth();
    }

    public float getHeight() {
        return rectangle.getHeight();
    }

    void collideX (float delta) {
        float x = position.x + velocity.x * delta, y = position.y;
        if (velocity.x > 0) {
            if (x > worldWidth - width / 2) {
                velocity.x = 0;
                position.x = worldWidth - width / 2;
            }
        } else {
            if (x < width / 2) {
                velocity.x = 0;
                position.x = width / 2;
            }
        }
    }

    void collideY (float delta) {
        float x = position.x, y = position.y + velocity.y * delta;
        if (velocity.y > 0) {
            // Moving up, collide above (with other player).
        } else if (y < 0) {
            // Landed.
            velocity.set(0, 0);
            position.y = 0;
            if (state.air() && state != State.punch) {
                state = State.land;
                landAnimationDuration = 0.15f;
            }
        }
    }

    public Color getTintColor() {
        return tintColor;
    }

    public void setTintColor(Color tintColor) {
        this.tintColor = tintColor;
    }
}
