package engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import server.Server;

/**
 * Login Screen menu
 */
public class LoginScreen implements Screen {
    private GameEngine game;
    private Server server;
    private SpriteBatch batch;
    private Sprite connectedPlayer;
    private Sprite disconnectedPlayer;
    private Sprite qrCode;
    private Sprite background;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private static final float cameraWidth = 40;
    private static final float cameraHeight = 20;


    public LoginScreen(GameEngine game, Server server) {
        this.game = game;
        this.server = server;

        camera = new OrthographicCamera(cameraWidth, cameraHeight);
        camera.update();
        camera.position.set(cameraWidth / 2, cameraHeight / 2, 0);
        camera.update();
        viewport = new FitViewport(cameraWidth, cameraHeight, camera);
        camera.update();

        batch = new SpriteBatch();

        background = new Sprite(new Texture(Gdx.files.classpath("assets/Background.png")));
        background.setSize(cameraWidth, cameraHeight);
        connectedPlayer = new Sprite(new Texture(Gdx.files.classpath("assets/Connected.png")));
        connectedPlayer.setSize(5, 5);
        disconnectedPlayer = new Sprite(new Texture(Gdx.files.classpath("assets/Disconnected.png")));
        disconnectedPlayer.setSize(5, 5);
        qrCode = new Sprite(new Texture(Gdx.files.local("ServerIP.png")));
        qrCode.setSize(5, 5);
        qrCode.setPosition((cameraWidth - 5) / 2, (cameraHeight - 5) / 2);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (server.getClients().get(Server.LEFT_SIDE - 1) != null && server.getClients().get(Server.RIGHT_SIDE - 1) != null)
            game.nextScreen();

        camera.update();

        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        background.draw(batch);

        if (server.getClients().get(Server.LEFT_SIDE - 1) != null){
            connectedPlayer.setPosition(connectedPlayer.getWidth(), 0);
            connectedPlayer.draw(batch);
        }
        else{
            disconnectedPlayer.setPosition(disconnectedPlayer.getWidth(), 0);
            disconnectedPlayer.draw(batch);
        }

        if (server.getClients().get(Server.RIGHT_SIDE - 1) != null){
            connectedPlayer.setPosition(cameraWidth - 2 * connectedPlayer.getWidth(), 0);
            connectedPlayer.flip(true, false);
            connectedPlayer.draw(batch);
            connectedPlayer.flip(true, false);
        }
        else{
            disconnectedPlayer.setPosition(cameraWidth - 2 * disconnectedPlayer.getWidth(), 0);
            disconnectedPlayer.flip(true, false);
            disconnectedPlayer.draw(batch);
            disconnectedPlayer.flip(true, false);
        }

        qrCode.draw(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
