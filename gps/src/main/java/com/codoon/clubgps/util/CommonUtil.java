package com.codoon.clubgps.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.codoon.clubgps.R;
import com.codoon.clubgps.application.GPSApplication;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        if (context == null) {
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

    public static String getDistanceKm(double m) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(m / 1000);
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

    /**
     * 时间戳转称可以展示的时间
     *
     * @param timestamp
     * @return
     */
    public static String parseTime(long timestamp) {
        Date date = new Date(timestamp);
        Context context = GPSApplication.getContext();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + context.getString(R.string.date_year)
                + "MM" + context.getString(R.string.date_month)
                + "dd" + context.getString(R.string.date_day)
                + " HH" + context.getString(R.string.date_hour)
                + "mm" + context.getString(R.string.date_minute));
        return sdf.format(date);
    }

    /**
     * 将时间戳解析成yyyyMM格式 主要用在历史列表分组
     *
     * @param timestamp
     * @return
     */
    public static String parseyyyyMM(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyM");
        return sdf.format(date);
    }

    /**
     * 通过字符串获取资源ID名称
     *
     * @param imageName
     * @param type      mipmap or string...
     * @return
     */
    public static int getResource(String imageName, String type) {
        Context ctx = GPSApplication.getContext();
        int resId = ctx.getResources().getIdentifier(imageName, type, ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }


    /**
     * 获取历史统计里周时间的展示文本
     *
     * @param time      格式:yyyyw
     * @param day_count 天数，一般传7天
     * @return
     */
    public static String getHistoryDisplayWeekTime(String time, int day_count) {
        String displayTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyw");
        SimpleDateFormat weekFormat = new SimpleDateFormat("M" + GPSApplication.getAppContext().getString(R.string.date_month) + "d" + GPSApplication.getAppContext().getString(R.string.date_day));
        Date currentDate = new Date();
        if (sdf.format(currentDate).equals(time)) {
            //如果是本周，直接显示"本周"文案
            displayTime = GPSApplication.getAppContext().getString(R.string.current_week);
        } else {
            //不是本周，显示M月d日-M月d日格式
            try {
                Date startDate = sdf.parse(time);
                Calendar weekCalendar = Calendar.getInstance();
                weekCalendar.setTime(startDate);
                displayTime += weekFormat.format(startDate) + " - ";
                weekCalendar.add(Calendar.DAY_OF_YEAR, day_count - 1);
                displayTime += weekFormat.format(weekCalendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return displayTime;

    }

    /**
     * 获取历史统计里月时间的展示文本
     *
     * @param yyyyM     格式:yyyyM
     * @return
     */
    public static String getHistoryDisplayMonthTime(String yyyyM) {
        String displayTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyM");
        Date currentDate = new Date();
        if (sdf.format(currentDate).equals(yyyyM)) {
            //如果是本月，直接显示"本月"文案
            displayTime = GPSApplication.getAppContext().getString(R.string.current_month);
        } else {
            displayTime = yyyyM.substring(0, 4) + GPSApplication.getContext().getString(R.string.date_year) +
                    yyyyM.substring(4, yyyyM.length()) + GPSApplication.getContext().getString(R.string.date_month);
        }
        return displayTime;
    }

    /**
     * 获取指定月的天数
     *
     * @param yyyyM 格式:yyyyM
     * @return
     */
    public static int getMonthDayCount(String yyyyM) {
        int year = Integer.parseInt(yyyyM.substring(0, 4));
        int month = Integer.parseInt(yyyyM.substring(4, yyyyM.length()));
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}
