package com.codoon.clubcodoongps.widget;

import android.content.Context;
import android.graphics.Canvas;
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
 * 不画具体的图表呈现,比如：直线、曲线...
 */

public class BaseChatView extends View {

    private int mWidth;
    private int mHeight;
    protected List<ChatPojo> datas = new ArrayList<>();
    protected boolean inited = false;

    protected Paint linePaint;
    private Paint textPaint;
    protected Rect drawRect;
    private Rect linesRect;//子类的图表都是画在这里面的,用这个类目的是让子类的线不要和父类的3根横线重叠

    private ChatPojo maxPojo, minPojo;
    protected float max = 0, min = 0;//最大值、最小值
    private double xOffset, yOffset;//每个值在x,y方向的偏移量
    private int bottomTextHeight = 0;
    private int leftTextPadding = 10, bottomTextPadding = 5;//单位:px,可以改成dp,但是要自己计算
    private int total_count;//一共有几段数据，在画第一个和最后一个bottomText的时候会用到

    public BaseChatView(Context context, AttributeSet attrs) {
        super(context, attrs);

        textPaint = new Paint();
        textPaint.setColor(0xFF79798E);
        textPaint.setTextSize(30);
        textPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(0xFF79798E);
        //设置画笔宽度
        linePaint.setStrokeWidth(1);
        linePaint.setAntiAlias(true);

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
        int top = (int) linePaint.getStrokeWidth();
        int right = mWidth;
        int bottom = mHeight - (int) linePaint.getStrokeWidth() - bottomTextHeight - bottomTextPadding * 2;
        drawRect = new Rect();
        drawRect.set(left, top, right, bottom);

        linesRect = new Rect();
        top = (int) (drawRect.top + linePaint.getStrokeWidth() * 2);
        bottom = (int) (drawRect.bottom - linePaint.getStrokeWidth() * 2);
        linesRect.set(left, top, right, bottom);


        //3、计算每个值在x,y上的偏移量
        xOffset = drawRect.width() / (datas.size() * 1d);
        yOffset = drawRect.height() / ((max - min) * 1d);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(drawRect == null) return;
        drawLeftValue(canvas);
        drawRect(canvas);
        drawRuleLines(canvas);
        drawBottomText(canvas);
    }

    /**
     * 绘制左边值，只画最大、最小值
     * (可以扩展为只画有leftText的值)
     */
    private void drawLeftValue(Canvas canvas){
        canvas.drawText(maxPojo.getLeftText(), leftTextPadding, getY(max) + getTextHeight(maxPojo.getLeftText()), textPaint);//文字顶部和rect顶部对其
        canvas.drawText(minPojo.getLeftText(), leftTextPadding, getY(min), textPaint);//文字底部和rect底部对其
    }

    /**
     * 绘制路径区域
     */
    private void drawRect(Canvas canvas){
    }

    /**
     * 绘制尺度线
     */
    private void drawRuleLines(Canvas canvas){
        float y = getRuleY(max);
        canvas.drawLine(drawRect.left, y, drawRect.right, y, linePaint);
        y = getRuleY(min);
        canvas.drawLine(drawRect.left, y, drawRect.right, y, linePaint);
        y = getRuleY(min + (max - min) / 2);
        Path path = new Path();
        path.moveTo(drawRect.left, y);
        path.lineTo(drawRect.right, y);
        linePaint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{ 6, 6, 6, 6}, 1);
        linePaint.setPathEffect(effects);
        canvas.drawPath(path, linePaint);
    }

    /**
     * 绘制底部文字
     */
    private void drawBottomText(Canvas canvas){
        ChatPojo pojo;
        int text_w;
        float x;
        int lastIndex = -1;//保存上一个文字的索引，用于判断当前文字和上一个文字是否重叠
        String lastText = "";//保存上一个文字的内容，用于判断当前文字和上一个文字是否重叠
        for(int i=0;i<datas.size();i++){
            pojo = datas.get(i);
            if(TextUtils.isEmpty(pojo.getBottomText())) continue;
            text_w = getTextWitdh(pojo.getBottomText());

            if(total_count == 1){
                //如果只有一个底部标签，那当前标签应该显示在中间
                x = drawRect.width() / 2 - text_w / 2;
            }else{
                x = getX(i) - text_w / 2;
                //检查是否超出右边界
                if(x + text_w > drawRect.right){
                    //超出右边界
                    x = drawRect.right - text_w;
                }

                if(lastIndex != -1 && !TextUtils.isEmpty(lastText)) {
                    //检查与上一个文字是否重叠
                    float lastX = getX(lastIndex) - text_w / 2;
                    if(lastX + getTextWitdh(lastText) > x ){
                        //和上一个文字重叠了
                        //取消绘制当前文本
                        continue;
                    }
                }
            }

            canvas.drawText(pojo.getBottomText(), x, getY(min) + bottomTextHeight + bottomTextPadding, textPaint);
            lastIndex = i;
            lastText = pojo.getBottomText();
        }

    }

    protected float getX(double index){
        return (float) (drawRect.left + index * xOffset);
    }

    protected float getY(double value){
        value = value - min;
        return (float) (linesRect.bottom + linesRect.top - value * yOffset);
    }

    private float getRuleY(double value){
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