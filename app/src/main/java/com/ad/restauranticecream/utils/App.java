package com.ad.restauranticecream.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.android.gms.analytics.Tracker;
import com.orm.SugarApp;

/**
 * Created  on 8/2/16.
 */
public class App extends SugarApp {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getBaseContext());
    }
}
