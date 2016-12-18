package com.angrymuscrat.ya.geoloc.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by YA on 18.12.2016.
 */

public class GameMode {
    static public int gameMode = 1, amounthOfRounds = 0, score = 0;

    //gameMode = 1 - просто обычгая игра на amounthOfRounds раундов

    static public LatLng getPosition() {
        switch(gameMode) {
            case 1 : {
                //TODO нужен более интелектуальный рандом!
                Random rand = new Random(System.currentTimeMillis());
                double lat = ((rand.nextDouble() - 0.5) * 100);
                double lng = ((rand.nextDouble() - 0.5) * 100);
                return new LatLng(lat, lng);
            }
            default: return null;
        }
    }

    static public Integer gerRadius() {
        switch(gameMode) {
            case 1 : {
                return 20_000_000;
            }
            default: return null;
        }
    }

    static public void clear() {
        gameMode = 0;
        amounthOfRounds = 0;
        score = 0;
    }
}
