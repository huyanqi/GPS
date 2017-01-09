package com.codoon.clubcodoongps;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testgogogo() throws Exception {
        System.out.println(Math.rint(1.4));
    }

    @Test
    public void test2() throws Exception {
        String yyyyMMdd = "20161009";
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