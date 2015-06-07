/*
import com.sun.java.util.jar.pack.Instruction;
*/

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by Duarte on 03/06/2015.
 */
public class Server implements Runnable{

    final static int PORT = 4445;
    final static int BUF_SIZE = 1024;
    final static int MAX_PLAYERS_NUMBER = 2;

    ArrayList<Client> clients;

    public Server(){
        super();
        this.clients = new ArrayList<Client>(MAX_PLAYERS_NUMBER);
        for(int i = 0; i < MAX_PLAYERS_NUMBER; i++){
            this.clients.add(null);
        }
    }


    public class Responder extends ClientConnection {

        public Responder(DatagramSocket socket, DatagramPacket packet){
            super(socket,packet);
        }

        /**
         * Disconnects the client from the server.
         * If the client is already assigned to a position it removes him from there to.
         */
        public void disconnectClient(){
            int number = client.getPlayerNr();
            client.setSocket(null);
            if(number!= 0)
                clients.set(number - 1,null);
            client.setPlayerNr(0);
            System.out.println("Player " + number + " has disconnected");
        }


        /**
         * Creates a thread that handles the decrease of the rowSpeed over time.
         * The thread executes every 200 miliseconds and decreases the rowSpeed by 5. The rowSpeed never falls behind 0.
         * When the rowSpeed is 0 it does nothing.
         */
        private void startRowUpdate(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (client.getPlayerNr() != 0 && client != null) {
                        try {
                            Thread.sleep(200);
                            if(client.getRowSpeed() > 0){
                                client.setRowSpeed(client.getRowSpeed() - 5);
                                client.sendToClient("" + client.getRowSpeed());
                                if(client.getRowSpeed() < 0)
                                    client.setRowSpeed(0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        /**
         * Handles the side choosing from the player.
         *
         * @return If the player succeeds to get the position he choose. If the player chooses to disconnect it also returns false.
         * @throws IOException
         */
        public boolean clientOptSide() throws IOException{
            byte[] buf = client.receiveFromClient();
            ClientServerMessages message = ClientServerMessages.valueOf(new String(buf).trim());
            switch (message) {
                case CHOOSE_PLAYER_1:
                    if (clients.get(0) != null)
                        return false;
                    clients.set(0, client);
                    client.setPlayerNr(1);
                    System.out.println("Client IP: " + client.getAddress() + " is now player " + client.getPlayerNr());
                    return true;
                case CHOOSE_PLAYER_2:
                    if (clients.get(1) != null)
                        return false;
                    clients.set(1, client);
                    client.setPlayerNr(2);
                    System.out.println("Client IP: " + client.getAddress() + " is now player " + client.getPlayerNr());
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


                while(client.getSocket() != null && client.getPlayerNr() == 0) {
                    boolean result = clientOptSide();
                    if(result)
                        client.sendToClient(ClientServerMessages.SUCCESS.toString());
                    else
                        if(client.getSocket() != null)
                            client.sendToClient(ClientServerMessages.FAILED.toString());
                }

                startRowUpdate();

                while(client.getSocket() != null){
                    byte[] buf = client.receiveFromClient();
                    ClientServerMessages message = ClientServerMessages.valueOf(new String(buf).trim());
                    switch (message){
                        case TICK:
                            sentTick(client.getPlayerNr());
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

    /**
     * Increases player rowSpeed by 10.
     *
     * @param playerNr  Player who sent the Tick
     */
    public void sentTick(int playerNr){
        System.out.println("Player nr " + playerNr +" - TICK!");
        Client client = clients.get(playerNr -1);
        if(client.getRowSpeed() != Client.ROW_VALUE){
            client.setRowSpeed(client.getRowSpeed() + 10);
            if(client.getRowSpeed() > Client.ROW_VALUE)
                client.setRowSpeed(Client.ROW_VALUE);
            try{
                client.sendToClient("" + client.getRowSpeed());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void run(){
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            if(QRCodeGenerator.createCode(InetAddress.getLocalHost().getHostAddress(), "res/ServerIP.png"))
                System.out.println(InetAddress.getLocalHost().getHostAddress());
            else{
                System.out.println("Error creating IP QRCode");
            }

            while (true) {
                byte[] buf = new byte[BUF_SIZE];
                DatagramPacket received = new DatagramPacket(buf, buf.length);
                socket.receive(received);

                new Thread(new Responder(socket,received)).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new Server()).start();
    }
}
