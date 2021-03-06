package element.def;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import element.def.GameElementDef;

/**
 * Rock's body definition.
 */
public class RockDef extends GameElementDef {
    private static final float RADIUS = 0.5f;
    private static final BodyDef bodyDef = initBodyDef();
    private static final FixtureDef fixtureDef = initFixtureDef();

    /**
     * Creates a fixture definition.
     *
     * @return created fixture definition.
     */
    private static FixtureDef initFixtureDef() {
        CircleShape shape = new CircleShape();
        shape.setRadius(RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        return fixtureDef;
    }

    /**
     * Creates a body definition.
     *
     * @return Created body definition.
     */
    private static BodyDef initBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.allowSleep = true;

        return bodyDef;
    }

    public RockDef() {
        super(bodyDef, fixtureDef, RADIUS * 2, RADIUS * 2);
    }
}
