package com.angrymuscrat.ya.geoloc;
//TODO дизайн + все надписи в приложении одним языком, а не как я сделал

//TODO еше игровых режимов!!!

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.angrymuscrat.ya.geoloc.model.GameException;
import com.angrymuscrat.ya.geoloc.model.GameMode;
import com.google.android.gms.maps.model.PolylineOptions;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    private Button generalMode;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mode1 : {
                GameMode.gameMode = 1;
                Intent intent = new Intent (this, GeneralModeActivity.class);
                startActivity(intent);
                break;
            }
            default: break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        generalMode = (Button) findViewById(R.id.mode1);
        generalMode.setOnClickListener(this);
    }
}
