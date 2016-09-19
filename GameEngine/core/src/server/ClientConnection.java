package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

/**
 * Created by Duarte on 04/06/2015.
 */
public abstract class ClientConnection implements Runnable {
    Client client;
    Socket socket = null;

    public ClientConnection(Socket socket) {
        this.socket = socket;
    }

    public void makeConnection() throws IOException {
        this.client = new Client(socket);
        client.sendToClient(ClientServerMessages.SUCCESS.toString());
        System.out.println("Efectou ligacao com " + socket.getInetAddress());
    }


    public abstract void disconnectClient();

    public abstract boolean clientOptSide() throws IOException, ClassNotFoundException;

}