package com.angrymuscrat.ya.geoloc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.angrymuscrat.ya.geoloc.model.GameMode;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    private Button generalMode;
    private Button localMode;
    private Button chosenMode;

    @Override
    public void onClick(View view) {
        GameMode.score = 0;
        switch (view.getId()) {
            case R.id.mode1 : {
                Intent intent = new Intent (this, GeneralModeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.mode2:{
                Intent intent = new Intent (this, LocalModeActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.mode3:{
                Intent intent = new Intent (this, ChosenModeActivity.class);
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
        localMode = (Button) findViewById(R.id.mode2);
        localMode.setOnClickListener(this);
        chosenMode = (Button) findViewById(R.id.mode3);
        chosenMode.setOnClickListener(this);
    }
}
