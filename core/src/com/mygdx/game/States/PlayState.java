package com.mygdx.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets.AssetsTerry;
import com.mygdx.game.Characters.Terry;
import com.mygdx.game.MyGdxGame;

import static com.mygdx.game.Global.*;

public class PlayState extends State {

    // Private Properties
    private Texture mainFighter;
    Terry terry;

    World oWorld;

    Array<Body> arrBodies;

    Box2DDebugRenderer renderer;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        AssetsTerry.load();

        Vector2 gravity = new Vector2(0, 0);
        oWorld = new World(gravity, true);
        arrBodies = new Array<>();
        renderer = new Box2DDebugRenderer();

        createFloor();
        createTerry();
    }

    private void createFloor() {
        BodyDef bd = new BodyDef();
        bd.position.set(0, .6f);
        bd.type = BodyDef.BodyType.StaticBody;

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, MyGdxGame.WIDTH, 0);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.friction = 1f;

        Body oBody = oWorld.createBody(bd);
        oBody.createFixture(fixDef);
        shape.dispose();
    }

    private void createTerry() {
        terry = new Terry(400, 500);
        BodyDef bd = new BodyDef();
        bd.position.x = terry.position.x;
        bd.position.y = terry.position.y;
        bd.type = BodyDef.BodyType.DynamicBody;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Terry.WIDTH, Terry.HEIGHT);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.restitution = 0;
        fixDef.density = 15;

        Body oBody = oWorld.createBody(bd);
        oBody.createFixture(fixDef);
        oBody.setUserData(terry);

        shape.dispose();
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        float accelX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            accelX = -1;
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            accelX = 1;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            terry.duck();
        }

        if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            terry.jump();
        }

        oWorld.step(dt, 8, 6);
        oWorld.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if (body.getUserData() instanceof Terry) {
                Terry obj = (Terry) body.getUserData();
                obj.update(body, dt, accelX);
            }
        }
    }

    private void drawTerry() {
        Sprite keyframe = AssetsTerry.idle;

        if (terry.isJumping) {
            // TODO: Add the proper ASSETS
//            keyframe = AssetsLearn8.jump;
        } else if (terry.isFalling) {
            // TODO: Add the proper ASSETS
//            keyframe = AssetsLearn8.fall;
        } else if (terry.isWalking) {
            // TODO: Learn which call for sprite animations
            keyframe = AssetsTerry.walkingAnimation.getKeyFrame(terry.stateTime);
        } else if (terry.isDucking) {
            // TODO: Add the proper ASSETS
//            keyframe = AssetsLearn8.duck;
        }

        // TODO: SOLVE WEIRD PHYSICS AT THE BEGINNING
        if (terry.velocity != null && terry.velocity.x < 0) {
            keyframe.setPosition(terry.position.x + Terry.DRAW_WIDTH / 2, terry.position.y - Terry.DRAW_HEIGHT / 2 + .25f);
            keyframe.setSize(-Terry.DRAW_WIDTH, Terry.DRAW_HEIGHT);
        } else {
            keyframe.setPosition(terry.position.x - Terry.DRAW_WIDTH / 2, terry.position.y - Terry.DRAW_HEIGHT / 2 + .25f);
            keyframe.setSize(Terry.DRAW_WIDTH, Terry.DRAW_HEIGHT);
        }

        keyframe.draw(batch);
    }

    @Override
    public void render(SpriteBatch sb) {

        batch.begin();

        drawTerry();

        batch.end();
    }

    @Override
    public void dispose() {
        AssetsTerry.dispose();
        oWorld.dispose();
        renderer.dispose();
    }
}
