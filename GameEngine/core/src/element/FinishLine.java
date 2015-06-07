package element;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Flávio on 07/06/2015.
 */
public class FinishLine extends GameElement {

    public FinishLine(Body body, float width, float height) {
        super(body, FinishLine.class.getSimpleName(), width, height);
    }
}
