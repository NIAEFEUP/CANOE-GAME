import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.GameElementDef;

/**
 * Created by Flávio on 19/05/2015.
 */
public class CanoeDef extends GameElementDef {
    private static BodyDef bodyDef;
    private static FixtureDef fixtureDef;
    private static final float WIDTH = 1.0f;
    private static final float HEIGHT = 4.0f;

    static {
        initFixtureDef();
        initBodyDef();
    }

    private static void initFixtureDef() {
        PolygonShape shape = new PolygonShape();
        shape.set(new Vector2[] {new Vector2(0, -HEIGHT / 2),
                new Vector2(WIDTH/2, -HEIGHT / 2 * 0.75f),
                new Vector2(WIDTH/2, HEIGHT / 2 * 0.75f),
                new Vector2(0, HEIGHT / 2),
                new Vector2(-WIDTH/2, HEIGHT / 2 * 0.75f),
                new Vector2(-WIDTH/2, -HEIGHT / 2 *0.75f)});
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 50f;
        fixtureDef.friction = 0.2f;
        fixtureDef.restitution = 0.0f;
    }

    private static void initBodyDef() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.allowSleep = false;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }
}
