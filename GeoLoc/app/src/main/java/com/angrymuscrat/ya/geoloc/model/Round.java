package com.angrymuscrat.ya.geoloc.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

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
                GameMode.score += (int) res[0];
                return (int) (res[0]);
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
