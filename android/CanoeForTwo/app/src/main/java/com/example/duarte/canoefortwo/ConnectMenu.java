package com.example.duarte.canoefortwo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duarte.canoefortwo.util.SystemUiHider;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ConnectMenu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* this.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_connect_menu);

        final Singleton singleton = Singleton.getInstance();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        final String ipPattern = "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b";   //Regular expression to verify the IP entered by the user.
        final EditText ipInputField = (EditText) findViewById(R.id.ipTextField);

        final TextView errorText = (TextView) findViewById(R.id.errorTextField);

        final Button connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ipInputField.getText().toString().matches(ipPattern)){
                    errorText.setText("Invalid IP format");
                    errorText.setVisibility(TextView.VISIBLE);
                }else{
                    new Thread(){
                        public void run(){
                            ConnectMenu.this.runOnUiThread(new makeConnection(ipInputField.getText().toString()));
                        }
                    }.start();

                }
            }
        });

        final Button qrButton = (Button) findViewById(R.id.QRbutton);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

                    startActivityForResult(intent, 0);

                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                final EditText ipInputField = (EditText) findViewById(R.id.ipTextField);
                String contents = data.getStringExtra("SCAN_RESULT");
                ipInputField.setText(contents);

            }
            if(resultCode == RESULT_CANCELED){
                Toast.makeText(ConnectMenu.this, "QR code reading failed", Toast.LENGTH_SHORT);
            }
        }
    }

    public void onResume(){
        super.onResume();
        final TextView errorText = (TextView) findViewById(R.id.errorTextField);
        errorText.setVisibility(TextView.INVISIBLE);
    }

    public class makeConnection implements Runnable{
        private String ip;
        public makeConnection(String ip){
            super();
            this.ip = ip;
        }
        @Override
        public void run() {
            final TextView errorText = (TextView) findViewById(R.id.errorTextField);
            try {
                if(!Singleton.getInstance().getConnection().connect(ip)){
                    errorText.setText("Cannot connect to server");
                    errorText.setVisibility(TextView.VISIBLE);
                }else {
                    Toast.makeText(ConnectMenu.this, "Connected to " + ip, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ConnectMenu.this, ChooseSide.class));
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
