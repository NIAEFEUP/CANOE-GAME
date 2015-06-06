import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import element.GameElement;
import element.Rock;
import element.def.CanoeDef;
import element.def.GameElementDef;

/**
 * Created by Flávio on 06/06/2015.
 */
public class TrackRenderer {
    Track track;

    Sprite canoeSprite;
    Sprite rockSprite;
    Sprite riverSprite;
    Sprite sandBankSprite;
    Sprite marginSprite;

    public TrackRenderer(Track track) {
        this.track = track;

        canoeSprite = new Sprite(new Texture("core/assets/Kayak.png"));

        rockSprite = new Sprite(new Texture("core/assets/Rock.png"));

        sandBankSprite = new Sprite(new Texture("core/assets/Sand.png"));

        riverSprite = new Sprite(new Texture("core/assets/Water.png"));
        riverSprite.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        marginSprite = new Sprite(new Texture("core/assets/Grass.png"));
        marginSprite.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void render(Graphics graphics, SpriteBatch batch) {

        canoeSprite.setSize(track.getCanoe().getWidth(), track.getCanoe().getHeight());
        canoeSprite.setOriginCenter();
        canoeSprite.setRotation((float) (track.getCanoe().getBody().getAngle() * 180 / Math.PI));
        canoeSprite.setPosition(track.getCanoe().getBody().getPosition().x - canoeSprite.getWidth() / 2,
                track.getCanoe().getBody().getPosition().y - canoeSprite.getHeight() / 2);
        canoeSprite.draw(batch);

        for (Rock rock : track.getRocks())
        {
            rockSprite.setSize(rock.getWidth(), rock.getHeight());
            rockSprite.setPosition(rock.getBody().getPosition().x - rockSprite.getWidth() / 2,
                    rock.getBody().getPosition().y - rockSprite.getHeight() / 2);
            rockSprite.draw(batch);
        }


    }
}
