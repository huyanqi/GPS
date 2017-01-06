package com.codoon.clubgps.bean;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 运动历史列表-顶部控件-viewpager item里每一列的竖线
 */

public class HistoryListViewLine {

    private int width;//线条的宽度
    private int height;//线条的高度
    private String topText;//顶部显示的文本
    private String bottomText;//底部显示的文本
    private int marginLeft;//左对齐距离

    private int x;
    private int y;

    private HistoryListViewLine(){}

    public HistoryListViewLine (int x, int y, int width, int height, String topText, String bottomText, int marginLeft) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.topText = topText;
        this.bottomText = bottomText;
        this.marginLeft = marginLeft;
    }

    public void draw(Canvas canvas, Paint linePaint){
        canvas.drawRect(x , y, x + width, y + height, linePaint);
    }

}
