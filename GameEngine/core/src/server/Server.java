package server;/*
import com.sun.java.util.jar.pack.Instruction;
*/


import com.badlogic.gdx.Gdx;
import element.CanoeObserver;
import element.Paddle;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Observable;


public class Server extends Observable implements Runnable, CanoeObserver{

    static int PORT = 4445;
    final static int BUF_SIZE = 1024;
    public final static int MAX_PLAYERS_NUMBER = 2;
    public final static int LEFT_SIDE = 1;
    public final static int RIGHT_SIDE = 2;

    private ArrayList<Client> clients;

    public Server(){
        super();
        this.clients = new ArrayList<Client>(MAX_PLAYERS_NUMBER);
        for(int i = 0; i < MAX_PLAYERS_NUMBER; i++){
            this.clients.add(null);
        }
        String ip = getIPAddress();
        if(QRCodeGenerator.createCode(ip, "ServerIP.png"))
            System.out.println(ip);
        else{
            System.out.println("Error creating IP QRCode");
        }
    }

    @Override
    public void onRow(float leftAngle, float leftVelocity, float rightAngle, float rightVelocity) {
        if(this.clients.get(0) != null)
            this.clients.get(0).setRowSpeed((int) (leftVelocity/ Paddle.MAX_ANGULAR_VELOCITY * 100));
        if(this.clients.get(1) != null)
            this.clients.get(1).setRowSpeed((int) (rightVelocity / Paddle.MAX_ANGULAR_VELOCITY * 100));
    }

    public ArrayList<Client> getClients() {
        return clients;
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
                    int previousRowSpeed = 0;
                    while (client.getPlayerNr() != 0 && client != null) {
                        try {
                            Thread.sleep(200);
                            if(client.getRowSpeed() != previousRowSpeed){
                                previousRowSpeed = client.getRowSpeed();
                                client.sendToClient("" + client.getRowSpeed());
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
         * @return              If the player succeeds to get the position he choose. If the player chooses to disconnect it also returns false.
         * @throws IOException  Throws IOException.
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
        setChanged();
        notifyObservers(playerNr);
    }

    public void disconnectAllPlayers(){
        for(Client client : clients){
            try {
                client.sendToClient(ClientServerMessages.DISCONNECT.toString());

            }catch (IOException e){
                e.printStackTrace();
            }
        }

        for(int i = 0; i < clients.size(); i++) {
            clients.set(i, null);
        }
    }

    /**
     * Get the IP address.
     *
     * @return  IP Address
     */
    public static String getIPAddress() {
        try {
            for (Enumeration<?> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration<?> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("Socket exception in getting IP Adress" + ex.toString());
        }
        return null;
    }

    public void run(){
        try {
            DatagramSocket socket = new DatagramSocket(PORT);


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
