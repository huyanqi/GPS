package com.codoon.clubgps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Frankie on 2017/1/13.
 */

public class TestView extends View {

    private Paint mPaint;
    private int maxValue = 200;

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setShader(new LinearGradient(0, 0, getWidth(), 0, new int[]{Color.RED, Color.YELLOW, Color.GREEN}, new float[]{0.2f , 0.1f, 0.7f}, Shader.TileMode.MIRROR));
        canvas.drawLine(0, 10, getWidth(), 10, mPaint);
    }

}
