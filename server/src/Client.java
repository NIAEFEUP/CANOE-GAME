import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Duarte on 04/06/2015.
 */
public class Client {
    final static int ROW_VALUE = 100;

    private int port;
    private int playerNr;
    private int rowValue;
    private InetAddress address;
    private DatagramSocket socket;


    public Client(int port, InetAddress address) throws IOException{
        this.setPort(port);
        this.setPlayerNr(0);
        this.setAddress(address);
        this.setRowValue(0);
        this.setSocket(new DatagramSocket(null));
    }


    public void sendToClient(String message) throws IOException{
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, getAddress(), getPort());
        getSocket().send(packet);
    }

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

    public int getRowValue() {
        return rowValue;
    }

    public void setRowValue(int rowValue) {
        this.rowValue = rowValue;
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
