/*
import com.sun.java.util.jar.pack.Instruction;
*/

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Duarte on 03/06/2015.
 */
public class Server {

    final static int PORT = 4445;
    final static int BUF_SIZE = 1024;
    final static int MAX_PLAYERS_NUMBER = 2;

    ArrayList<Client> clients;

    public Server(){
        super();
        this.clients = new ArrayList<Client>(2);
        this.clients.add(null);
        this.clients.add(null);
    }


    public class Responder extends ClientConnection {

        public Responder(DatagramSocket socket, DatagramPacket packet){
            super(socket,packet);
        }

        public void disconnectClient(){
            int number = client.playerNr;
            client.socket = null;
            if(number!= 0)
                clients.set(number - 1,null);
            client.playerNr = 0;
            System.out.println("Player " + number + " has disconnected");
        }

        private void startRowUpdate(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (client.playerNr != 0 || client != null) {
                        try {
                            Thread.sleep(200);
                            if(client.rowValue > 0){
                                client.rowValue -= 5;
                                client.sendToClient("" + client.rowValue);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        public boolean clientOptSide() throws IOException{
            byte[] buf = client.receiveFromClient();
            ClientServerMessages message = ClientServerMessages.valueOf(new String(buf).trim());
            switch (message) {
                case CHOOSE_PLAYER_1:
                    if (clients.get(0) != null)
                        return false;
                    clients.set(0, client);
                    client.playerNr = 1;
                    System.out.println("Client IP: " + client.address + " is now player " + client.playerNr);
                    return true;
                case CHOOSE_PLAYER_2:
                    if (clients.get(1) != null)
                        return false;
                    clients.set(1, client);
                    client.playerNr = 2;
                    System.out.println("Client IP: " + client.address + " is now player " + client.playerNr);
                    return true;
                case DISCONNECT:
                    disconnectClient();
                default:
                    return false;
            }
        }

        public void run() {
            System.out.println("Atendimento Client in IP: " + packet.getAddress() + " Port:" + packet.getPort());
            try {
                makeConnection();


                while(client.socket != null && client.playerNr == 0) {
                    boolean result = clientOptSide();
                    if(result)
                        client.sendToClient(ClientServerMessages.SUCCESS.toString());
                    else
                        if(client.socket != null)
                            client.sendToClient(ClientServerMessages.FAILED.toString());
                }

                startRowUpdate();

                while(client.socket != null){
                    byte[] buf = client.receiveFromClient();
                    ClientServerMessages message = ClientServerMessages.valueOf(new String(buf).trim());
                    switch (message){
                        case TICK:
                            sentTick(client.playerNr);
                            break;
                        default:
                            disconnectClient();
                            break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sentTick(int playerNr){
        System.out.println("Player nr " + playerNr +" - TICK!");
        Client client = clients.get(playerNr -1);
        if(client.rowValue != Client.ROW_VALUE){
            client.rowValue += 10;
            if(client.rowValue > Client.ROW_VALUE)
                client.rowValue = Client.ROW_VALUE;
            try{
                client.sendToClient("" + client.rowValue);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void run() throws IOException{
        @SuppressWarnings("resource")
        DatagramSocket socket = new DatagramSocket(PORT);
        System.out.println(InetAddress.getLocalHost().getHostAddress());

        while (true) {
            byte[] buf = new byte[BUF_SIZE];
            DatagramPacket received = new DatagramPacket(buf, buf.length);
            socket.receive(received);
//          System.out.println(received.getAddress() + " " + received.getPort());

            new Thread(new Responder(socket,received)).start();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
