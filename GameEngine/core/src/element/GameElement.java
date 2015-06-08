package element;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Represents a game element with a physics body.
 */
public class GameElement {
    private Body body;
    private float width;
    private float height;

    public GameElement(Body body, String type, float width, float height)
    {
        this.body = body;
        this.body.setUserData(type);
        this.width = width;
        this.height = height;
    }

    public Body getBody() {
        return body;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
