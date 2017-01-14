package com.codoon.clubgps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.PaceChatViewPojo;
import com.codoon.clubgps.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 2017/1/13.
 */

public class PaceChatView extends View {

    private float lineSmoothness = 0.1f;
    private List<PaceChatViewPojo> datas;
    private float maxValue;
    private RectF bgRect;

    private int[] lineColors = new int[]{0xffF85445, 0xffd8d552, 0xff79E05C};
    private float[] levels;

    private Paint bgPaint;
    private Paint textPaint;
    private Paint dottePaint;//刻度虚线画笔
    private Paint linePaint;//贝塞尔曲线画笔

    private float leftTextHeight;
    private List<Point> mPointList;

    private int verticalValuePadding = 10;//左侧文字的padding 这里填单位是dp
    private float perWidth;//每一个item占用的宽度
    private int mWidth;
    private int mHeight;

    public PaceChatView(Context context, AttributeSet attrs) {
        super(context, attrs);

        bgPaint = new Paint();
        bgPaint.setColor(ContextCompat.getColor(context, R.color.light_gray_bg));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(28);
        textPaint.setColor(ContextCompat.getColor(context, R.color.light_white));

        leftTextHeight = CommonUtil.getTextHeight("0", textPaint);

        dottePaint = new Paint();
        dottePaint.setStyle(Paint.Style.STROKE);
        dottePaint.setColor(ContextCompat.getColor(context, R.color.dottet_line));
        dottePaint.setStrokeWidth(1);
        PathEffect effects = new DashPathEffect(new float[]{ 6, 6, 6, 6}, 1);
        dottePaint.setPathEffect(effects);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(ContextCompat.getColor(context, R.color.gps_line));
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(4);

        verticalValuePadding = CommonUtil.dip2px(verticalValuePadding);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        init();
    }

    public void setDatas(List<PaceChatViewPojo> datas){
        this.datas = datas;
    }

    private void init(){
        bgRect = getBgRect();
        perWidth = bgRect.width() / datas.size();
        initLevel();
    }

    /**
     * 将数据的value，分成lineColors.length个等级，用于画线的颜色渐变
     */
    private int[] mLineColors;

    private void initLevel(){
        int count = lineColors.length;
        float offset = maxValue / count;
        levels = new float[count];
        for(int i=1;i<=count;i++){
            levels[i-1] = i * offset;
        }

        mLineColors = new int[datas.size()];
        for(int i=0;i<datas.size();i++){
            mLineColors[i] = lineColors[getLevel(datas.get(i).getValue())];
        }
    }

    /**
     * 根据value获取等级
     * @param value
     * @return
     */
    private int getLevel(float value){
        int level = levels.length - 1;
        float startV = 0;
        for(int i=0;i<levels.length;i++){
            if(startV <= value && value <= levels[i]){
                level = i;
                break;
            }
            startV = levels[i];
        }

        return level;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        rect.set(0, 0, mWidth, mHeight);
        canvas.drawRect(rect, bgPaint);

        mPointList = new ArrayList<>();
        Point mPoint;
        float[] values = new float[datas.size()];
        for(int i=0;i<datas.size();i++){
            startDraw(i, datas.get(i), canvas);
            values[i] = datas.get(i).getValue();

            float x = getLineX(i);
            float y = getLineY(values[i]);
            mPoint = new Point();
            mPoint.set((int)x, (int)y);
            mPointList.add(mPoint);
        }

        drawBazier(canvas);

    }

    private void startDraw(int index, PaceChatViewPojo paceChatViewPojo, Canvas canvas){
        drawLeft(paceChatViewPojo.getValue(), canvas);
        drawText(index, paceChatViewPojo.getText(), canvas);
    }

    private void drawBazier(Canvas canvas){
        //保存曲线路径
        Path mPath = new Path();
        //保存辅助线路径
        Path mAssistPath = new Path();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX;
        float nextPointY;

        final int lineSize = mPointList.size();
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Point point = mPointList.get(valueIndex);
                currentPointX = point.x;
                currentPointY = point.y;
            }
            if (Float.isNaN(previousPointX)) {
                //是否是第一个点
                if (valueIndex > 0) {
                    Point point = mPointList.get(valueIndex - 1);
                    previousPointX = point.x;
                    previousPointY = point.y;
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    Point point = mPointList.get(valueIndex - 2);
                    prePreviousPointX = point.x;
                    prePreviousPointY = point.y;
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < lineSize - 1) {
                Point point = mPointList.get(valueIndex + 1);
                nextPointX = point.x;
                nextPointY = point.y;
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // 将Path移动到开始点
                mPath.moveTo(currentPointX, currentPointY);
                mAssistPath.moveTo(currentPointX, currentPointY);
            } else {
                // 求出控制点坐标
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (lineSmoothness * firstDiffX);
                final float firstControlPointY = previousPointY + (lineSmoothness * firstDiffY);
                final float secondControlPointX = currentPointX - (lineSmoothness * secondDiffX);
                final float secondControlPointY = currentPointY - (lineSmoothness * secondDiffY);
                //画出曲线
                mPath.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,currentPointX, currentPointY);

                //将控制点保存到辅助路径上
                mAssistPath.lineTo(firstControlPointX, firstControlPointY);
                mAssistPath.lineTo(secondControlPointX, secondControlPointY);
                mAssistPath.lineTo(currentPointX, currentPointY);
            }

            // 更新值,
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }

        //添加一个从最后一个点到控件最后的结束点,y坐标相同
        Point lastPoint = mPointList.get(mPointList.size()-1);
        Paint tmpPaint = new Paint();
        tmpPaint.setStrokeWidth(linePaint.getStrokeWidth());
        tmpPaint.setColor(mLineColors[mLineColors.length-1]);
        canvas.drawLine(lastPoint.x - tmpPaint.getStrokeWidth() / 2, lastPoint.y, bgRect.right, lastPoint.y, tmpPaint);

        linePaint.setShader(new LinearGradient(getLineX(0), 0, lastPoint.x, 0, mLineColors, null, Shader.TileMode.MIRROR));
        canvas.drawPath(mPath, linePaint);

        mPath.close();

    }

    /**
     * 获取图表背景矩形(既不包含底部文字和左侧文字)
     * @return
     */
    private RectF getBgRect(){
        RectF rect = new RectF();
        //1.获取最大value(方便计算左侧需要留出的空间)
        for(PaceChatViewPojo pojo : datas){
            maxValue = Math.max(maxValue, pojo.getValue());
        }

        //2.计算左侧需要的宽度
        float xOffset = textPaint.measureText(maxValue+"") + verticalValuePadding * 2;

        //3.计算底部需要的高度
        float yOffset = CommonUtil.getTextHeight("0", textPaint) + verticalValuePadding * 2;

        rect.set(xOffset , CommonUtil.getTextHeight("0", textPaint) / 2, mWidth, mHeight - yOffset);

        return rect;
    }

    private void drawLeft(float value, Canvas canvas){
        //1.画文字
        float y = getLineY(value) + leftTextHeight / 2;
        canvas.drawText(value+"", verticalValuePadding, y, textPaint);
        //2.画虚线
        Path path = new Path();
        float lineY = getLineY(value);
        path.moveTo(bgRect.left, lineY);
        path.lineTo(bgRect.right, lineY);
        canvas.drawPath(path, dottePaint);
    }

    private void drawText(int index, String text, Canvas canvas){
        float[] xb = getXAndBaseLine(index, text);
        canvas.drawText(text, xb[0], xb[1], textPaint);
    }

    private float getBaseLineY(int value){
        return mHeight - mHeight * value / maxValue + CommonUtil.getTextHeight(value+"", textPaint) / 2 + bgRect.top;
    }

    private float getLineY(float value){
        float rectHeight = bgRect.height();
        float y = rectHeight - rectHeight * (value / maxValue) + bgRect.top;
        return y;
    }

    /**
     * 获取底部文字的 x 坐标和 baseLine
     *
     * @param index
     * @return      v
     */
    private float[] getXAndBaseLine(int index, String text) {
        float x = getLineX(index) - CommonUtil.getTextWitdh(text, textPaint) / 2;
        float baseLine = getBaseLineY(0) - verticalValuePadding * 2;
        return new float[]{x, baseLine};
    }

    /**
     *
     * @param index
     * @return 返回每一格最左侧的x坐标
     */
    private float getLineX(int index){
        return perWidth * index + bgRect.left;
    }



}
