package engine;

import server.Server;
import com.badlogic.gdx.Screen;

/**
 * Assembles the game logic, rendering and controller.
 */
public class GameScreen implements Screen {
    private Track track;
    private GDXTrackRenderer renderer;
    private TrackController controller;
    private GameEngine game;

    public GameScreen(GameEngine game, Server server) {
        this.game = game;
        TrackConfig cfg = new TrackConfig();
        cfg.width = 10f;
        cfg.length = 25f;
        cfg.margin = 5f;
        track = new Track(cfg);

        track.addRock(3, 10);
        track.addRock(4, 15);
        track.addRock(-2, 13);
        track.addRock(2, 15);

        track.addSandBank(-2, 20, 4, 5);
        track.addSandBank(0, 10, 2, 8);

        renderer = new GDXTrackRenderer(track);
        controller = new RemoteTrackController(track, server);
    }

    @Override
    public void show() {
        track.start();
        controller.connect();
    }

    @Override
    public void render(float delta) {
        if (!track.isFinished()) {
            controller.processInput();
            track.update(delta);
            renderer.render();
        }
        else
            game.nextScreen();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        controller.disconnect();
    }

    @Override
    public void dispose() {

    }
}
