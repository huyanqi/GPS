package com.codoon.clubcodoongps.widget;

/**
 * Created by Frankie on 2017/1/23.
 */

public class CalendarPojo {

    private float ratio;//进度条的百分不，范围:0-1
    private String text;//显示在控件中心的文字
    private int bgType;//0:不要背景 1:一圈蓝色

    private CalendarPojo() {
    }

    public CalendarPojo(float ratio, String text, int bgType) {
        this.ratio = ratio;
        this.text = text;
        this.bgType = bgType;
    }

    public float getRatio() {
        return ratio;
    }

    public String getText() {
        return text;
    }

    public int getBgType() {
        return bgType;
    }
}