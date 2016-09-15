package engine;


import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

import server.Server;


/**
 * Establishes the connection between the inputs received from the server and the game.
 */
public class RemoteTrackController implements Observer, TrackController{
    private Track track;
    private AtomicInteger rowLeft;
    private AtomicInteger rowRight;
    private Server server;

    public RemoteTrackController(Track track, Server server){
        this.track = track;
        this.server = server;
        rowLeft = new AtomicInteger(0);
        rowRight = new AtomicInteger(0);
    }

    @Override
    public void update(Observable o, Object arg) {
        int playerNr = (Integer) arg;
        switch (playerNr){
            case Server.LEFT_SIDE:
                rowLeft.incrementAndGet();
                break;
            case Server.RIGHT_SIDE:
                rowRight.incrementAndGet();
                break;
        }
    }

    @Override
    public void processInput() {
        while (rowLeft.get() > 0) {
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
        server.addObserver(this);
        track.getCanoe().addObserver(server);
    }

    @Override
    public void disconnect() {
        server.deleteObserver(this);
        track.getCanoe().removeObserver(server);
    }
}
