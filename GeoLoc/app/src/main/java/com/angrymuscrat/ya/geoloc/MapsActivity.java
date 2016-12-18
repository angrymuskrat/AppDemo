package com.angrymuscrat.ya.geoloc;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.angrymuscrat.ya.geoloc.model.GameException;
import com.angrymuscrat.ya.geoloc.model.GameMode;
import com.angrymuscrat.ya.geoloc.model.Round;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        OnStreetViewPanoramaReadyCallback, View.OnClickListener{

    private final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private StreetViewPanorama streetView;
    private MapFragment mapFragment;
    private StreetViewPanoramaFragment streetFragment;
    private boolean isMapVisible = true, isRoundNow = true;
    private Button swift, nextRound;
    private Round newRound;
    private TextView myText;
    private int numberOfRound = 1;

    @Override
    public void onClick(View view) {
        Log.d(TAG, "click");
        switch (view.getId()) {
            case R.id.changefragment : {
                    visibleOfStreetView(isMapVisible);
                break;
            }
            case R.id.checkanswer : {
                if (isRoundNow) {
                    try {
                        newRound.setUserLocation(streetView.getLocation().position);
                        int res = newRound.checkUserAnswer();
                        String mes = "your fine is " + Integer.toString(res) + " m";
                        mMap.addPolyline(new PolylineOptions()
                                .add(newRound.getUserAns(), newRound.getUserLocation())
                                .color(Color.RED));
                        nextRound.setText("следующий раунд");
                        isRoundNow = false;
                        if (numberOfRound == GameMode.amounthOfRounds) {
                            nextRound.setText("menu");
                            mes += "; all fine is " + Integer.toString(GameMode.score) + " m";
                        }
                        myText.setText(mes);
                    }
                    catch (GameException e) {
                        Toast errorMes = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
                        errorMes.show();
                    }
                }
                else {
                    if (numberOfRound == GameMode.amounthOfRounds) {
                        Intent intent = new Intent(this, MenuActivity.class);
                        startActivity(intent);
                    }
                    numberOfRound++;
                    mMap.clear();
                    nextRound.setText("проверить ответ");
                    myText.setText("Round " + Integer.toString(numberOfRound));
                    newRound.clearLocation();
                    setNewLocation();
                    isRoundNow = true;
                }
                visibleOfStreetView(isRoundNow);
                break;
            }
            default: break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        newRound = new Round();
        myText = (TextView) findViewById(R.id.textmapsactivity);
        myText.setText("Round " + Integer.toString(numberOfRound));
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        streetFragment = (StreetViewPanoramaFragment) getFragmentManager()
                .findFragmentById(R.id.streetview);
        mapFragment.getMapAsync(this);
        streetFragment.getStreetViewPanoramaAsync(this);
        swift = (Button) findViewById(R.id.changefragment);
        nextRound = (Button) findViewById(R.id.checkanswer);
        nextRound.setOnClickListener(this);
        swift.setOnClickListener(this);
        visibleOfStreetView(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (isRoundNow) {
                    mMap.clear();
                    newRound.setUserAns(latLng);
                    mMap.addMarker(new MarkerOptions().title("answer").position(latLng));
                }
            }
        });
        LatLng startPoint = new LatLng(-34, 151);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint));
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetView = streetViewPanorama;
        setNewLocation();
    }

    private void setNewLocation() {
        int r = GameMode.gerRadius();
        LatLng point = GameMode.getPosition();
        Log.d(TAG, Double.toString(point.latitude) + " " + Double.toString(point.longitude));
        streetView.setPosition(point, r);
    }

    private void visibleOfStreetView(boolean isTrue) {
        int tmp;
        tmp = isTrue ? View.VISIBLE :  View.GONE;
        streetFragment.getView().setVisibility(tmp);
        tmp = isTrue ? View.GONE :  View.VISIBLE;
        mapFragment.getView().setVisibility(tmp);
        isMapVisible = !isTrue;
    }
}
