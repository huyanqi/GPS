package com.codoon.clubgps.bean;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.codoon.clubgps.util.LogUtil;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Frankie on 2016/12/28.
 *
 * gps点
 *
 * 记录跑步时的每一个点
 */

public class GPSPoint extends DataSupport {

    @Column(ignore = true)
    private static final String TAG = "GPSPoint";

    private double latitude;//纬度
    private double longitude;//经度

    private double total_length;//总跑步距离
    private double distance;//距离上一个坐标点的距离,单位:m
    private long pace;//当前配速,单位:s

    private long timestamp;//坐标产生的时间
    private int index;//本次跑步产生的第几个点

    private double speed;//当前速度,单位:km/h
    private double calories;//本次消耗的卡路里,单位:kcal

    private boolean is_running;//正在跑步中(非暂停)

    private boolean is_valid = true;//是否为有效的点

    @Column(ignore = true)
    private LatLng latLng;

    public GPSPoint(){}

    /**
     *
     * @param lat,lng 当前坐标点
     * @param lastGPSPoint 上一次的坐标点
     */
    public GPSPoint(double lat, double lng, GPSPoint lastGPSPoint, boolean isRunning){
        this.latitude = lat;
        this.longitude = lng;
        this.is_running = isRunning;
        latLng = new LatLng(lat, lng);
        timestamp = new Date().getTime();

        if(lastGPSPoint != null){
            //计算离上一个点的距离,单位m
            distance = AMapUtils.calculateLineDistance(new LatLng(lat, lng), new LatLng(lastGPSPoint.latitude, lastGPSPoint.longitude));
            index = lastGPSPoint.getIndex();//临时将点标记为上一个坐标的点，如果是有效点，则+1
            checkValid(lastGPSPoint);//验证点是否有效
            LogUtil.i(TAG, "验证结果:"+is_valid);

            //离上一个点的时间,单位s
            double duration = Double.parseDouble(new DecimalFormat("#.##").format((timestamp - lastGPSPoint.getTimestamp()) / 1000d));

            //有效的点
            if(is_valid){
                index++;
                //计算离起点的距离
                total_length = distance + lastGPSPoint.total_length;
                //计算配速
                LogUtil.i(TAG, "distance="+distance+",计算配速:1000/"+Math.round(distance)+"*"+duration);
                pace = (long) ((1000d / distance) * duration);

                //计算卡路里消耗
                calories = 65 * (distance / 1000) * 1.036;//暂时写死体重65kg，和K值 K = 30 / 速度(分钟/400米)

                //计算速度，单位 km/h
                speed = (distance / 1000 )/ (duration / 3600);
            }

        }

    }

    private void checkValid(GPSPoint lastGPSPoint) {
        LogUtil.i(TAG, "开始验证有效性");
        if (distance < 1) {
            LogUtil.i(TAG, "放弃本次坐标点,原因:距离=" + distance + "m");
            is_valid = false;
            return;
        }

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

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setIs_running(boolean is_running) {
        this.is_running = is_running;
    }

    public long getPace() {
        return pace;
    }

    public boolean is_valid() {
        return is_valid;
    }

    public double getSpeed() {
        return speed;
    }

    public double getCalories() {
        return calories;
    }

    public double getTotal_length() {
        return total_length;
    }

    /**
     * 获取有效的路径点
     * @return
     */
    public static List<GPSPoint> getValidDatas(){
        return DataSupport.where("is_valid = 1").order("index").find(GPSPoint.class);
    }

    @Override
    public String toString() {
        return "GPSPoint{" +
                "distance=" + distance +
                ", pace=" + pace +
                ", index=" + index +
                ", is_running=" + is_running +
                ", speed=" + speed +
                ", calories=" + calories +
                '}';
    }
}
