package element;

import com.badlogic.gdx.physics.box2d.Body;
import element.GameElement;

/**
 * Created by Flávio on 06/06/2015.
 */
public class Rock extends GameElement {
    public Rock(Body body, float width, float height) {
        super(body, Rock.class.getSimpleName(), width, height);
    }
}
