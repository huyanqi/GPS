package com.codoon.clubgps.util;

import android.widget.Toast;

import com.codoon.clubgps.application.GPSApplication;

/**
 * Created by Frankie on 2017/1/5.
 */

public class ToastUtil {

    public static void showToast(int strRes) {
        showToast(GPSApplication.getContext().getString(strRes));
    }

    public static void showToast(String str) {
        Toast.makeText(GPSApplication.getContext(), str, Toast.LENGTH_SHORT).show();
    }

}
