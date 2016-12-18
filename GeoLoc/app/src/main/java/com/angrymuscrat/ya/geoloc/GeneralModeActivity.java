package com.angrymuscrat.ya.geoloc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.angrymuscrat.ya.geoloc.model.GameMode;

public class GeneralModeActivity extends Activity {
    EditText myEditText;
    Button myButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_mode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myButton = (Button) findViewById(R.id.generaleditbutton);
        myEditText = (EditText) findViewById(R.id.generaledittext);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GameMode.amounthOfRounds = (new Integer(myEditText.getText().toString())).intValue();
                    if (GameMode.amounthOfRounds < 1 || GameMode.amounthOfRounds > 10)
                        throw new Exception();
                    Intent intent = new Intent (GeneralModeActivity.this, MapsActivity.class);
                    startActivity(intent);
                }
                catch (Exception e) {
                    Toast errorMes = Toast.makeText(GeneralModeActivity.this, "Введите корректное значение!", Toast.LENGTH_LONG);
                    errorMes.show();
                }
            }
        });
    }
}
