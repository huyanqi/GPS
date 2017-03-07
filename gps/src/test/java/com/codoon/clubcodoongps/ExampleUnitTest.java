package com.codoon.clubcodoongps;

import com.codoon.clubgps.util.TestUtil;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testgogogo() throws Exception {
        TestUtil testUtil = new TestUtil();
        System.out.println(testUtil.numToCN(100));
    }

    @Test
    public void test_time (){
        TestUtil testUtil = new TestUtil();
        System.out.println(testUtil.timeToText(3601));
    }

    public String getHistoryDisplayWeekTime(String time, int day_count) {
        String displayTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyw");
        SimpleDateFormat weekFormat = new SimpleDateFormat("M月d日");
        Date currentDate = new Date();
        if (sdf.format(currentDate).equals(time)) {
            //如果是本周，直接显示"本周"文案
            displayTime = "本周";
        } else {
            //不是本周，显示M月d日-M月d日格式
            try {
                Date startDate = sdf.parse(time);
                Calendar weekCalendar = Calendar.getInstance();
                weekCalendar.setTime(startDate);
                weekCalendar.add(Calendar.DAY_OF_YEAR, 1);
                startDate = weekCalendar.getTime();
                displayTime += weekFormat.format(startDate) + " - ";
                weekCalendar.add(Calendar.DAY_OF_YEAR, day_count - 1);
                displayTime += weekFormat.format(weekCalendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return displayTime;

    }

    public String getWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            week --;
            if(week == 0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                year --;
                try {
                    return getWeek(sdf.parse(year+"1231"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return year+""+week;
    }

    @Test
    public void test2() throws Exception {
        String yyyyMMdd = "20170101";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(yyyyMMdd);
        sdf = new SimpleDateFormat("yyyyw");
        System.out.println(sdf.format(date));
    }

    @Test
    public void test3() throws Exception {
        long timestamp = 1476069692209l;
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyw");
        System.out.println(sdf.format(date));
        sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(sdf.format(date));
    }

    @Test
    public void test4() throws Exception {
        String yyyyw = "201642";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyw");
        Date date = sdf.parse(yyyyw);

        sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(sdf.format(date));
    }

}