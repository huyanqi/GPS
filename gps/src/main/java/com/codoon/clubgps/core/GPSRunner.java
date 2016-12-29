package com.codoon.clubgps.core;

import android.app.Activity;
import android.content.Intent;

import com.codoon.clubgps.ui.GPSControllerActivity;
import com.codoon.clubgps.util.CacheUtil;

/**
 * Created by Frankie on 2016/12/27.
 *
 * GPS需要从这里打开
 */

public class GPSRunner {

    private double latitude,longitute;

    private GPSRunner(){}

    private Activity mActivity;

    public GPSRunner(Activity activity){
        this.mActivity = activity;
    }

    public GPSRunner setLocation(double latitude,double longitute){
        CacheUtil.getInstance().updateMyCacheLocation(latitude, longitute);
        return this;
    }

    public void run(){
        Intent intent = new Intent(mActivity, GPSControllerActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitute", longitute);
        mActivity.startActivity(intent);
    }

}
