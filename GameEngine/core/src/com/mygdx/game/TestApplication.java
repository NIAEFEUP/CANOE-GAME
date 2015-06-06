package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Flávio on 03/06/2015.
 */
public class TestApplication extends ApplicationAdapter implements InputProcessor {

    OrthographicCamera camera;
    SpriteBatch batch;
    Box2DDebugRenderer debugRenderer;
    World world;
    Body canoe;
    Body river;
    Body rock;
    Body sandBank;

    Sprite canoeSprite;
    Sprite rockSprite;
    Sprite riverSprite;
    Sprite sandBankSprite;
    Sprite grassSprite;

    FitViewport viewport;

    int cameraWidth = 20;
    int cameraHeight = 20;

    int riverLength = 30;
    int riverWidth = 10;


    public TestApplication() {
        super();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        Texture img = new Texture("core/assets/Kayak.png");
        canoeSprite = new Sprite(img);
        canoeSprite.setSize(1, 4);
        canoeSprite.setOriginCenter();

        img = new Texture("core/assets/Rock.png");
        rockSprite = new Sprite(img);
        rockSprite.setSize(1, 1);
        rockSprite.setOriginCenter();

        img = new Texture("core/assets/Water.png");
        img.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        riverSprite = new Sprite(img);
        riverSprite.setSize(1, 1);
        riverSprite.setOriginCenter();

        img = new Texture("core/assets/Sand2.png");
        img.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        sandBankSprite = new Sprite(img);
        sandBankSprite.setSize(1, 1);
        sandBankSprite.setOriginCenter();

        img = new Texture("core/assets/Grass.png");
        img.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        grassSprite = new Sprite(img);
        grassSprite.setSize(1, 1);
        grassSprite.setOriginCenter();
        grassSprite.setSize(cameraWidth, riverLength);
        grassSprite.setU(0);
        grassSprite.setU(cameraWidth);
        grassSprite.setV(0);
        grassSprite.setV(cameraHeight);


        world = new World(new Vector2(0f, 0f), true);

        BodyDef canoeBodyDef = new BodyDef();
        canoeBodyDef.type = BodyDef.BodyType.DynamicBody;
        canoeBodyDef.position.set(cameraWidth / 2, 3);
        canoeBodyDef.linearDamping = 0.2f;
        canoeBodyDef.angularDamping = 0.3f;

        PolygonShape canoeShape = new PolygonShape();
        Vector2 [] canoeShapeVertices = new Vector2[6];
        canoeShapeVertices[0] = new Vector2(0f, -2f);
        canoeShapeVertices[1] = new Vector2(0.5f, -1.5f);
        canoeShapeVertices[2] = new Vector2(0.5f, 1.5f);
        canoeShapeVertices[3] = new Vector2(0f, 2f);
        canoeShapeVertices[4] = new Vector2(-0.5f, 1.5f);
        canoeShapeVertices[5] = new Vector2(-0.5f, -1.5f);

        canoeShape.set(canoeShapeVertices);

        final FixtureDef canoeFixtureDef = new FixtureDef();
        canoeFixtureDef.shape = canoeShape;
        canoeFixtureDef.restitution = 0.0f;
        canoeFixtureDef.density = 10f;

        canoe = world.createBody(canoeBodyDef);
        canoe.createFixture(canoeFixtureDef);

        canoeShape.dispose();

        BodyDef rockBodyDef = new BodyDef();
        rockBodyDef.type = BodyDef.BodyType.StaticBody;
        rockBodyDef.position.set(0.5f + cameraWidth / 2, 10);

        FixtureDef rockFixtureDef = new FixtureDef();
        CircleShape rockShape = new CircleShape();
        rockShape.setRadius(0.5f);
        rockFixtureDef.shape = rockShape;

        rock = world.createBody(rockBodyDef);
        rock.createFixture(rockFixtureDef);

        rockShape.dispose();

        BodyDef riverBodyDef = new BodyDef();
        riverBodyDef.type = BodyDef.BodyType.StaticBody;
        riverBodyDef.position.set(cameraWidth / 2, 0);

        FixtureDef riverFixtureDef = new FixtureDef();
        Vector2 [] riverShapeVertices = new Vector2[4];
        riverShapeVertices[0] = new Vector2(-riverWidth / 2, 0);
        riverShapeVertices[1] = new Vector2(riverWidth / 2, 0);
        riverShapeVertices[2] = new Vector2(riverWidth / 2, riverLength);
        riverShapeVertices[3] = new Vector2(-riverWidth / 2, riverLength);

        ChainShape riverShape = new ChainShape();
        riverShape.createLoop(riverShapeVertices);
        riverFixtureDef.shape = riverShape;
        riverFixtureDef.friction = 0.0f;

        river = world.createBody(riverBodyDef);
        river.createFixture(riverFixtureDef);

        riverShape.dispose();

        BodyDef sandBankBodyDef = new BodyDef();
        sandBankBodyDef.type = BodyDef.BodyType.StaticBody;
        sandBankBodyDef.position.set(cameraWidth / 2 - 2, 15);

        FixtureDef sandBankFixtureDef = new FixtureDef();
        PolygonShape sandBankFixtureShape = new PolygonShape();
        sandBankFixtureShape.setAsBox(2, 4);
        sandBankFixtureDef.shape = sandBankFixtureShape;
        sandBankFixtureDef.isSensor = true;

        sandBank = world.createBody(sandBankBodyDef);
        sandBank.createFixture(sandBankFixtureDef);

        sandBankFixtureShape.dispose();


        camera = new OrthographicCamera(cameraWidth, cameraHeight);
        camera.update();
        camera.position.set(cameraWidth / 2, cameraHeight / 2, 0);
        camera.update();
        viewport = new FitViewport(cameraWidth, cameraHeight, camera);

        debugRenderer = new Box2DDebugRenderer();

        Gdx.input.setInputProcessor(this);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (contact.getFixtureA().getBody() == canoe && contact.getFixtureB().getBody() == sandBank ||
                        contact.getFixtureA().getBody() == sandBank && contact.getFixtureB().getBody() == canoe)
                    canoe.setLinearDamping(canoe.getLinearDamping() + 0.5f);
            }

            @Override
            public void endContact(Contact contact) {
                if (contact.getFixtureA().getBody() == canoe && contact.getFixtureB().getBody() == sandBank ||
                        contact.getFixtureA().getBody() == sandBank && contact.getFixtureB().getBody() == canoe)
                    canoe.setLinearDamping(canoe.getLinearDamping() - 0.5f);
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        rockSprite.setPosition(rock.getPosition().x - rockSprite.getWidth() / 2, rock.getPosition().y - rockSprite.getHeight() / 2);

        riverSprite.setSize(riverWidth, riverLength);
        riverSprite.setPosition(river.getPosition().x - riverSprite.getWidth() / 2, river.getPosition().y);
        riverSprite.setU(0);
        riverSprite.setU2(riverWidth);
        riverSprite.setV(0);
        riverSprite.setV2(riverLength);

        sandBankSprite.setSize(4, 8);
        sandBankSprite.setPosition(sandBank.getPosition().x - sandBankSprite.getWidth() / 2, sandBank.getPosition().y - sandBankSprite.getHeight() / 2);
        sandBankSprite.setU(0);
        sandBankSprite.setU2(4);
        sandBankSprite.setV(0);
        sandBankSprite.setV2(8);
    }

    @Override
    public void render() {

        world.step(1f / 60f, 3, 2);

        if (canoe.getPosition().y < cameraHeight / 2)
            camera.position.set(cameraWidth / 2, cameraHeight / 2, 0);
        else if (canoe.getPosition().y > riverLength - cameraHeight / 2)
            camera.position.set(cameraWidth / 2, riverLength - cameraHeight / 2, 0);
        else
            camera.position.set(cameraWidth / 2, canoe.getPosition().y, 0);
        camera.update();


        canoeSprite.setPosition(canoe.getPosition().x - canoeSprite.getWidth() / 2, canoe.getPosition().y - canoeSprite.getHeight() / 2);
        canoeSprite.setRotation((float) (canoe.getAngle() * 180 / Math.PI));

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            grassSprite.draw(batch);
            riverSprite.draw(batch);
            sandBankSprite.draw(batch);
            canoeSprite.draw(batch);
            rockSprite.draw(batch);
        batch.end();

        //debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {

        Vector2 impulse = canoe.getWorldVector(new Vector2(0f, 1.0f)).cpy();
        Vector2 applicationPoint;

        switch (keycode)
        {
            case Input.Keys.A:
                applicationPoint = canoe.getWorldPoint(new Vector2(-1.0f, 0f)).cpy();
                break;
            case Input.Keys.D:
                applicationPoint = canoe.getWorldPoint(new Vector2(1.0f, 0f)).cpy();
                break;
            default:
                return false;
        }

        canoe.applyLinearImpulse(impulse, applicationPoint, true);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
