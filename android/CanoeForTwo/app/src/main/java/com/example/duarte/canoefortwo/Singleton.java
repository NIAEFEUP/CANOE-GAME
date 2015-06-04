package com.example.duarte.canoefortwo;

import com.example.duarte.canoefortwo.network.ConnectionBridge;

/**
 * Created by Duarte on 03/06/2015.
 */
public class Singleton {

    private ConnectionBridge connection;

    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
        this.setConnection(new ConnectionBridge());
    }

    public ConnectionBridge getConnection() {
        return connection;
    }

    public void setConnection(ConnectionBridge connection) {
        this.connection = connection;
    }
}
