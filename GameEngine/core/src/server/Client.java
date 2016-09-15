package server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class that has the clients information and that handles communication with the client.
 */
public class Client {
    final static int ROW_VALUE = 100;

    private int playerNr;
    private int rowSpeed;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    public Client(Socket socket) throws IOException{
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Sends a message to the client through it's socket.
     *
     * @param message       message to be sent.
     * @throws IOException  Throws IOException.
     */
    public void sendToClient(String message) throws IOException{
        out.writeObject(message);
        out.flush();
    }


    /**
     * Handles the reception from a client's message through.
     *
     * @return              message received from the cliente in a byte[]
     * @throws IOException  Throws IOException.
     */
    public Object receiveFromClient() throws IOException, ClassNotFoundException {
        return in.readObject();
    }


    public int getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(int playerNr) {
        this.playerNr = playerNr;
    }

    public int getRowSpeed() {
        return rowSpeed;
    }

    public void setRowSpeed(int rowSpeed) {
        this.rowSpeed = rowSpeed;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }
}
