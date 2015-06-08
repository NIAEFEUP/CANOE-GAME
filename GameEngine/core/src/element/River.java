package element;

import com.badlogic.gdx.physics.box2d.*;
import element.GameElement;

/**
 * Represents the river.
 */
public class River extends GameElement {
    public River(Body body, float width, float length) {
        super(body, GameElement.class.getSimpleName(), width, length);
    }
}