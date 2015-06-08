package element.def;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Factory that produces a physics body based on its body definition.
 */
public class GameElementBodyFactory {
    private World world;

    public GameElementBodyFactory(World world) {
        this.world = world;
    }

    public Body build(GameElementDef def) {
        Body body = world.createBody(def.getBodyDef());
        body.createFixture(def.getFixtureDef());
        return body;
    }
}
