package Engine;


import java.util.Observable;
import java.util.Observer;

import Server.Server;


/**
 *
 */
public class RemoteTrackController implements Observer{
    Track track;

    public RemoteTrackController(Track track, Server server){
        this.track = track;
        server.addObserver(this);
        track.getCanoe().addObserver(server);
    }

    @Override
    public void update(Observable o, Object arg) {
        int playerNr = (int) arg;
        switch (playerNr){
            case Server.LEFT_SIDE:
                track.getCanoe().rowLeft();
                break;
            case Server.RIGHT_SIDE:
                track.getCanoe().rowRight();
                break;
        }
    }
}
