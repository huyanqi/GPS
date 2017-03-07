package com.codoon.clubcodoongps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;

/**
 * Created by Frankie on 2017/3/2.
 */

public class NewElevationView extends BaseChatView {

    private Paint linePaint;
    private Paint mPaint;

    public NewElevationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        linePaint = new Paint();
        linePaint.setColor(0xFF3E94D6);
        linePaint.setStrokeWidth(3);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        //线段连接处变为圆角
        linePaint.setPathEffect(new CornerPathEffect(5));

        mPaint = new Paint();
        mPaint.setColor(0xFFBEAD61);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.FILL);
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
        Path linePath = new Path();
        path.moveTo(drawRect.left, getY(min));
        linePath.moveTo(drawRect.left, getY(datas.get(0).getValue()));
        double value;
        for(int i=0;i<datas.size();i++){
            value = datas.get(i).getValue();
            path.lineTo(getX(i), getY(value));
            linePath.lineTo(getX(i), getY(value));
        }
        path.lineTo(drawRect.right, getY(datas.get(datas.size()-1).getValue()));
        path.lineTo(drawRect.right, getY(min));
        linePath.lineTo(drawRect.right, getY(datas.get(datas.size()-1).getValue()));

        canvas.drawPath(linePath, linePaint);

        mPaint.setStyle(Paint.Style.FILL);
        if(mPaint.getShader() == null)
            mPaint.setShader(new LinearGradient(0, getY(max), 0, getY(min),
                    new int[]{0x453e94d6, 0x403e94d6, 0x063e94d6}, null, Shader.TileMode.CLAMP));
        canvas.drawPath(path, mPaint);
    }

}
