package com.codoon.clubcodoongps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.codoon.clubcodoongps.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Frankie on 2017/2/23.
 */

public class StepFreqView extends View {

    private List<Integer> datas = new ArrayList<>();
    private int mWidth, mHeight;
    private Rect drawRect;
    private int min = 0, max;//最小值、最大值
    private double xOffset, yOffset;//每个值在x,y方向的偏移量
    private Paint mPaint;
    private Paint textPaint;
    private Paint dottePaint;
    private Paint linePaint;
    private int defaultColor = 0xFF79798E;
    private int leftTextPadding = 10;//单位:px,可以改成dp,但是要自己计算

    public StepFreqView(Context context, AttributeSet attrs) {
        super(context, attrs);
        textPaint = new Paint();
        textPaint.setColor(defaultColor);
        textPaint.setTextSize(30);
        textPaint.setAntiAlias(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //设置画笔宽度
        mPaint.setStrokeWidth(1);
        //消除锯齿
        mPaint.setAntiAlias(true);

        dottePaint = new Paint();
        dottePaint.setStyle(Paint.Style.STROKE);
        dottePaint.setColor(ContextCompat.getColor(context, R.color.dottet_line));
        dottePaint.setStrokeWidth(1);
        PathEffect effects = new DashPathEffect(new float[]{ 6, 6, 6, 6}, 1);
        dottePaint.setPathEffect(effects);
        dottePaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(0xFFBEAD61);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(3);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setPathEffect(new CornerPathEffect(5));

    }

    public void setDatas(List<Integer> datas) {
        this.datas = datas;
        if (datas == null || datas.size() == 0 || mWidth == 0 || mHeight == 0) return;
        drawRect = new Rect();
        max = Collections.max(datas);

        //计算左侧文本需要的最大宽度
        int left_max_w = 0;
        int w;
        for(double data : datas){
            w = getTextWitdh(data+"");
            left_max_w = Math.max(left_max_w, w);
        }

        //计算画图区域的边距
        int left = left_max_w + leftTextPadding * 2;
        int top = (int) mPaint.getStrokeWidth();
        int right = mWidth;
        int bottom = mHeight - (int) mPaint.getStrokeWidth();
        drawRect.set(left, top, right, bottom);

        //计算每个值在x,y上的偏移量
        xOffset = drawRect.width() / Double.parseDouble(datas.size()+"");
        yOffset = drawRect.height() / Double.parseDouble((max - min)+"");

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        setDatas(datas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(datas.size() == 0) return;
        drawLeftValue(canvas);
        drawRect(canvas);
        drawRuleLines(canvas);
        drawLines(canvas);
    }

    /**
     * 绘制左边值，只画最大、最小值
     */
    private void drawLeftValue(Canvas canvas){
        canvas.drawText(max+"", leftTextPadding, getY(max) + getTextHeight(max+""), textPaint);//文字顶部和rect顶部对其
        canvas.drawText(min+"", leftTextPadding, getY(min), textPaint);//文字底部和rect底部对其
    }

    /**
     * 绘制路径区域
     */
    private void drawRect(Canvas canvas){
        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(drawRect, mPaint);
    }

    /**
     * 绘制尺度线
     */
    private void drawRuleLines(Canvas canvas){
        mPaint.setPathEffect(null);
        mPaint.setColor(defaultColor);
        float y = getY(max);
        canvas.drawLine(drawRect.left, y, drawRect.right, y, mPaint);
        y = getY(min);
        canvas.drawLine(drawRect.left, y, drawRect.right, y, mPaint);
        y = getY(min + (max - min) / 2);
        Path path = new Path();
        path.moveTo(drawRect.left, y);
        path.lineTo(drawRect.right, y);
        canvas.drawPath(path, dottePaint);
    }

    /**
     * 绘制真正的图表线条
     */
    private void drawLines(Canvas canvas){
        Path path = new Path();
        path.moveTo(drawRect.left, getY(min));
        double value;
        for(int i=0;i<datas.size();i++){
            value = datas.get(i);
            path.lineTo(getX(i), getY(value));
        }
        path.lineTo(drawRect.right, getY(datas.get(datas.size()-1)));
        path.lineTo(drawRect.right, getY(min));

        canvas.drawPath(path, linePaint);
    }

    private float getX(double index){
        return (float) (drawRect.left + index * xOffset);
    }

    private float getY(double value){
        value = value - min;
        return (float) (drawRect.bottom - value * yOffset);
    }

    private int getTextWitdh(String text) {
        int iRet = 0;
        if (text != null && text.length() > 0) {
            int len = text.length();
            float[] widths = new float[len];
            textPaint.getTextWidths(text, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    private int getTextHeight(String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }

}
