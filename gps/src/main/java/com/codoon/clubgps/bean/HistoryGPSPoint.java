package com.codoon.clubgps.bean;

import com.amap.api.maps.model.LatLng;

import org.litepal.crud.DataSupport;

/**
 * Created by Frankie on 2017/1/5.
 *
 * 只记录路线点
 *
 */

public class HistoryGPSPoint extends DataSupport {

    private String parent_uuid;//所属记录的UUID

    private double latitude;//纬度
    private double longitude;//经度

    private double total_length;//总跑步距离
    private double distance;//距离上一个坐标点的距离,单位:m
    private long pace;//当前配速,单位:s

    private long timestamp;//坐标产生的时间
    private int index;//本次跑步产生的第几个点

    private double speed;//当前速度,单位:km/h
    private double calories;//本次消耗的卡路里,单位:kcal

    public HistoryGPSPoint(String parent_uuid, GPSPoint gpsPoint){
        this.parent_uuid = parent_uuid;
        this.latitude = gpsPoint.getLatitude();
        this.longitude = gpsPoint.getLongitude();
        this.distance = gpsPoint.getDistance();
        this.pace = gpsPoint.getPace();
        this.timestamp = gpsPoint.getTimestamp();
        this.index = gpsPoint.getIndex();
        this.speed = gpsPoint.getSpeed();
        this.calories = gpsPoint.getCalories();
        this.total_length = gpsPoint.getTotal_length();
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

    public double getTotal_length() {
        return total_length;
    }

    private HistoryGPSPoint(){}

    public LatLng getLatlng(){
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return "HistoryGPSPoint{" +
                "parent_uuid='" + parent_uuid + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", total_length=" + total_length +
                ", distance=" + distance +
                ", pace=" + pace +
                ", timestamp=" + timestamp +
                ", index=" + index +
                ", speed=" + speed +
                ", calories=" + calories +
                '}';
    }
}
