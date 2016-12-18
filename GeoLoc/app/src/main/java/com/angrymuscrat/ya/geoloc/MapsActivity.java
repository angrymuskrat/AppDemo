package com.angrymuscrat.ya.geoloc;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.angrymuscrat.ya.geoloc.model.GameException;
import com.angrymuscrat.ya.geoloc.model.Round;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;

import java.util.Random;

//TODO сделать активити со стартовым меню

//TODO дизайн!!

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback, View.OnClickListener{

    private final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private StreetViewPanorama streetView;
    private MapFragment mapFragment;
    private StreetViewPanoramaFragment streetFragment;
    private boolean isMapVisible = true, isRoundNow = true;
    private Button swift, nextRound;
    private Round newRound;
    private TextView myText;

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
                        int res = newRound.checkUserAnswer();
                        myText.setText("your fine is " + Integer.toString(res) + " km");
                        mMap.addPolyline(new PolylineOptions()
                                .add(newRound.getUserAns(), newRound.getUserLocation())
                                .color(Color.BLACK));
                        nextRound.setText("следующий раунд");
                        isRoundNow = false;
                    }
                    catch (GameException e) {
                        Toast errorMes = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
                        errorMes.show();
                    }
                }
                else {
                    mMap.clear();
                    nextRound.setText("проверить ответ");
                    myText.setText("Round 1");
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

        //TODO: добавить сохранение данных при повороте
        newRound = new Round();
        myText = (TextView) findViewById(R.id.textmapsactivity);
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
        int r = 20_000_000;
        LatLng point = newRound.randLocation();
        Log.d(TAG, Double.toString(point.latitude) + " " + Double.toString(point.longitude));
        streetView.setPosition(point, r);
        //TODO почему-то getLocation возвращает null, это плохо
        if (streetView.getLocation() != null)
            newRound.setUserLocation(streetView.getLocation().position);
        else {
            newRound.setUserLocation(point);
            Log.d(TAG, "isn't correct location");
        }
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
