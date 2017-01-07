package com.codoon.clubgps.bean;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.util.CommonUtil;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 运动历史列表-顶部控件-viewpager item里每一列的竖线
 */

public class HistoryListViewLine {

    private int width;//线条的宽度
    private int height;//线条的高度
    private String topText;//顶部显示的文本
    private String bottomText;//底部显示的文本

    private int x;
    private int y;
    private int chatRectBottom;

    private int gradientStartColor = 0xfff35566;//线条渐变色
    private int gradientEndColor = 0Xfff8832e;
    private Shader mShader;

    private Paint bottomTextPaint;
    private Paint gradientPaint;

    private HistoryListViewLine(){}
    public HistoryListViewLine (int x, int width, String topText, String bottomText) {
        this.x = x;
        this.width = width;
        this.topText = topText;
        this.bottomText = bottomText;
        gradientPaint = new Paint();
        //gradientPaint.setShadowLayer(10, 10, 10, );
        gradientPaint.setAntiAlias(true);

        bottomTextPaint = new Paint();
        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setColor(Color.BLUE);
        bottomTextPaint.setTextSize(CommonUtil.dip2px(GPSApplication.getContext(), 11));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void updateHeight(int chatRectBottom, int height){
        this.chatRectBottom = chatRectBottom;
        y = chatRectBottom - height;
        this.height = height;
        mShader = new LinearGradient(width ,0, width, height, new int[] {gradientStartColor, gradientEndColor},null,Shader.TileMode.MIRROR);
        gradientPaint.setShader(mShader);
    }

    public void draw(Canvas canvas){
        //1.画线条
        canvas.drawRect(x , y, x + width, y + height, gradientPaint);

        //2.画底部文字
        canvas.drawText(bottomText, x + width / 2 , chatRectBottom + CommonUtil.dip2px(GPSApplication.getContext(), 16), bottomTextPaint);
    }

}
