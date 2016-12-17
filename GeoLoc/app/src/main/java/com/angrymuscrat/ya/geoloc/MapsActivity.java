package com.angrymuscrat.ya.geoloc;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback, View.OnClickListener{

    private final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private StreetViewPanorama streetView;
    private MapFragment mapFragment;
    private StreetViewPanoramaFragment streetFragment;
    private boolean isMapVisible = true;
    private Button swift;

    @Override
    public void onClick(View view) {
        Log.d(TAG, "click");
        switch (view.getId()) {
            case R.id.changefragment : {
                if (isMapVisible) {
                    isMapVisible = false;
                    mapFragment.getView().setVisibility(View.GONE);
                    streetFragment.getView().setVisibility(View.VISIBLE);
                } else {
                    isMapVisible = true;
                    mapFragment.getView().setVisibility(View.VISIBLE);
                    streetFragment.getView().setVisibility(View.GONE);
                }
                break;
            }
            default: break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        streetFragment = (StreetViewPanoramaFragment) getFragmentManager()
                .findFragmentById(R.id.streetview);
        mapFragment.getMapAsync(this);
        streetFragment.getStreetViewPanoramaAsync(this);
        swift = (Button) findViewById(R.id.changefragment);
        swift.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetView = streetViewPanorama;
        LatLng SAN_FRAN = new LatLng(37.765927, -122.449972);

        streetView.setPosition(SAN_FRAN);
    }

}
