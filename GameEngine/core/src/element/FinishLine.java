package element;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Represents the finish line.
 */
public class FinishLine extends GameElement {

    public FinishLine(Body body, float width, float height) {
        super(body, FinishLine.class.getSimpleName(), width, height);
    }
}
