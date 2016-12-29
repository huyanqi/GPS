package com.codoon.clubgps.util;

import android.Manifest;

/**
 * Created by Frankie on 2016/12/28.
 */

public class Constant {

    /**
     * 必须的权限
     */
    public static final String[] mustPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};

    /**
     * 默认的当前位置坐标点
     */
    public static final double DEFAULT_LAT = 30.5272112352;
    public static final double DEFAULT_LNG = 104.0687161241;

    /**
     * 地图自动移动时间,单位:ms
     */
    public static final long MAP_AUTOMOVE_TIME = 4500;

}
