package com.codoon.clubgps.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by Frankie on 2016/12/27.
 */

public class GPSApplication extends Application {

    private static GPSApplication appContext;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static GPSApplication getAppContext() {
        return appContext;
    }

    public SharedPreferences getSharedPreferences(){
        if(mSharedPreferences == null)
            mSharedPreferences = appContext.getSharedPreferences("gps", MODE_PRIVATE);
        return mSharedPreferences;
    }

}
