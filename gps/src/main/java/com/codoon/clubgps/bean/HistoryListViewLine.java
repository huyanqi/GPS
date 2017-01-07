package com.codoon.clubgps.bean;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

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

    private int gradientStartColor = 0Xfff8832e;//线条渐变色
    private int gradientEndColor = 0xfff35566;
    private Shader mShader;
    private Paint gradientPaint;

    private HistoryListViewLine(){}
    public HistoryListViewLine (int x, int y, int width, int height, String topText, String bottomText) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.topText = topText;
        this.bottomText = bottomText;
        gradientPaint = new Paint();
        mShader = new LinearGradient(width ,0, width, height,new int[] {gradientStartColor, gradientEndColor},null,Shader.TileMode.MIRROR);
        gradientPaint.setShader(mShader);
        //gradientPaint.setShadowLayer(10, 10, 10, );
        gradientPaint.setAntiAlias(true);
    }

    public void draw(Canvas canvas){

        canvas.drawRect(x , y, x + width, y + height, gradientPaint);
    }

}
