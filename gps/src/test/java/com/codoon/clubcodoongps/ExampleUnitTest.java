package com.codoon.clubcodoongps;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testgogogo() throws Exception {
        double num = 301.32;
        System.out.println(Math.floor(301.32));

        int count = 0;
        while(num > 0){
            num /= 10;
            count++;
        }

        System.out.println("位数:"+count);
    }
}