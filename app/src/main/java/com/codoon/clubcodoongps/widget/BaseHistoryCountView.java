package com.codoon.clubcodoongps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 2017/3/2.
 *
 * 这个控件只负责画背景框、左侧文字、底部文字
 *
 * 不画具体的图表呈现
 */

public class BaseHistoryCountView extends View {

    private int mWidth;
    private int mHeight;
    protected List<ChatPojo> datas = new ArrayList<>();
    protected boolean inited = false;
    private String bgColor = "#33323C";

    private Paint bgPaint;
    private Paint linePaint;
    private Paint dottedPaint;
    protected Paint textPaint;
    protected Rect drawRect;

    private ChatPojo maxPojo, minPojo;
    protected float max = 0, min = 0;//最大值、最小值
    protected double xOffset, yOffset;//每个值在x,y方向的偏移量
    private int bottomTextHeight = 0;
    private int leftTextPadding = 10, bottomTextPadding = 5;//单位:px,可以改成dp,但是要自己计算
    private int total_count;//一共有几段数据，在画第一个和最后一个bottomText的时候会用到

    public BaseHistoryCountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor(bgColor));

        textPaint = new Paint();
        textPaint.setColor(0xFF79798E);
        textPaint.setTextSize(30);
        textPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(0xFF79798E);
        //设置画笔宽度
        linePaint.setStrokeWidth(1);
        linePaint.setAntiAlias(true);

        dottedPaint = new Paint();
        dottedPaint.setColor(0xFF79798E);
        //设置画笔宽度
        dottedPaint.setStrokeWidth(1);
        dottedPaint.setAntiAlias(true);
        dottedPaint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{ 6, 6, 6, 6}, 1);
        dottedPaint.setPathEffect(effects);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = getMeasuredWidth();
        this.mHeight = getMeasuredHeight();
        if(!inited && datas.size() != 0) {
            init();
        }
    }

    public void setDatas(List<ChatPojo> datas){
        if(datas == null || datas.size() == 0) return;
        this.datas = datas;
        if(mWidth != 0) {
            init();
        }
    }

    protected void init(){
        System.out.println("parent init");
        inited = true;

        //1、需要遍历数组
        //获取左侧文本需要的最大宽度
        //本次最大值、最小值
        int left_max_w = 0;
        int w;
        for(ChatPojo chatPojo : datas){

            if(!TextUtils.isEmpty(chatPojo.getBottomText())) total_count++;

            w = getTextWitdh(chatPojo.getLeftText());
            bottomTextHeight = Math.max(bottomTextHeight, getTextHeight(chatPojo.getBottomText()));
            left_max_w = Math.max(left_max_w, w);

            if(min == 0 || chatPojo.getValue() < min){
                min = chatPojo.getValue();
                minPojo = chatPojo;
            }

            if(max == 0 || max < chatPojo.getValue()){
                max = chatPojo.getValue();
                maxPojo = chatPojo;
            }

        }

        //2、获取图表区域
        int left = left_max_w + leftTextPadding * 2;
        int top = (int) linePaint.getStrokeWidth() + getTextHeight(maxPojo.getLeftText()) * 2;//要离顶部多一些，给显示最大值留足够空间
        int right = mWidth;
        int bottom = mHeight - (int) linePaint.getStrokeWidth() - bottomTextHeight - bottomTextPadding * 2;
        drawRect = new Rect();
        drawRect.set(left, top, right, bottom);

        //3、计算每个值在x,y上的偏移量
        xOffset = drawRect.width() / (datas.size() * 1d);
        yOffset = drawRect.height() / ((max - min) * 1d);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(drawRect == null) return;

        canvas.drawRect(drawRect, bgPaint);

        ChatPojo chatPojo;
        for(int i=0;i<datas.size();i++){
            chatPojo = datas.get(i);
            drawLeftTextAndRuleLines(chatPojo, canvas);
            drawBottomText(i, chatPojo, canvas);
        }

    }

    private void drawLeftTextAndRuleLines(ChatPojo pojo, Canvas canvas){
        if(TextUtils.isEmpty(pojo.getLeftText())) return;
        float y = getY(pojo.getValue());
        //如果当前是最大值，画实线
        if(pojo.getValue() == max){
            canvas.drawLine(drawRect.left, y, drawRect.right, y, linePaint);
            canvas.drawText(pojo.getLeftText(), leftTextPadding, y + getTextHeight(pojo.getLeftText()) / 2, textPaint);
        }else if(pojo.getValue() == min){
            //最小值,不画任何东西
            //canvas.drawLine(drawRect.left, y, drawRect.right, y, linePaint);

        }else{
            //否则画虚线
            Path path = new Path();
            path.moveTo(drawRect.left, y);
            path.lineTo(drawRect.right, y);
            canvas.drawPath(path, dottedPaint);
            canvas.drawText(pojo.getLeftText(), leftTextPadding, y + getTextHeight(pojo.getLeftText()) / 2, textPaint);
        }

    }

    /**
     * 绘制底部文字
     */
    private void drawBottomText(int index, ChatPojo pojo, Canvas canvas){
        canvas.drawText(pojo.getBottomText(), (float) (getX(index) + (xOffset / 2 - getTextWitdh(pojo.getBottomText()) / 2)), getY(min) + bottomTextHeight + bottomTextPadding, textPaint);
    }

    protected float getX(double index){
        return (float) (drawRect.left + index * xOffset);
    }

    protected float getY(double value){
        value = value - min;
        return (float) (drawRect.bottom - value * yOffset);
    }

    protected int getTextWitdh(String text) {
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

    protected int getTextHeight(String text) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }

}