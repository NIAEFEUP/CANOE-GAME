package engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Flávio on 06/06/2015.
 */
public class KeyboardTrackController implements InputProcessor, TrackController{
    private Track track;
    private AtomicInteger rowLeft;
    private AtomicInteger rowRight;

    public KeyboardTrackController(Track track) {
        this.track = track;

        rowLeft = new AtomicInteger(0);
        rowRight = new AtomicInteger(0);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode)
        {
            case Input.Keys.A:
                rowLeft.incrementAndGet();
                break;
            case Input.Keys.D:
                rowRight.incrementAndGet();
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void processInput() {
        while (rowLeft.get() > 0){
            track.getCanoe().rowLeft();
            rowLeft.decrementAndGet();
        }
        while (rowRight.get() > 0) {
            track.getCanoe().rowRight();
            rowRight.decrementAndGet();
        }
    }

    @Override
    public void connect() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void disconnect() {
        Gdx.input.setInputProcessor(null);
    }
}
