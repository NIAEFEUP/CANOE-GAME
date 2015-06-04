package com.example.duarte.canoefortwo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.duarte.canoefortwo.network.ConnectionBridge;

public class ChooseSide extends Activity {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);this.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_connect_menu);
        setContentView(R.layout.activity_choose_side);

        final ImageButton rightButton = (ImageButton) findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Singleton.getInstance().getConnection().choosePlayerNr(RIGHT))
                    startActivity(new Intent(ChooseSide.this, PlayActivity.class));
                else
                    Toast.makeText(ChooseSide.this, "Side already selected by another player", Toast.LENGTH_LONG);
            }
        });

        final ImageButton leftButton = (ImageButton) findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Singleton.getInstance().getConnection().choosePlayerNr(LEFT))
                    startActivity(new Intent(ChooseSide.this, PlayActivity.class));
                else
                    Toast.makeText(ChooseSide.this, "Side already selected by another player", Toast.LENGTH_LONG);
            }
        });

        final Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().getConnection().disconnect();
                finish();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Singleton.getInstance().getConnection().getState() == ConnectionBridge.State.NOT_CONNECTED)
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_side, menu);
        return true;
    }
}
