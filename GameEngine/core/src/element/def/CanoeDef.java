package element.def;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Flávio on 19/05/2015.
 */
public class CanoeDef extends GameElementDef {
    private static final float WIDTH = 1.0f;
    private static final float HEIGHT = 4.0f;
    private static final BodyDef bodyDef = initBodyDef();
    private static final FixtureDef fixtureDef = initFixtureDef();

    public static final float damping = 0.2f;

    public CanoeDef() {
        super(bodyDef, fixtureDef, WIDTH, HEIGHT);
    }

    private static FixtureDef initFixtureDef() {
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[] {new Vector2(0, -HEIGHT / 2),
                new Vector2(WIDTH/2, -HEIGHT / 2 * 0.75f),
                new Vector2(WIDTH/2, HEIGHT / 2 * 0.75f),
                new Vector2(0, HEIGHT / 2),
                new Vector2(-WIDTH/2, HEIGHT / 2 * 0.75f),
                new Vector2(-WIDTH/2, -HEIGHT / 2 *0.75f)});
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 50f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.0f;

        return fixtureDef;
    }

    private static BodyDef initBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.allowSleep = false;
        bodyDef.linearDamping = damping;
        bodyDef.linearDamping = damping;

        return bodyDef;
    }
}
