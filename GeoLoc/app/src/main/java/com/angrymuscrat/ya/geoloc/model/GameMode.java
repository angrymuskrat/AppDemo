package com.angrymuscrat.ya.geoloc.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by YA on 18.12.2016.
 */

public class GameMode {
    static public int amounthOfRounds = 0, score = 0;
    static public GameGenInterface generator=null;

    //gameMode = 1 - просто обычгая игра на amounthOfRounds раундов

    static public LatLng getPosition() {
        if (generator==null)
            return null;
        else
            return generator.genPosition();
    }

    static public Integer gerRadius() {
        if (generator==null)
            return null;
        else
            return generator.genRadius();
    }

    static public void clear() {
        generator=null;
        //gameMode = 0;
        amounthOfRounds = 0;
        score = 0;
    }
}
