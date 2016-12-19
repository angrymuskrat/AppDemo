package com.angrymuscrat.ya.geoloc;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by YA on 19.12.2016.
 */

public class GeoLoc extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
