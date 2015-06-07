package element.def;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import element.def.GameElementDef;

/**
 * Created by Flávio on 06/06/2015.
 */
public class SandBankDef extends GameElementDef {
    private static final BodyDef bodyDef = initBodyDef();

    private static BodyDef initBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.allowSleep = true;
        return bodyDef;
    }

    private static FixtureDef initFixtureDef(float width, float height) {
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(width /2 , height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.shape = shape;

        return fixtureDef;
    }

    public SandBankDef(float width, float height) {
        super(bodyDef, initFixtureDef(width, height), width, height);
    }
}
