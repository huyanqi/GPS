package com.codoon.clubgps.bean;

/**
 * Created by Frankie on 2017/1/12.
 *
 * 历史详情->配速图表用到的bean
 */

public class PaceChatBean {

    private int kmNumber;//公里数
    private String avg_pace;//当前公里数的平均配速
    private long duration;//当前公里数的耗时，单位:s

    private String kmText;//中间穿插的tag,如:5公里 00:30:20、半程马拉松 01:20:33

    public int getKmNumber() {
        return kmNumber;
    }

    public void setKmNumber(int kmNumber) {
        this.kmNumber = kmNumber;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getAvg_pace() {
        return avg_pace;
    }

    public void setAvg_pace(String avg_pace) {
        this.avg_pace = avg_pace;
    }

    public String getKmText() {
        return kmText;
    }

    public void setKmText(String kmText) {
        this.kmText = kmText;
    }

    @Override
    public String toString() {
        return "PaceChatBean{" +
                "kmNumber=" + kmNumber +
                ", avg_pace=" + avg_pace +
                ", duration=" + duration +
                ", kmText=" + kmText +
                '}';
    }
}
