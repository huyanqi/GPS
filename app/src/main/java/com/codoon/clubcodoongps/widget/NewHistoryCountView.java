package com.codoon.clubcodoongps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.codoon.clubgps.util.CommonUtil;

/**
 * Created by Frankie on 2017/3/4.
 */

public class NewHistoryCountView extends BaseHistoryCountView {

    private Paint gradientPaint;
    private int gradientStartColor = 0Xfff8832e;//线条渐变色
    private int gradientEndColor = 0xfff35566;
    private Shader mShader;
    private int minWidth = CommonUtil.dip2px(8);//竖状条的最小宽度
    private int lineWidth = minWidth;
    private int lrPadding = CommonUtil.dip2px(2);//竖状条左右间距
    private int minHeight = CommonUtil.dip2px(4);//竖状条的最小高度
    private float lastTopTextX;//最后一个最大值的右边x坐标,防止有多个最大值导致重叠展示的问题

    public NewHistoryCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gradientPaint = new Paint();
        gradientPaint.setAntiAlias(true);
    }

    @Override
    protected void init() {
        super.init();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height;
        ChatPojo chatPojo;
        float left;
        RectF lineRect;
        for(int i = 0; i < datas.size(); i++){
            chatPojo = datas.get(i);
            height = getY(chatPojo.getValue());

            if(drawRect.bottom - height <= minHeight){
                height = drawRect.bottom - minHeight;
            }

            mShader = new LinearGradient(lineWidth , height, lineWidth, drawRect.bottom, gradientStartColor, gradientEndColor, Shader.TileMode.MIRROR);
            gradientPaint.setShader(mShader);
            left = (float) (getX(i) + (xOffset / 2 - lineWidth / 2));
            lineRect = new RectF(left, height, left + lineWidth, drawRect.bottom);
            canvas.drawRect(lineRect, gradientPaint);

            if(chatPojo.getValue() == max) {
                //最大值，需要在顶部标识
                String text = chatPojo.getValue()+"";
                left = lineRect.left + (lineWidth / 2 - getTextWitdh(text) / 2);
                if(left < lastTopTextX){
                    //当前要绘制的文字 和上次已经绘制过的文字重叠了，所以本次不绘制
                }else{
                    canvas.drawText(text, left , height - getTextHeight(text) / 2, textPaint);
                    lastTopTextX = left + getTextWitdh(text);
                }
            }

        }
    }

}
