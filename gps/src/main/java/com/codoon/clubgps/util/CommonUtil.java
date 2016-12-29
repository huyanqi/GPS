package com.codoon.clubgps.util;

import android.content.Context;
import android.util.DisplayMetrics;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by huan on 15/5/27.
 */

public class CommonUtil {

    /**
     * 获取手机屏幕宽度
     *
     * @param context
     * @return
     */

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return metrics.widthPixels;
    }

    /**
     * 获取手机屏幕高度
     *
     * @param context
     * @return
     */

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return metrics.heightPixels;
    }

    /**
     * 返回屏幕尺寸
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */

    public static int dip2px(Context context, float dipValue) {
        if(context == null){
            return 1;
        }
        float scale = getDisplayMetrics(context).density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取00:00:00格式的时间
     *
     * @param time ：单位:秒
     * @return
     */
    public static String getPeriodTime(long time) {//88
        StringBuilder sb = new StringBuilder();
        int hour = (int) (time / (60 * 60));
        int min = (int) (time % (60 * 60)) / (60);//1
        int sec = (int) (time % (60));  //28

        if (hour < 10) {
            sb.append("0" + hour + ":");
        } else {
            sb.append(hour + ":");
        }
        if (min < 10) {
            sb.append("0" + min + ":");
        } else {
            sb.append(min + ":");
        }
        if (sec < 10) {
            sb.append("0" + sec);
        } else {
            sb.append(sec + "");
        }
        return sb.toString();
    }

    /**
     * 使用DecimalFormat,保留小数点后两位
     */
    public static String format2(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }

    /**
     * 平均配速转换
     *
     * @param time 单位:秒
     * @return
     */
    public static String getPaceTimeStr(long time) {
        int min = (int) (time / 60);
        int second = (int) (time % 60);
        String tmp = "";
        if (min < 10) {
            tmp += "0" + min;
        } else {
            tmp += String.valueOf(min);
        }
        tmp += "'";
        if (second < 10) {
            tmp += "0" + second;

        } else
            tmp += String.valueOf(second);
        tmp += "\"";
        return tmp;


    }

}
