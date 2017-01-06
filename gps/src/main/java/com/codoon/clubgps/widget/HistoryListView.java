package com.codoon.clubgps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.codoon.clubgps.bean.HistoryListViewLine;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 这个类的任务:通过TYPE计算每个line的坐标、高度、显示文本、marginLeft
 *
 */

public class HistoryListView extends View {

    private Context mContext;
    private String bgColor = "#2b2b34";
    private int gradientStartColor = 0xfff35566;//线条渐变色
    private int gradientEndColor = 0Xfff8832e;

    private Paint testPaint;
    private HistoryListViewLine testLine;

    private boolean isInited;

    /**
     * 控件展示类型
     * 只支持周和月
     */
    public static int TYPE_WEEK = 0;
    public static int TYPE_MONTH = 0;
    private int showType = TYPE_WEEK;

    public HistoryListView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public HistoryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init(){

        testPaint = new Paint();
        Shader mShader = new LinearGradient(50 ,0, 50, 150,new int[] {gradientStartColor, gradientEndColor},null,Shader.TileMode.MIRROR);
        testPaint.setShader(mShader);
        testLine = new HistoryListViewLine(0, 0, 50, 150, "haha", "hehe", 10);

        setBackgroundColor(Color.parseColor(bgColor));
        isInited = true;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInited) return;
        super.onDraw(canvas);
        testLine.draw(canvas, testPaint);
    }

}
