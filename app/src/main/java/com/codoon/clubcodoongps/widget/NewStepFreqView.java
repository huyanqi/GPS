package com.codoon.clubcodoongps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

/**
 * Created by codoon on 2017/3/2.
 */

public class NewStepFreqView extends BaseChatView {

    private Paint mPaint;

    public NewStepFreqView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(0xFFBEAD61);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new CornerPathEffect(5));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(drawRect == null) return;
        drawLines(canvas);
    }

    /**
     * 绘制真正的图表线条
     */
    private void drawLines(Canvas canvas){
        Path path = new Path();
        path.moveTo(drawRect.left, getY(min));
        float value;
        for(int i=0;i<datas.size();i++){
            value = datas.get(i).getValue();
            path.lineTo(getX(i), getY(value));
        }
        path.lineTo(drawRect.right, getY(datas.get(datas.size()-1).getValue()));

        canvas.drawPath(path, mPaint);
    }

}
