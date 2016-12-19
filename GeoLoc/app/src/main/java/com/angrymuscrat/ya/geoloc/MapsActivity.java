package com.angrymuscrat.ya.geoloc;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.angrymuscrat.ya.geoloc.model.GameException;
import com.angrymuscrat.ya.geoloc.model.GameMode;
import com.angrymuscrat.ya.geoloc.model.PlaceException;
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
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.maps.model.StreetViewPanoramaOrientation;

import static java.security.AccessController.getContext;

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
    private int numberOfRound = 0;

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
                        if (streetView.getLocation() == null)
                            throw new PlaceException();
                        newRound.setUserLocation(streetView.getLocation().position);
                        int res = newRound.checkUserAnswer();
                        String mes = "your score is " + Integer.toString(res) + " m";
                        mMap.addPolyline(new PolylineOptions()
                                .add(newRound.getUserAns(), newRound.getUserLocation())
                                .color(Color.RED));
                        nextRound.setText("Next round");
                        isRoundNow = false;
                        numberOfRound++;
                        if (numberOfRound == GameMode.amounthOfRounds) {
                            fin(mes);
                        }
                        else
                            myText.setText(mes);
                    }
                    catch (GameException e) {
                        Toast errorMes = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
                        errorMes.show();
                    }
                    catch (PlaceException e) {
                        fin("Near this location there aren't panoramas");
                    }
                }
                else {
                    if (numberOfRound == GameMode.amounthOfRounds) {
                        Intent intent = new Intent(this, MenuActivity.class);
                        startActivity(intent);
                    }
                    mMap.clear();
                    nextRound.setText("Check answer");
                    myText.setText("Round " + Integer.toString(numberOfRound + 1));
                    newRound.clearLocation();
                    isRoundNow = true;
                    setNewLocation();
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
        myText.setText("Round " + Integer.toString(numberOfRound + 1));
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
        streetView.setStreetNamesEnabled(false);
        setNewLocation();
    }

    private void setNewLocation() {
        Log.d(TAG, "new location");
        if (!isInternetConnection()) {
            Log.d(TAG, "isn't Internet connection");
            fin("You haven't Internet connection");
        }
        int r = GameMode.getRadius();
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

    private final void fin(String mes) {
        Log.d(TAG, "fin");
        nextRound.setText("Menu");
        myText.setText(mes + "; all score is " + Integer.toString(GameMode.score)
                + " from "
                + Integer.toString(Round.MAX_POINT * numberOfRound));
        numberOfRound = GameMode.amounthOfRounds;
        isRoundNow = false;
    }

    private boolean isInternetConnection () {
        ConnectivityManager netState = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = netState.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
