import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Class that has the clients information and that handles communication with the client.
 */
public class Client {
    final static int ROW_VALUE = 100;

    private int port;
    private int playerNr;
    private int rowSpeed;
    private InetAddress address;
    private DatagramSocket socket;


    public Client(int port, InetAddress address) throws IOException{
        this.setPort(port);
        this.setPlayerNr(0);
        this.setAddress(address);
        this.setRowSpeed(0);
        this.setSocket(new DatagramSocket(null));
    }

    /**
     * Sends a message to the client through it's socket.
     *
     * @param message   message to be sent.
     * @throws IOException
     */
    public void sendToClient(String message) throws IOException{
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, getAddress(), getPort());
        getSocket().send(packet);
    }


    /**
     * Handles the reception from a client's message through.
     *
     * @return  message received from the cliente in a byte[]
     * @throws IOException
     */
    public byte[] receiveFromClient() throws IOException{
        byte[] buf = new byte[Server.BUF_SIZE];
        DatagramPacket received = new DatagramPacket(buf, buf.length);
        getSocket().receive(received);
        return buf;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }
}
