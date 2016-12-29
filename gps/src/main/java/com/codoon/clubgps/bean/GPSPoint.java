package com.codoon.clubgps.bean;

import com.amap.api.maps2d.model.LatLng;
import com.codoon.clubgps.util.GPSUtil;

import java.util.Date;

/**
 * Created by Frankie on 2016/12/28.
 *
 * gps点
 *
 * 记录跑步时的每一个点
 */

public class GPSPoint {

    private double latitude;//纬度
    private double longitude;//经度

    private double distance;//距离上一个坐标点的距离,单位:m
    private long pace;//当前配速

    private long timestamp;//坐标产生的时间
    private int index;//本次跑步产生的第几个点

    private boolean is_running;//正在跑步中(非暂停)

    private LatLng latLng;

    private GPSPoint(){}

    /**
     *
     * @param lat,lng 当前坐标点
     * @param lastGPSPoint 上一次的坐标点
     */
    public GPSPoint(double lat, double lng, GPSPoint lastGPSPoint, boolean isRunning){
        this.latitude = lat;
        this.longitude = lng;
        timestamp = new Date().getTime();
        if(lastGPSPoint != null){
            //离上一个点的距离,单位m
            distance = GPSUtil.computeDistanceBetween(new LatLng(lat, lng), new LatLng(lastGPSPoint.latitude, lastGPSPoint.longitude));
            //离上一个点的时间,单位s
            long duration = (timestamp - lastGPSPoint.getTimestamp()) / 1000;
            //计算配速
            pace = (1000 / Math.round(distance)) * duration;
            index = lastGPSPoint.getIndex();//临时将点标记为上一个坐标的点，如果是有效点，则+1
        }
        latLng = new LatLng(lat, lng);
        this.is_running = isRunning;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance() {
        return distance;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setIs_running(boolean is_running) {
        this.is_running = is_running;
    }

    public long getPace() {
        return pace;
    }

    @Override
    public String toString() {
        return "GPSPoint{" +
                "distance=" + distance +
                ", pace=" + pace +
                ", index=" + index +
                ", is_running=" + is_running +
                '}';
    }
}
