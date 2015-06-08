package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Duarte on 04/06/2015.
 */
public abstract class ClientConnection implements Runnable {
    Client client;
    DatagramSocket socket = null;
    DatagramPacket packet = null;

    public ClientConnection(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    public void makeConnection() throws IOException {
        this.client = new Client(packet.getPort(), packet.getAddress());
        client.sendToClient(ClientServerMessages.SUCCESS.toString());
        System.out.println("Efectou ligacao com " + packet.getAddress());
    }


    public abstract void disconnectClient();

    public abstract boolean clientOptSide() throws IOException;

}