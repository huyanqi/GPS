package com.codoon.clubgps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.view.View;

import com.codoon.clubgps.R;
import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.bean.HistoryCount;
import com.codoon.clubgps.bean.HistoryCountData;
import com.codoon.clubgps.bean.HistoryListViewLine;
import com.codoon.clubgps.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 2017/1/6.
 * <p>
 * 这个类的任务:
 * <p>
 * 接收
 *
 * @see com.codoon.clubgps.bean.HistoryCount 对象
 * <p>
 * 通过type知道需要展示周还是月统计
 * 通过time知道统计范围(2017年第1周、或者2016年12月)
 * <p>
 * 通过HistoryCount对象可以获取
 * @see com.codoon.clubgps.bean.HistoryCountData 对象列表
 * <p>
 * 计算出每个HistoryCountData对象数据，通过
 * @see HistoryListViewLine 对象展示
 */

public class HistoryListView extends View {

    private Context mContext;
    private String bgColor = "#2b2b34";

    private HistoryCount mHistoryCount;
    private int lineCount;
    private List<HistoryCountData> historyCountDataList;

    private boolean isInited;

    private ChatRect chatRect;//图表，包含最大刻度、背景...
    private int rectMarginBottomDP = 40;
    private int rectMarginLRDP = 10;

    private int minWidthDP;//line最小宽度dp值

    public HistoryListView(Context context, HistoryCount historyCount) {
        super(context);
        this.mContext = context;
        this.mHistoryCount = historyCount;
        minWidthDP = CommonUtil.dip2px(context, 8);

        historyCountDataList = mHistoryCount.findChildren();
    }

    private void initLines() {
        //1.根据类型判断需要展示的line数量
        if (mHistoryCount.getType() == 0) {
            //周榜
            lineCount = 7;//可以写死7天
            getWeekLines();
        } else {
            //月榜
            //1.根据月份获取那一月的天数
            String month = mHistoryCount.getTime();//格式:yyyyM

        }

        setBackgroundColor(Color.parseColor(bgColor));

    }

    private int eachWidth;//每个列的宽度
    private int startX;//开始x坐标
    private double max_length;//最大的长度
    private List<HistoryListViewLine> lineList;

    private void getWeekLines() {

        eachWidth = getMeasuredWidth() / lineCount;//平均分配宽度
        startX = eachWidth / 2 - CommonUtil.dip2px(getContext(), minWidthDP) / 2 + 30;

        //1.获取line对象列表
        lineList = new ArrayList<>();
        for (int i = 0; i < lineCount; i++) {
            lineList.add(getLine(i));
        }

        //2.组装line数据


        /*HistoryCountData historyCountData;
        for(int i = 0;i < historyCountDataList.size();i++){
            historyCountData = historyCountDataList.get(i);
            getLine(i, historyCountData);
        }*/

        //System.out.println("控件宽度:"+);
    }

    private void getMaxLength() {
        for (HistoryCountData historyCountData : historyCountDataList) {
            max_length = historyCountData.getTotal_length() > max_length ? historyCountData.getTotal_length() : max_length;
        }
        System.out.println("最大距离:" + max_length);
    }

    /**
     * 计算出图表矩形框的位置
     */
    private void initChatRect() {
        chatRect = new ChatRect();

        //1.获取Rect的范围
        int left = CommonUtil.dip2px(mContext, rectMarginLRDP);
        int right = getMeasuredWidth() - CommonUtil.dip2px(mContext, rectMarginLRDP);
        chatRect.set(left, 0, right, getMeasuredHeight() - CommonUtil.dip2px(mContext, rectMarginBottomDP));

        //2.计算出最大刻度值
        getMaxLength();
        chatRect.getPxPerLength(max_length);

    }

    /**
     * 获取line对象
     *
     * @param index
     * @return
     */
    private HistoryListViewLine getLine(int index) {
        int x = startX + (index * eachWidth);
        HistoryListViewLine line = new HistoryListViewLine(x, minWidthDP, Math.random()+"", "周"+index);
        line.updateHeight(chatRect.getBottom(), 80);

        return line;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initChatRect();
        initLines();

        isInited = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInited) return;
        super.onDraw(canvas);
        chatRect.draw(canvas);
        for (HistoryListViewLine historyListViewLine : lineList) {
            historyListViewLine.draw(canvas);
        }
    }

    private class ChatRect {

        private Rect mRect;
        private String bgColor = "#33323C";
        private String ruleColor = "#CCCCCC";

        private Paint bgPaint;//背景画笔
        private Paint ruleTextPaint;//刻度文字画笔
        private Paint linePaint;//刻度虚线画笔

        private int max_rule;//最大刻度尺
        private double pxPerLength;

        public void set(int left, int top, int right, int bottom) {
            mRect = new Rect();
            mRect.set(left, top, right, bottom);

            bgPaint = new Paint();
            bgPaint.setColor(Color.parseColor(bgColor));

            ruleTextPaint = new Paint();
            ruleTextPaint.setColor(Color.parseColor(ruleColor));
            ruleTextPaint.setAntiAlias(true);
            ruleTextPaint.setTextSize(CommonUtil.dip2px(GPSApplication.getContext(), 11));

            linePaint = new Paint();
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setColor(Color.WHITE);
            linePaint.setStrokeWidth(1);

        }

        public int getBottom() {
            return mRect.bottom;
        }

        /**
         * 计算最大刻度值
         *
         * @param max_length
         * @return 返回每个length的像素值
         */
        public double getPxPerLength(double max_length) {
            max_rule = (int) max_length;
            if(max_rule < 2)
                max_rule = 2;

            return pxPerLength = mRect.bottom * 0.8 / max_length;
        }

        public void draw(Canvas canvas) {
            //画背景
            canvas.drawRect(mRect, bgPaint);

            //画刻度虚线
            drawRuleLine(max_rule, canvas);
            drawRuleLine(max_rule / 2, canvas);
        }

        private void drawRuleLine(int value, Canvas canvas){
            float ruleY = (float) (value * pxPerLength);
            Path path = new Path();
            path.moveTo(mRect.left, mRect.bottom - ruleY);
            path.lineTo(mRect.right ,mRect.bottom - ruleY);
            PathEffect effects = new DashPathEffect(new float[]{ 6, 6, 6, 6}, 1);
            linePaint.setPathEffect(effects);

            canvas.drawPath(path, linePaint);

            canvas.drawText(value+GPSApplication.getContext().getString(R.string.kilometers), mRect.left, mRect.bottom - ruleY - 5, ruleTextPaint);

        }

    }

}
