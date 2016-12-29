package com.codoon.clubgps.util;

import android.content.SharedPreferences;

import com.amap.api.maps2d.model.LatLng;
import com.codoon.clubgps.application.GPSApplication;

/**
 * Created by Frankie on 2016/12/28.
 */

public class CacheUtil {

    private static CacheUtil sCacheUtil;
    private static SharedPreferences mSharedPreferences;

    private CacheUtil() {
    }

    public static CacheUtil getInstance() {
        if (sCacheUtil == null) {
            sCacheUtil = new CacheUtil();
            mSharedPreferences = GPSApplication.getAppContext().getSharedPreferences();
        }
        return sCacheUtil;
    }

    /**
     * 缓存我的当前位置
     * @param lat
     * @param lng
     */
    public void updateMyCacheLocation(double lat, double lng) {
        mSharedPreferences.edit()
                .putLong("my_lat", Double.doubleToRawLongBits(lat))
                .putLong("my_lng", Double.doubleToRawLongBits(lng))
                .commit();
    }

    /**
     * 获取我缓存的当前位置
     * @return
     */
    public LatLng getMyCacheLocation() {
        double my_lat = Double.longBitsToDouble(mSharedPreferences.getLong("my_lat", 0));
        double my_lng = Double.longBitsToDouble(mSharedPreferences.getLong("my_lng", 0));
        if(my_lat == 0 || my_lng == 0){
            my_lat = Constant.DEFAULT_LAT;
            my_lng = Constant.DEFAULT_LNG;
        }
        return new LatLng(my_lat, my_lng);
    }

}
