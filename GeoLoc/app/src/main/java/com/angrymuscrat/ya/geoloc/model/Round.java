package com.angrymuscrat.ya.geoloc.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by YA on 18.12.2016.
 */

public class Round {
    private LatLng userLocation = null, userAns = null;

    public Round() {
        userLocation = null;
        userAns = null;
    }

    public int checkUserAnswer() throws GameException {
        if (userAns == null)
            throw new GameException("isn,t user answer");
        if (userLocation != null ) {
            try {
                float res[] = new float[3];
                Location.distanceBetween(userAns.latitude, userAns.longitude
                        , userLocation.latitude, userLocation.longitude, res);
                return (int) (res[0]) / 1000;
            }
            catch (IllegalArgumentException e) {
                return 0;
            }
        }
        else {
            throw new GameException("Location to be determined");
        }
    }

    public void clearLocation() {
        userAns = null;
        userLocation = null;
    }

    static public LatLng randLocation() {
        Random rand = new Random(System.currentTimeMillis());
        double lat = ((rand.nextDouble() - 0.5) * 100);
        double lng = ((rand.nextDouble() - 0.5) * 100);
        return new LatLng(lat, lng);
    }

    public void setUserAns(LatLng point) {
        userAns = point;
    }

    public void setUserLocation (LatLng point){
        userLocation = point;
    }

    public LatLng getUserLocation() {
        return new LatLng(userLocation.latitude, userLocation.longitude);
    }

    public LatLng getUserAns() {
        return new LatLng(userAns.latitude, userAns.longitude);
    }
}
