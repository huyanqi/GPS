package com.codoon.clubgps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.bean.HistoryListViewLine;

import java.util.List;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 这个类的任务:通过TYPE计算每个line的坐标、高度、显示文本、marginLeft
 *
 */

public class HistoryListView extends View {

    private Context mContext;
    private String bgColor = "#2b2b34";

    private HistoryListViewLine testLine;
    private HistoryListViewLine testLine2;

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
        testLine = new HistoryListViewLine(0, 0, 30, 8, "haha", "hehe");
        testLine2 = new HistoryListViewLine(60, 0, 30, 16, "haha", "hehe");

        setBackgroundColor(Color.parseColor(bgColor));
        isInited = true;
    }

    public void setShowType(int showType, int count, List<HistoryDetail> datas) {
        this.showType = showType;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInited) return;
        super.onDraw(canvas);
        testLine.draw(canvas);
        testLine2.draw(canvas);
    }

}
