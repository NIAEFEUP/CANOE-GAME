package com.example.duarte.canoefortwo;

import android.app.Activity;
import android.content.Intent;
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
                    try {
                        if(!Singleton.getInstance().getConnection().connect(ipInputField.getText().toString())){
                            errorText.setText("Cannot connect to server");
                            errorText.setVisibility(TextView.VISIBLE);
                        }else {
                            Toast.makeText(ConnectMenu.this, "Connected to " + ipInputField.getText(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ConnectMenu.this, ChooseSide.class));
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void onResume(){
        super.onResume();
        final TextView errorText = (TextView) findViewById(R.id.errorTextField);
        errorText.setVisibility(TextView.INVISIBLE);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connect_menu, menu);
        return true;
    }*/
}
