package element.def;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by Flávio on 19/05/2015.
 */
public class GameElementDef {
    float width;
    float height;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    public GameElementDef(BodyDef bodyDef, FixtureDef fixtureDef, float width, float height) {
        this.bodyDef = bodyDef;
        this.fixtureDef = fixtureDef;
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }
}
