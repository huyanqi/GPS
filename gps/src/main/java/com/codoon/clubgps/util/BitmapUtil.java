package com.codoon.clubgps.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Created by Frankie on 2017/1/11.
 */

public class BitmapUtil {

    /**
     * 显示公里牌数字
     *
     * @param icon
     * @param count
     * @return
     */
    public static Bitmap generatorCountIcon(Bitmap icon, int count) {
        //初始化画布
        Bitmap contactIcon = Bitmap.createBitmap(icon.getWidth(), icon.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(contactIcon);

        //拷贝图片
        Paint iconPaint = new Paint();
        iconPaint.setDither(true);//防抖动
        iconPaint.setFilterBitmap(true);//用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
//        Rect src=new Rect(0, 0, ic_launcher.getWidth(), ic_launcher.getHeight());
        Rect dst = new Rect(0, 0, icon.getWidth(), icon.getWidth());
        canvas.drawBitmap(icon, null, dst, iconPaint);

        //启用抗锯齿和使用设备的文本字距
        Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        countPaint.setColor(Color.WHITE);
        countPaint.setTextSize(CommonUtil.dip2px(10));
        float textLen = countPaint.measureText(String.valueOf(count));

        Paint.FontMetrics fm = countPaint.getFontMetrics();
        //计算文字高度
        float fontHeight = fm.bottom - fm.top;
        //计算文字baseline
        float textBaseY = icon.getHeight() - (icon.getHeight() - fontHeight) / 2 - fm.bottom;
        countPaint.setTypeface(Typeface.DEFAULT_BOLD);
        int textLeft = (int) ((icon.getWidth() - textLen) / 2);
        canvas.drawText(String.valueOf(count), textLeft, textBaseY, countPaint);
        return contactIcon;
    }

}
