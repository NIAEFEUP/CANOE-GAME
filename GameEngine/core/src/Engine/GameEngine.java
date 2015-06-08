package engine;

import com.badlogic.gdx.Gdx;
import server.Server;
import com.badlogic.gdx.Game;

/**
 * Created by Flávio on 08/06/2015.
 */
public class GameEngine extends Game {
    private GameScreen gameScreen;
    private LoginScreen loginScreen;
    private Server server;

    @Override
    public void create() {
        server = new Server();
        new Thread(server).start();
        gameScreen = new GameScreen(this, server);
        loginScreen = new LoginScreen(this, server);
        setScreen(loginScreen);
    }

    public void nextScreen() {
        if (getScreen() == loginScreen)
            setScreen(gameScreen);
        else if (getScreen() == gameScreen){
            server.disconnectAllPlayers();
            setScreen(loginScreen);
        }
    }
}
