package com.example.duarte.canoefortwo.network;

import android.util.Log;

import com.example.duarte.canoefortwo.menus.ChooseSide;
import com.example.duarte.canoefortwo.Singleton;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;

/**
 * Establishes the connection with the server
 */
public class ConnectionBridge extends Observable{

    public static final int BUF_SIZE = 1024;
    public static final int PORT = 4445;

    public enum State {CONNECTED , NOT_CONNECTED}

    private State state;
    private Socket socket;
    private InetAddress address;
    private int privatePort;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ConnectionBridge(){
        super();
        this.setState(State.NOT_CONNECTED);
    }


    /**
     * Establishes a connection with the server.
    public class StartConnection implements Runnable{
        String ipString;
        public StartConnection(String ip){
            this.ipString = ip;
        }

        public void run(){
            try {
                socket = new Socket(address,PORT);
                this.in = new InputStreamReader(socket.getInputStream());


                String received_info = new String(received.getData()).trim();
                if (received_info.equals(ClientServerMessages.SUCCESS.toString().trim())){
                    address = received.getAddress();
                    privatePort = received.getPort();
                    Log.v("Connection", received_info);
                    setState(State.CONNECTED);
                }else{
                    socket = null;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
     */

    public boolean startConnection(String address) throws IOException {
        try {
            socket = new Socket(address,PORT);
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());

            String receivedInfo = null;

            receivedInfo = ((String) in.readObject()).trim();

            if (receivedInfo.equals(ClientServerMessages.SUCCESS.toString().trim())){
                Log.v("Connection", receivedInfo);
                setState(State.CONNECTED);
                return true;
            }else{
                socket = null;
                this.in = null;
                this.out = null;
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends one of the predefined messages to the server through the socket.
     *
     * @param message   Message sent
     * @throws          IOException
     */
    public void sendMessage(ClientServerMessages message){
        final String strMessage = message.toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeObject(strMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * Receives a message from the server through the socket.
     *
     * @return  message received.
     * @throws IOException
     */
    public ClientServerMessages receiveMessage() throws IOException, ClassNotFoundException {
        return ClientServerMessages.valueOf(( (String) in.readObject()).trim());
    }

    /**
     * Receives a message from the server through the socket.
     * The received message is returned as a string.
     *
     * @return  message received
     * @throws IOException
     */
    public String receiveStringMessage() throws IOException, ClassNotFoundException {
        return ((String) in.readObject()).trim();
    }

    /***
     * Receives the rowSpeed values from the server and updates the local player stats.
     */
    public class ReceiveValuesFromServer implements Runnable{
        @Override
        public void run() {
            while (state == State.CONNECTED){
                try {
                    String received = receiveStringMessage();
                    if(received.equals( ClientServerMessages.DISCONNECT.toString())) {
                        setState(State.NOT_CONNECTED);
                        return;
                    }
                    Singleton.getInstance().getPlayer().setRowSpeed(Integer.valueOf(received));
                }catch (IOException e){
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sends a message to the server with the position the user wants to play in.
     * Verifies the response from the server and returns if it was successful or not.
     *
     * @param   playerNr    position in which the user wants to play in.
     * @return              result.
     */
    public boolean choosePlayerNr(int playerNr){
        try {
            switch (playerNr) {
                case ChooseSide.LEFT:
                    sendMessage(ClientServerMessages.CHOOSE_PLAYER_1);
                    break;
                case ChooseSide.RIGHT:
                    sendMessage(ClientServerMessages.CHOOSE_PLAYER_2);
                    break;
                default:
                    return false;
            }

            if(receiveMessage() == ClientServerMessages.SUCCESS) {
                Log.v("Player option", "Success");
                new Thread(new ReceiveValuesFromServer()).start();
                return true;
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Creates a thread to handle the connection attempt to the server using the StartConnection class.
     * During 3 seconds verifies if the connection has benn established.
     * Upon success it changes the connection state to CONNECTED.
     * If not sends a interrupt signal to the thread.
     *
     * @param ip    Server's IP.
     * @return      connection resutl.
     * @throws      IOException
     */
    public boolean connect(String ip) throws IOException {
        return startConnection(ip);

       /* long initTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - initTime < 3000){
            if(this.getState() == State.CONNECTED)
                return true;
        }

        return false;
        */
    }

    /**
     * Sends a message to the server saying the it wants to disconnect.
     * Then it changes the state to NOT_CONNECTED
     */
    public void disconnect(){
        try {
            sendMessage(ClientServerMessages.DISCONNECT);
        }catch (Exception e){
            e.printStackTrace();
        }
        setState(State.NOT_CONNECTED);
        Singleton.getInstance().getPlayer().setRowSpeed(0);

    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        setChanged();
        notifyObservers();
    }


}
