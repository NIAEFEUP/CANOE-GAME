package element;

import com.badlogic.gdx.physics.box2d.Body;
import element.GameElement;

/**
 * Represents a rock
 */
public class Rock extends GameElement {
    public Rock(Body body, float width, float height) {
        super(body, Rock.class.getSimpleName(), width, height);
    }
}
