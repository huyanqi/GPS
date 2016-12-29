package com.codoon.clubgps.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Frankie on 2016/12/27.
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

}
