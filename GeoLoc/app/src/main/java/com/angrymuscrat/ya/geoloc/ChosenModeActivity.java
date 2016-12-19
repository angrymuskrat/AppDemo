package com.angrymuscrat.ya.geoloc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.angrymuscrat.ya.geoloc.model.GameGenInterface;
import com.angrymuscrat.ya.geoloc.model.GameMode;
import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by pohvalister on 19.12.16.
 */

public class ChosenModeActivity extends Activity {
    EditText myEditText;
    Button myButton;

    EditText editLongit;
    EditText editLatit;
    public static double lat;
    public static double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameMode.generator=new ChosenGenerator();//the chosen generator, blessed with divine forse
        //почти не менял general->chosen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_mode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myButton = (Button) findViewById(R.id.chosenitbutton);
        myEditText = (EditText) findViewById(R.id.chosenittext);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //
                    lat=(new Double(editLatit.getText().toString())).doubleValue();
                    lng=(new Double(editLongit.getText().toString())).doubleValue();
                    //
                    GameMode.amounthOfRounds = (new Integer(myEditText.getText().toString())).intValue();
                    if (GameMode.amounthOfRounds < 1 || GameMode.amounthOfRounds > 10)
                        throw new Exception();
                    Intent intent = new Intent(ChosenModeActivity.this, MapsActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast errorMes = Toast.makeText(ChosenModeActivity.this, "Enter the correct value", Toast.LENGTH_LONG);
                    errorMes.show();
                }
            }
        });
        //
        editLongit=(EditText) findViewById(R.id.chosenEditLong);
        editLatit=(EditText) findViewById(R.id.chosenEditLatit);
    }
    private class ChosenGenerator implements GameGenInterface {
        @Override
        public LatLng getPosition() {
            Random rand = new Random(System.currentTimeMillis());
            double latTmp = ((rand.nextDouble() - 0.5) * 4 + ChosenModeActivity.lat);
            double lngTmp = ((rand.nextDouble() - 0.5) * 8 + ChosenModeActivity.lng);
            return new LatLng(latTmp, lngTmp);
        }
        public Integer getRadius(){
            return 200_000;//Спб : 44км * 25 км мб радиус надо выбрать
        }
    }
}
