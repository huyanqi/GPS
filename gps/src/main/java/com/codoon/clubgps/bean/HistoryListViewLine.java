package com.codoon.clubgps.bean;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import com.codoon.clubgps.util.CommonUtil;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 运动历史列表-顶部控件-viewpager item里每一列的竖线
 */

public class HistoryListViewLine {

    private int width;//线条的宽度
    private int height;//线条的高度
    private String topText = "";//顶部显示的文本
    private String bottomText = "";//底部显示的文本

    private String timeTag;//标记具体哪一天，格式:yyyyMMdd

    private float x;
    private float y;
    private int chatRectBottom;

    private int gradientStartColor = 0Xfff8832e;//线条渐变色
    private int gradientEndColor = 0xfff35566;
    private Shader mShader;

    private Paint topTextPaint;
    private Paint bottomTextPaint;
    private Paint gradientPaint;

    private HistoryListViewLine(){}
    public HistoryListViewLine (int x, int width, String timeTag, int chatRectBottom, int textColor) {
        this.x = x;
        this.width = width;
        this.timeTag = timeTag;
        this.chatRectBottom = chatRectBottom;
        gradientPaint = new Paint();
        //gradientPaint.setShadowLayer(10, 10, 10, );
        gradientPaint.setAntiAlias(true);

        bottomTextPaint = new Paint();
        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setColor(textColor);
        bottomTextPaint.setTextSize(CommonUtil.dip2px(12));
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);

        topTextPaint = new Paint();
        topTextPaint.setAntiAlias(true);
        topTextPaint.setColor(gradientStartColor);
        topTextPaint.setTextSize(CommonUtil.dip2px(10));
        topTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void updateHeight(int height){
        y = chatRectBottom - height;
        this.height = height;
        mShader = new LinearGradient(width , y, width, y + height, gradientStartColor, gradientEndColor, Shader.TileMode.MIRROR);
        gradientPaint.setShader(mShader);
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public String getTimeTag() {
        return timeTag;
    }

    public void draw(Canvas canvas){
        //1.画线条
        canvas.drawRect(x , y, x + width, y + height, gradientPaint);

        //2.画底部文字
        canvas.drawText(bottomText, x + width / 2 , chatRectBottom + CommonUtil.dip2px(16), bottomTextPaint);

        //3.画顶部文字
        canvas.drawText(topText, x + width / 2 , y - CommonUtil.dip2px(5), topTextPaint);
    }

}
