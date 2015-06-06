package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MyGdxGame extends ApplicationAdapter{
	SpriteBatch batch;
	Sprite sprite;
	Texture img;
	World world;
	Body body;
	Body bodyEdgeScreen;

	Matrix4 debugMatrix;
	OrthographicCamera camera;

	Box2DDebugRenderer debugRenderer;

	float PIXELS_TO_METERS = 100f;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("core/assets/badlogic.jpg");

		// Create two identical sprites slightly offset from each other vertically
		sprite = new Sprite(img);
		sprite.setBounds(50, 50, 200, 200);
		sprite.setOriginCenter();

		world = new World(new Vector2(0f, 0f),true);

		// Sprite1's Physics body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((sprite.getX() + sprite.getOriginX()) / PIXELS_TO_METERS,
				(sprite.getY() + sprite.getOriginY()) / PIXELS_TO_METERS);

		body = world.createBody(bodyDef);

		// Both bodies have identical shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(100 / PIXELS_TO_METERS, 100 / PIXELS_TO_METERS);

		// Sprite1
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.friction = 0.0f;
		fixtureDef.density = 0.1f;
		fixtureDef.restitution = 0.5f;


		body.createFixture(fixtureDef);
		body.setBullet(true);
		//body.applyLinearImpulse(0.0f, 0.5f, body.getLocalCenter().x + 1, body.getLocalCenter().y, true);

		shape.dispose();

		// Now the physics body of the bottom edge of the screen
		BodyDef bodyDef3 = new BodyDef();
		bodyDef3.type = BodyDef.BodyType.StaticBody;
		float w = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
		float h = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

		bodyDef3.position.set(0,0);
		FixtureDef fixtureDef3 = new FixtureDef();

	//	EdgeShape edgeShape = new EdgeShape();
		Vector2 [] wallVertices = new Vector2[4];
		wallVertices[0] = new Vector2(0, 0);
		wallVertices[1] = new Vector2(w, 0);
		wallVertices[2] = new Vector2(w, h);
		wallVertices[3] = new Vector2(0, h);

		ChainShape wall = new ChainShape();
		wall.createLoop(wallVertices);
	//	edgeShape.set(-w / 2, -h / 2, w / 2, -h / 2);
		fixtureDef3.shape = wall;
		fixtureDef3.friction = 0.0f;


		bodyEdgeScreen = world.createBody(bodyDef3);
		bodyEdgeScreen.createFixture(fixtureDef3);
	//	edgeShape.dispose();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() + 100,Gdx.graphics.
				getHeight() + 100);
		camera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		camera.update();
		debugMatrix = camera.combined.cpy().scl(PIXELS_TO_METERS);
		debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render() {
		camera.update();
		// Step the physics simulation forward at a rate of 60hz
		world.step(1f / 60f, 6, 2);

		sprite.setPosition((body.getPosition().x * PIXELS_TO_METERS) - sprite.getOriginX(),
				(body.getPosition().y * PIXELS_TO_METERS) - sprite.getOriginY());

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	 //	FitViewport viewport = new FitViewport(10000, 10000, camera);
	//	viewport.apply();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		batch.end();

		//Gdx.gl.glViewport(100, 100, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		debugRenderer.render(world, debugMatrix);
	}

	@Override
	public void dispose() {
		img.dispose();
		world.dispose();
	}


}
