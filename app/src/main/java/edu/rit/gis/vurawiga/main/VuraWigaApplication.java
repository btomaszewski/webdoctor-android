package edu.rit.gis.vurawiga.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by siddeshpillai on 10/24/15.
 */
public class VuraWigaApplication extends Application {

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
