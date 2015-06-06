import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Duarte on 04/06/2015.
 */
public class Client {
    final static int ROW_VALUE = 100;

    int port;
    int playerNr;
    int rowValue;
    InetAddress address;
    DatagramSocket socket;


    public Client(int port, InetAddress address) throws IOException{
        this.port = port;
        this.playerNr = 0;
        this.address = address;
        this.rowValue = 0;
        this.socket = new DatagramSocket(null);
    }


    public void sendToClient(String message) throws IOException{
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
        socket.send(packet);
    }

    public byte[] receiveFromClient() throws IOException{
        byte[] buf = new byte[Server.BUF_SIZE];
        DatagramPacket received = new DatagramPacket(buf, buf.length);
        socket.receive(received);
        return buf;
    }

}
