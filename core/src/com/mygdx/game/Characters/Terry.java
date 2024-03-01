package com.mygdx.game.Characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets.AssetsTerry;
import com.mygdx.game.Player.Player;
import com.mygdx.game.Player.State;

public class Terry {
    public static final float DRAW_WIDTH = 278;
    public static final float DRAW_HEIGHT = 696;
    public static final float FRAME_DURATION = 0.15f;
    public static final float WALK_SPEED = 3;
    public static final float JUMP_SPEED = 3;

    public float stateTime = 0;

    public Vector2 position;
    public Rectangle rectangle;
    public Vector2 velocity;

    public int dir = 1;

    public Terry(float x, float y, int dir) {
        AssetsTerry.load();
        position = new Vector2(x, y);
        this.dir = dir;
        this.rectangle = new Rectangle(position.x, position.y, 0, 0);
    }

    public void update(Player player, float delta, float stateTime) {
        this.stateTime = stateTime;

        // Check if the player is in the punch state and decrement the duration
        if (player.state == State.punch) {
            player.punchAnimationDuration -= delta;
            if (player.punchAnimationDuration <= 0) {
                player.state = State.idle;
                player.punchAnimationDuration = 0;
            }
        }

        if (player.state == State.land) {
            player.landAnimationDuration -= delta;
            if (player.landAnimationDuration <= 0) {
                player.state = State.idle;
                player.landAnimationDuration = 0;
            }
        }
    }

    public void render(SpriteBatch batch, Player player) {
        position = player.position;
        dir = player.dir;
        int yPositionModifier = 100;

        Sprite keyframe = switch (player.state) {
            case idle -> AssetsTerry.idleAnimation.getKeyFrame(stateTime);
            case walk -> AssetsTerry.walkingAnimation.getKeyFrame(stateTime);
            case jumpUp -> AssetsTerry.jumpUp;
            case jumpFall -> AssetsTerry.falling;
            case land -> {
                yPositionModifier = 70;
                yield AssetsTerry.land;
            }
            case punch -> AssetsTerry.lightPunch1Animation.getKeyFrame(stateTime);
//            case punch -> AssetsTerry.punchComboAnimation.getKeyFrame(stateTime);
            default -> AssetsTerry.idle;
        };

        if (player.getTintColor() != null) {
            keyframe.setColor(player.getTintColor()); // Set the tint color for this player
        } else {
            // Reset to default color if no tint color is specified
            keyframe.setColor(Color.SKY);
        }

        keyframe.setPosition(position.x - keyframe.getWidth()/2, position.y + keyframe.getHeight()/2);
        keyframe.setScale(1);
        keyframe.setFlip(dir > -1, false);
        rectangle.width = keyframe.getWidth();
        rectangle.height = keyframe.getHeight();

        keyframe.draw(batch);
    }

    public float getWidth() {
        return  rectangle.getWidth();
    }

    public float getHeight() {
        return  rectangle.getHeight();
    }

    public Rectangle getAccurateRectangle() {
        float spriteX = position.x - getWidth() / 2;
        float spriteY = position.y + getHeight() / 2;
        float spriteWidth = getWidth();
        float spriteHeight = getHeight();
        return new Rectangle(spriteX, spriteY, spriteWidth, spriteHeight);
    }
}
