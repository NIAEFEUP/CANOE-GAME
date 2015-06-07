package Engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Flávio on 07/06/2015.
 */
public class GameEngine extends ApplicationAdapter{
    Track track;
    TrackRenderer renderer;

    @Override
    public void create() {
        TrackConfig cfg = new TrackConfig();
        cfg.width = 10f;
        cfg.length = 30f;
        cfg.margin = 5f;
        track = new Track(cfg);
        renderer = new TrackRenderer(track);
        track.addSandBank(0, 10, 5, 10);

        track.start();
        track.getCanoe().getBody().setLinearVelocity(0f, 10f);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        renderer.resize(width, height);
    }

    @Override
    public void render() {
        track.update(1/60f);
        renderer.render();
    }
}
