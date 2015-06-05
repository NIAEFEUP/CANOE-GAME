package com.example.duarte.canoefortwo;

import com.example.duarte.canoefortwo.network.ConnectionBridge;

/**
 * Created by Duarte on 03/06/2015.
 */
public class Singleton {

    private ConnectionBridge connection;
    private Player player;

    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
        this.setConnection(new ConnectionBridge());
        this.setPlayer(new Player());
    }

    public ConnectionBridge getConnection() {
        return connection;
    }

    private void setConnection(ConnectionBridge connection) {
        this.connection = connection;
    }

    public Player getPlayer() {
        return player;
    }

    private void setPlayer(Player player) {
        this.player = player;
    }
}
