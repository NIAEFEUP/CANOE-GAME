package Engine;

import Server.Server;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Flávio on 07/06/2015.
 */
public class GameEngine extends ApplicationAdapter{
    Track track;
    TrackRenderer renderer;
    RemoteTrackController controller;

    @Override
    public void create() {
        TrackConfig cfg = new TrackConfig();
        cfg.width = 10f;
        cfg.length = 30f;
        cfg.margin = 5f;
        track = new Track(cfg);
        renderer = new TrackRenderer(track);
        Server server = new Server();
        new Thread(server).start();

        track.start();
        track.getCanoe().getBody().setLinearVelocity(0f, 0f);

        //controller = new TrackController(track);
        controller = new RemoteTrackController(track, server);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        renderer.resize(width, height);
    }

    @Override
    public void render() {
//        System.out.println(track.getCanoe().getLeftPaddle().getAngle());
//        System.out.println(track.getCanoe().getLeftPaddle().getAngularVelocity());
        track.update(1f / 60f);
        renderer.render();
    }
}
