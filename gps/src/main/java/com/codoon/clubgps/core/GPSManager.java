package com.codoon.clubgps.core;

import android.app.Activity;
import android.content.Intent;

import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.ui.GPSControllerActivity;
import com.codoon.clubgps.ui.HistoryListActivity;
import com.codoon.clubgps.util.CacheUtil;

/**
 * Created by Frankie on 2016/12/27.
 *
 * GPS相关功能需要从这里开始
 */

public class GPSManager {

    private double latitude,longitute;

    private GPSManager(){}

    private Activity mActivity;

    public GPSManager(Activity activity){
        this.mActivity = activity;
    }

    public GPSManager setLocation(double latitude, double longitute){
        CacheUtil.getInstance().updateMyCacheLocation(latitude, longitute);
        return this;
    }

    /**
     * 是否需要模拟GPS数据
     */
    public GPSManager fake(boolean fake){
        GPSApplication.getAppContext().setFake(fake);
        return this;
    }

    public GPSManager userId(String user_id){
        GPSApplication.getAppContext().setUser_id(user_id);
        return this;
    }

    /**
     * 开始跑步
     */
    public void run(){
        Intent intent = new Intent(mActivity, GPSControllerActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitute", longitute);
        mActivity.startActivity(intent);
    }

    /**
     * 获取用户运动记录
     * @param user_id
     * @return
     */
    public void getUserRecord(String user_id){
        Intent intent = new Intent(mActivity, HistoryListActivity.class);
        intent.putExtra("user_id", user_id);
        mActivity.startActivity(intent);
    }

}
