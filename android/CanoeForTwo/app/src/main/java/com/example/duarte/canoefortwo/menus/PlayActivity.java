package com.example.duarte.canoefortwo.menus;

import com.example.duarte.canoefortwo.R;
import com.example.duarte.canoefortwo.Singleton;
import com.example.duarte.canoefortwo.network.ClientServerMessages;
import com.example.duarte.canoefortwo.network.ConnectionBridge;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;


import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class PlayActivity extends Activity implements Observer {

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

        Singleton.getInstance().getConnection().addObserver(this);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        final ImageButton strokeButton = (ImageButton) findViewById(R.id.strokeButton);
        strokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().getConnection().sendMessage(ClientServerMessages.TICK);
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
                    int previousRowSpeed = rowSpeed;
                    rowSpeed = Singleton.getInstance().getPlayer().getRowSpeed();
                    if(rowSpeed == previousRowSpeed){
                        continue;
                    }
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

    @Override
    public void update(Observable observable, Object data) {
        if (Singleton.getInstance().getConnection().getState()== ConnectionBridge.State.NOT_CONNECTED)
            finish();
    }
}
