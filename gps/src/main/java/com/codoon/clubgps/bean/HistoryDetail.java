package com.codoon.clubgps.bean;

import android.graphics.Bitmap;

import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.util.FileUtil;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Frankie on 2017/1/5.
 *
 * 记录运动的详情(不记录路线点)
 */

public class HistoryDetail extends DataSupport {

    private String uuid;
    private String user_id;

    private long max_pace;//最大配速,单位:s/km
    private long avg_pace;//平均配速
    private double max_speed;//最大速度,单位:km/h
    private double avg_speed;//平均速度

    private double total_length;//运动总长
    private long total_time;//持续时间，单位:s
    private double total_calories;//消耗卡路里总数
    private String thum;//缩略图地址

    private long startTimesStamp;//跑步开始时间(不是记录生成时间)
    private long saveTimesStamp;//跑步开始时间(不是记录生成时间)

    @Column(ignore = true)
    private List<HistoryGPSPoint> recordGPSPointList;

    /**
     * 生成一个完整的运动记录
     * @param gpsPointList 本次运动所有路线点
     * @return
     */
    public HistoryDetail build(Bitmap bitmap, long total_time, long avg_pace, List<GPSPoint> gpsPointList){
        recordGPSPointList = new ArrayList<>();
        this.total_time = total_time;
        this.avg_pace = avg_pace;
        //生成记录ID
        uuid = UUID.randomUUID().toString();
        //设置所属用户
        user_id = GPSApplication.getAppContext().getUser_id();
        //遍历所有坐标点设置对象属性
        double speed = 0;//总速度
        for(GPSPoint gpsPoint : gpsPointList){
            recordGPSPointList.add(new HistoryGPSPoint(gpsPoint));
            total_length += gpsPoint.getDistance();

            max_pace = gpsPoint.getPace() > max_pace ? gpsPoint.getPace() : max_pace;

            speed += gpsPoint.getSpeed();
            max_speed = gpsPoint.getSpeed() > max_speed ? gpsPoint.getSpeed() : max_speed;

            total_calories += gpsPoint.getCalories();
        }

        try {
            startTimesStamp = new SimpleDateFormat("yyyy-M-dd").parse("2017-1-7").getTime();//gpsPointList.get(0).getTimestamp();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        saveTimesStamp = System.currentTimeMillis();
        int size = gpsPointList.size();
        avg_speed = speed / size;

        thum = "file:///"+FileUtil.saveGPSThum(bitmap, uuid).getAbsolutePath();

        return this;
    }

    public List<HistoryGPSPoint> getRecordGPSPointList() {
        return recordGPSPointList;
    }

    public String getThum() {
        return thum;
    }

    public String getUuid() {
        return uuid;
    }

    public long getMax_pace() {
        return max_pace;
    }

    public long getAvg_pace() {
        return avg_pace;
    }

    public double getMax_speed() {
        return max_speed;
    }

    public double getAvg_speed() {
        return avg_speed;
    }

    public double getTotal_length() {
        return total_length;
    }

    public long getTotal_time() {
        return total_time;
    }

    public double getTotal_calories() {
        return total_calories;
    }

    public long getStartTimesStamp() {
        return startTimesStamp;
    }

    public long getSaveTimesStamp() {
        return saveTimesStamp;
    }

    public static List<HistoryDetail> getHistoryList(String user_id){
        List<HistoryDetail> list = DataSupport.where("user_id = ?", user_id).order("saveTimesStamp DESC").find(HistoryDetail.class);
        return list;
    }

    @Override
    public String toString() {
        return "HistoryDetail{" +
                "uuid='" + uuid + '\'' +
                ", user_id='" + user_id + '\'' +
                ", total_length=" + total_length +
                ", max_pace=" + max_pace +
                ", avg_pace=" + avg_pace +
                ", max_speed=" + max_speed +
                ", avg_speed=" + avg_speed +
                ", total_time=" + total_time +
                ", total_calories=" + total_calories +
                ", startTimesStamp=" + startTimesStamp +
                ", saveTimesStamp=" + saveTimesStamp +
                '}';
    }

}
