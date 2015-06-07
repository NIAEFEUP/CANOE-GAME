package Engine;

import Engine.Track;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import element.Rock;
import element.SandBank;

/**
 * Created by Flávio on 06/06/2015.
 */
public class TrackRenderer {
    private Track track;

    private Sprite canoeSprite;
    private Sprite rockSprite;
    private Sprite riverSprite;
    private Sprite sandBankSprite;
    private Sprite marginSprite;
    private Sprite paddleSprite;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float cameraWidth;
    private float cameraHeight;
    private FitViewport viewport;

    public TrackRenderer(Track track) {
        this.track = track;

        batch = new SpriteBatch();

        cameraWidth = 2 * track.getMarginWidth() + track.getRiver().getWidth();
        cameraHeight = track.getRiver().getWidth() * 2;
        camera = new OrthographicCamera(cameraWidth, cameraHeight);
        camera.update();
        camera.position.set(0f, cameraHeight / 2, 0);
        camera.update();
        viewport = new FitViewport(cameraWidth, cameraHeight, camera);

        canoeSprite = new Sprite(new Texture("core/assets/Kayak.png"));

        rockSprite = new Sprite(new Texture("core/assets/Rock.png"));

        sandBankSprite = new Sprite(new Texture("core/assets/Sand.png"));
        sandBankSprite.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        riverSprite = new Sprite(new Texture("core/assets/Water.png"));
        riverSprite.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        marginSprite = new Sprite(new Texture("core/assets/Grass.png"));
        marginSprite.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    public void render() {
        camera.update();

        if (track.getCanoe().getBody().getPosition().y < cameraHeight / 2)
            camera.position.set(0f, cameraHeight / 2, 0);
        else if (track.getCanoe().getBody().getPosition().y < track.getRiver().getHeight() - cameraHeight / 2)
            camera.position.set(0f, track.getCanoe().getBody().getPosition().y, 0);
        else
            camera.position.set(0f, track.getRiver().getHeight() - cameraHeight / 2, 0);

        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        marginSprite.setSize(cameraWidth, track.getRiver().getHeight());
        marginSprite.setU(0f);
        marginSprite.setU2(cameraWidth);
        marginSprite.setV(0f);
        marginSprite.setV2(track.getRiver().getHeight());
        marginSprite.setPosition(-cameraWidth / 2, 0);
        marginSprite.draw(batch);


        riverSprite.setSize(track.getRiver().getWidth(), track.getRiver().getHeight());
        riverSprite.setU(0f);
        riverSprite.setU2(track.getRiver().getWidth());
        riverSprite.setV(0f);
        riverSprite.setV2(track.getRiver().getHeight());
        riverSprite.setPosition(track.getRiver().getBody().getPosition().x - track.getRiver().getWidth() / 2,
                                track.getRiver().getBody().getPosition().y - track.getRiver().getHeight() / 2);
        riverSprite.draw(batch);

        for (SandBank sandBank : track.getSandBanks())
        {
            sandBankSprite.setSize(sandBank.getWidth(), sandBank.getHeight());
            sandBankSprite.setU(0f);
            sandBankSprite.setU2(sandBank.getWidth());
            sandBankSprite.setV(0f);
            sandBankSprite.setV2(sandBank.getHeight());
            sandBankSprite.setPosition(sandBank.getBody().getPosition().x - sandBankSprite.getWidth() / 2,
                    sandBank.getBody().getPosition().y - sandBankSprite.getHeight() / 2);
            sandBankSprite.draw(batch);
        }

        for (Rock rock : track.getRocks())
        {
            rockSprite.setSize(rock.getWidth(), rock.getHeight());
            rockSprite.setPosition(rock.getBody().getPosition().x - rockSprite.getWidth() / 2,
                    rock.getBody().getPosition().y - rockSprite.getHeight() / 2);
            rockSprite.draw(batch);
        }

        canoeSprite.setSize(track.getCanoe().getWidth(), track.getCanoe().getHeight());
        canoeSprite.setOriginCenter();
        canoeSprite.setRotation((float) (track.getCanoe().getBody().getAngle() * 180 / Math.PI));
        canoeSprite.setPosition(track.getCanoe().getBody().getPosition().x - track.getCanoe().getWidth() / 2,
                track.getCanoe().getBody().getPosition().y - track.getCanoe().getHeight() / 2);
        canoeSprite.draw(batch);

        batch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
