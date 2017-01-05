package com.codoon.clubgps.application;

import android.content.SharedPreferences;

import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by Frankie on 2016/12/27.
 */

public class GPSApplication extends LitePalApplication {

    private static GPSApplication appContext;
    private SharedPreferences mSharedPreferences;
    private boolean isFake;
    private String user_id;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        LitePal.initialize(this);
        CrashReport.initCrashReport(getApplicationContext(), "9267c19260", false);
    }

    public static GPSApplication getAppContext() {
        return appContext;
    }

    public SharedPreferences getSharedPreferences(){
        if(mSharedPreferences == null)
            mSharedPreferences = appContext.getSharedPreferences("gps", MODE_PRIVATE);
        return mSharedPreferences;
    }

    public boolean isFake() {
        return isFake;
    }

    public void setFake(boolean fake) {
        isFake = fake;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
