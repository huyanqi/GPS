package com.codoon.clubgps.bean;

import com.codoon.clubgps.application.GPSApplication;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Frankie on 2017/1/5.
 *
 * 记录运动的详情(不记录路线点)
 */

public class RecordDetail extends DataSupport {

    private String record_id;
    private String user_id;

    private double distance;//运动总长
    private long max_pace;//最大配速,单位:s/km
    private long avg_pace;//平均配速
    private double max_speed;//最大速度,单位:km/h
    private double avg_speed;//平均速度
    private long duration;//持续时间，单位:s
    private double calories;//消耗卡路里总数

    private long startTimesStamp;//跑步开始时间(不是记录生成时间)
    private long saveTimesStamp;//跑步开始时间(不是记录生成时间)

    @Column(ignore = true)
    private List<RecordGPSPoint> recordGPSPointList;

    /**
     * 生成一个完整的运动记录
     * @param gpsPointList 本次运动所有路线点
     * @return
     */
    public RecordDetail build(List<GPSPoint> gpsPointList){
        recordGPSPointList = new ArrayList<>();
        //生成记录ID
        record_id = UUID.randomUUID().toString();
        //设置所属用户
        user_id = GPSApplication.getAppContext().getUser_id();
        //遍历所有坐标点设置对象属性
        long pace = 0;//总配速
        double speed = 0;//总速度
        for(GPSPoint gpsPoint : gpsPointList){
            recordGPSPointList.add(new RecordGPSPoint(gpsPoint));
            distance += gpsPoint.getDistance();

            pace += gpsPoint.getPace();
            max_pace = gpsPoint.getPace() > max_pace ? gpsPoint.getPace() : max_pace;

            speed += gpsPoint.getSpeed();
            max_speed = gpsPoint.getSpeed() > max_speed ? gpsPoint.getSpeed() : max_speed;

            calories += gpsPoint.getCalories();
        }

        startTimesStamp = gpsPointList.get(0).getTimestamp();
        saveTimesStamp = System.currentTimeMillis();
        int size = gpsPointList.size();
        duration = (gpsPointList.get(size - 1).getTimestamp() - startTimesStamp) / 1000;
        avg_pace = pace / size;
        avg_speed = speed / size;

        return this;
    }

    public List<RecordGPSPoint> getRecordGPSPointList() {
        return recordGPSPointList;
    }

    @Override
    public String toString() {
        return "RecordDetail{" +
                "record_id='" + record_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", distance=" + distance +
                ", max_pace=" + max_pace +
                ", avg_pace=" + avg_pace +
                ", max_speed=" + max_speed +
                ", avg_speed=" + avg_speed +
                ", duration=" + duration +
                ", calories=" + calories +
                ", startTimesStamp=" + startTimesStamp +
                ", saveTimesStamp=" + saveTimesStamp +
                '}';
    }
}
