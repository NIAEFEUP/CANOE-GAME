package com.example.duarte.canoefortwo.network;

import android.util.Log;

import com.example.duarte.canoefortwo.ChooseSide;
import com.example.duarte.canoefortwo.util.SystemUiHider;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by Duarte on 04/06/2015.
 */
public class ConnectionBridge{

    public static final int BUF_SIZE = 64;
    public static final int PORT = 4445;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {CONNECTED , NOT_CONNECTED}

    private State state;
    private DatagramSocket socket;

    public ConnectionBridge(){
        super();
        this.setState(State.NOT_CONNECTED);
    }

    public class StartConnection implements Runnable{
        String ip;
        public StartConnection(String ip){
            this.ip = ip;
        }

        public void run(){
            try {
                DatagramSocket server = new DatagramSocket();
                byte[] buf = new byte[BUF_SIZE];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), PORT);
                server.send(packet);

                int port = server.getLocalPort();
                server.close();

                Log.v(ConnectionBridge.class.toString(), "" + port);
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 1234));
                socket.receive(packet);
                Log.v(ConnectionBridge.class.toString(), "Ola");
//                if (ClientServerMessages.valueOf(buf.toString()) == ClientServerMessages.SUCCESS)
                setState(State.CONNECTED);
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
    
    public void sendMessage(ClientServerMessages message) throws IOException{
        byte[] buf = new byte[BUF_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.send(packet);

    }
    
    public ClientServerMessages receiveMessage() throws IOException{
        byte[] buf = new byte[BUF_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        return ClientServerMessages.valueOf(buf.toString());
    }

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

            if(receiveMessage() == ClientServerMessages.SUCCESS)
                return true;



        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean connect(String ip) throws IOException {
        Thread connectionAttempt = new Thread(new StartConnection(ip));
        connectionAttempt.start();
        try {
            for(int i = 0; i < 3000; i += 250)
                if(this.state == State.NOT_CONNECTED)
                    Thread.sleep(250);
                else
                    break;
            //connectionAttempt.interrupt();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if(this.getState() == State.CONNECTED)
            return true;
        return false;
    }

    public void disconnect(){
        try {
            sendMessage(ClientServerMessages.DISCONNECT);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.state = State.NOT_CONNECTED;
    }
}
