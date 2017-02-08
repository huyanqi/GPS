package com.codoon.clubcodoongps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Gravity;
import android.widget.TextView;

import com.codoon.clubgps.util.CommonUtil;

/**
 * Created by Frankie on 2017/1/23.
 *
 * 1.画灰色空心圆
 *
 * 2.画蓝色空心圆圈
 *
 * 3.根据bgType看画什么背景
 *
 */

public class CalendarView extends TextView {

    private CalendarPojo mPojo;
    private Paint circlePaint;
    private int mHeight;
    private int mWidth;

    public CalendarView(Context context, CalendarPojo calendarPojo) {
        super(context);
        this.mPojo = calendarPojo;
        setTextColor(calendarPojo.getRatio() == 0 ? 0xFF999999 : 0xFF666666);
        setGravity(Gravity.CENTER);
        setTextSize(13);
        setText(calendarPojo.getText());

        circlePaint = new Paint();
        circlePaint.setColor(0xFFEEEEEE);
        circlePaint.setStyle(Paint.Style.STROKE);//空心
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(2);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int offset = (int) (circlePaint.getStrokeWidth() / 2);
        RectF drawRect = new RectF();
        int left = mWidth / 2 - mHeight / 2;
        int top = 0;
        int right = mWidth / 2 + mHeight / 2;
        int bottom = mHeight;
        drawRect.set(left, top, right, bottom);

        int radiu = (int) (drawRect.centerY() - offset);

        //1.画灰色空心圆
        canvas.drawCircle(drawRect.centerX(), drawRect.centerY(), radiu, circlePaint);

        //2.画蓝色空心圆圈
        circlePaint.setColor(0xFF00B9FF);
        drawRect.set(drawRect.left + offset, drawRect.top + offset, drawRect.right - offset, drawRect.bottom - offset);
        canvas.drawArc(drawRect, -90, mPojo.getRatio() * 360, false, circlePaint);

        //3.画背景
        if(mPojo.getBgType() == 1){
            //画一圈蓝色
            circlePaint.setStyle(Paint.Style.FILL);//改为实心
            circlePaint.setColor(0x4D00B9FF);
            canvas.drawCircle(drawRect.centerX(), drawRect.centerY(), radiu - CommonUtil.dip2px(4), circlePaint);
        }

        super.onDraw(canvas);
    }

}
