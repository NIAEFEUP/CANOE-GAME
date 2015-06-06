package com.example.duarte.canoefortwo;

import com.example.duarte.canoefortwo.network.ClientServerMessages;
import com.example.duarte.canoefortwo.network.ConnectionBridge;
import com.example.duarte.canoefortwo.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.example.duarte.canoefortwo.network.ConnectionBridge;


import java.io.IOException;

public class PlayActivity extends Activity {

    private Handler progressBarHandler = new Handler();
    private int rowSpeed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play);

        final View contentView = findViewById(R.id.activity_play);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        final ImageButton strokeButton = (ImageButton) findViewById(R.id.strokeButton);
        strokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try { Singleton.getInstance().getConnection().sendMessage(ClientServerMessages.TICK);}
                catch (IOException e){e.printStackTrace();}
            }
        });



        final Button disconnectButton = (Button) findViewById(R.id.disconnectButton);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectServer();
            }
        });

        new Thread(new Runnable() {
            public void run() {
                while (Singleton.getInstance().getConnection().getState() == ConnectionBridge.State.CONNECTED) {
                    rowSpeed = Singleton.getInstance().getPlayer().getRowSpeed();
                    progressBarHandler.post(new Runnable() {
                        public void run() {
//                            Log.v("progressBarHandler", "" + Player.getRowSpeed());
                            progressBar.setProgress(rowSpeed);
                        }
                    });
                }
            }
        }).start();
    }

    private void disconnectServer() {
        Singleton.getInstance().getConnection().disconnect();
        finish();
    }


    @Override
    public void onBackPressed() {
        disconnectServer();
    }
}
