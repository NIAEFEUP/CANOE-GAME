package com.example.duarte.canoefortwo.network;

import android.os.Looper;
import android.util.Log;

import com.example.duarte.canoefortwo.ChooseSide;
import com.example.duarte.canoefortwo.Singleton;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Duarte on 04/06/2015.
 */
public class ConnectionBridge{

    public static final int BUF_SIZE = 1024;
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
    private InetAddress ip;
    private int privatePort;
    public ConnectionBridge(){
        super();
        this.setState(State.NOT_CONNECTED);
    }

    public class ReceiveValuesFromServer implements Runnable{

        @Override
        public void run() {
            while (state == State.CONNECTED){
                Log.v("Receive from server", "receive a correr");
                try {
                    String received = receiveStringMessage();
                    Log.v("Received packet", received);
                    Singleton.getInstance().getPlayer().setRowSpeed(new Integer(received));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public class StartConnection implements Runnable{
        String ipString;
        public StartConnection(String ip){
            this.ipString = ip;
        }

        public void run(){
            try {
                socket = new DatagramSocket();
                byte[] buf = new byte[BUF_SIZE];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(ipString), PORT);
                socket.send(packet);

                buf = new byte[BUF_SIZE];
                DatagramPacket received = new DatagramPacket(buf, buf.length);
                socket.receive(received);

                String received_info = new String(received.getData()).trim();
                if (received_info.equals(ClientServerMessages.SUCCESS.toString().trim())){
                    ip = received.getAddress();
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
    
    public void sendMessage(ClientServerMessages message) throws IOException{
        byte[] buf = message.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, privatePort);
        socket.send(packet);
    }
    
    public ClientServerMessages receiveMessage() throws IOException{
        byte[] buf = new byte[BUF_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        return ClientServerMessages.valueOf(new String(packet.getData()).trim());
    }

    public String receiveStringMessage() throws IOException{
        byte[] buf = new byte[BUF_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        return new String(packet.getData()).trim();
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

            if(receiveMessage() == ClientServerMessages.SUCCESS) {
                Log.v("Player option", "Success");
                new Thread(new ReceiveValuesFromServer());
                return true;
            }



        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean connect(String ip) throws IOException {
        new Thread(new StartConnection(ip)).start();

        long initTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - initTime < 3000){
            if(this.getState() == State.CONNECTED)
                return true;
        }
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
