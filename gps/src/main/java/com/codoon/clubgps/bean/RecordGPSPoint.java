package com.codoon.clubgps.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Frankie on 2017/1/5.
 *
 * 只记录路线点
 *
 */

public class RecordGPSPoint extends DataSupport {

    private double latitude;//纬度
    private double longitude;//经度

    private double distance;//距离上一个坐标点的距离,单位:m
    private long pace;//当前配速,单位:s

    private long timestamp;//坐标产生的时间
    private int index;//本次跑步产生的第几个点

    private double speed;//当前速度,单位:km/h
    private double calories;//本次消耗的卡路里,单位:kcal

    public RecordGPSPoint (GPSPoint gpsPoint){
        this.latitude = gpsPoint.getLatitude();
        this.longitude = gpsPoint.getLongitude();
        this.distance = gpsPoint.getDistance();
        this.pace = gpsPoint.getPace();
        this.timestamp = gpsPoint.getTimestamp();
        this.index = gpsPoint.getIndex();
        this.speed = gpsPoint.getSpeed();
        this.calories = gpsPoint.getCalories();
    }

    private RecordGPSPoint(){}

}
