package engine;

import server.Server;
import com.badlogic.gdx.Game;

/**
 * Manages the application's states.
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

    /**
     * Changes the game's state.
     */
    public void nextScreen() {
        if (getScreen() == loginScreen)
            setScreen(gameScreen);
        else if (getScreen() == gameScreen){
            server.disconnectAllPlayers();
            setScreen(loginScreen);
        }
    }
}
