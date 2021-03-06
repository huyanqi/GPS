package com.codoon.clubgps.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.codoon.clubgps.R;
import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.bean.HistoryCount;
import com.codoon.clubgps.bean.HistoryCountData;
import com.codoon.clubgps.bean.HistoryListViewLine;
import com.codoon.clubgps.util.CommonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private int bottomTextColor;

    private HistoryCount mHistoryCount;
    private int lineCount;
    private List<HistoryCountData> historyCountDataList;

    private boolean isInited;
    private SimpleDateFormat yyyyMMddFormat;

    private ChatRect chatRect;//图表，包含最大刻度、背景...
    private int rectMarginBottomDP = 40;
    private int rectMarginLRDP = 10;

    private int minWidth;//line最小宽度
    private double pxPerLength;

    private int eachWidth;//每个列的宽度
    private int startX;//开始x坐标
    private double max_length;//最大的树状图高度
    private int min_length;//最小的树状图高度
    private List<HistoryListViewLine> lineList;

    private double totalLength;//当前界面展示数据的距离总长度
    private String currentTime;//当前界面展示数据的时间段,格式: 周(M月d日-M月d日) 月(yyyy年M月)

    public HistoryListView(Context context, HistoryCount historyCount) {
        super(context);
        this.mHistoryCount = historyCount;
        bottomTextColor = ContextCompat.getColor(getContext(), R.color.light_white);
        minWidth = CommonUtil.dip2px(8);
        yyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");

        historyCountDataList = mHistoryCount.findChildren();

        init();
    }

    private void init(){
        //1.初始化竖线数量
        if (mHistoryCount.getType() == 0) {
            //周榜
            lineCount = 7;//可以写死7天
        } else {
            //月榜
            //根据月份获取那一月的天数
            String month = mHistoryCount.getTime();//格式:yyyyM
            lineCount = CommonUtil.getMonthDayCount(month);
        }

        //2.初始化控件包含的时间段
        if(mHistoryCount.getType() == 0){
            currentTime = CommonUtil.getHistoryDisplayWeekTime(mHistoryCount.getTime(), lineCount);
        }else{
            currentTime = CommonUtil.getHistoryDisplayMonthTime(mHistoryCount.getTime());
        }

        //3.获取总长度、最大长度、最小长度
        getMaxLength();

    }

    private void initLines() {
        eachWidth = chatRect.getWidth() / lineCount;//平均分配宽度
        startX = chatRect.getLeft() + eachWidth / 2;

        //1.获取开始时间Calendar
        Calendar startCalendar = getStartCalendar();

        //2.初始化line对象列表
        lineList = new ArrayList<HistoryListViewLine>();
        for (int i = 0; i < lineCount; i++) {
            lineList.add(getLine(i, yyyyMMddFormat.format(startCalendar.getTime())));
            startCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        //3.更新line内容
        for(HistoryCountData historyCountData : historyCountDataList){
            for(HistoryListViewLine historyListViewLine : lineList){
                if(historyListViewLine.getTimeTag().equals(historyCountData.getTime())){
                    //更新高度
                    historyListViewLine.updateHeight((int) (pxPerLength * historyCountData.getTotal_length() / 1000) + min_length);

                    //如果是公里数最多的那天，就要显示公里数在顶部
                    if(historyCountData.getTotal_length() / 1000 == max_length){
                        historyListViewLine.setTopText(CommonUtil.format2(historyCountData.getTotal_length() / 1000)+"");
                    }
                }
            }
        }

        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_gray_bg));
    }

    private void getMaxLength() {
        totalLength = 0;
        if(historyCountDataList.size() == 0) return;
        for (HistoryCountData historyCountData : historyCountDataList) {
            totalLength += historyCountData.getTotal_length();

            max_length = historyCountData.getTotal_length() / 1000 > max_length ? historyCountData.getTotal_length() / 1000 : max_length;
        }

    }

    /**
     * 计算出图表矩形框的位置
     */
    private void initChatRect() {
        chatRect = new ChatRect();

        //1.获取Rect的范围
        int left = CommonUtil.dip2px(rectMarginLRDP);
        int right = getMeasuredWidth() - CommonUtil.dip2px(rectMarginLRDP);
        chatRect.set(left, 0, right, getMeasuredHeight() - CommonUtil.dip2px(rectMarginBottomDP));

        //2.计算出最大刻度值
        chatRect.getPxPerLength(max_length);

        //3.
        min_length = chatRect.getHeight() / 40;

    }

    /**
     * 获取line对象
     *
     * @param index
     * @return
     */
    private HistoryListViewLine getLine(int index, String timeTag) {
        int x = startX + (index * eachWidth);
        HistoryListViewLine line = new HistoryListViewLine(x, minWidth, timeTag, chatRect.getBottom(), bottomTextColor);
        line.updateHeight(min_length);
        if(mHistoryCount.getType() == 0){
            line.setBottomText(getContext().getString(CommonUtil.getResource("week_"+(index+1), "string")));
        }else{
            //月榜,只显示日期%5=0的日期，其他都显示.
            index = index+1;
            String showDay = "•";
            if(index == 1 || index % 5 == 0){
                showDay = index+"";
            }
            line.setBottomText(showDay);
        }

        return line;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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

    /**
     * 获取开始时间的日历
     * @return
     */
    public Calendar getStartCalendar() {
        String date = mHistoryCount.getTime();
        int type = mHistoryCount.getType();

        Calendar startCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = null;
        if(type == 0){
            sdf = new SimpleDateFormat("yyyyw");
        }else{
            //月统计
            sdf = new SimpleDateFormat("yyyyM");
        }
        try {
            startCalendar.setTime(sdf.parse(date));

            if(type == 0){
                //默认的每周第一天是周日，需要手动+1跳过这一天，我们要的是周一
                startCalendar.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startCalendar;
    }

    private class ChatRect {

        private Rect mRect;
        private String bgColor = "#33323C";

        private Paint bgPaint;//背景画笔
        private Paint ruleTextPaint;//刻度文字画笔
        private Paint linePaint;//刻度虚线画笔

        private int max_rule;//最大刻度尺

        public void set(int left, int top, int right, int bottom) {
            mRect = new Rect();
            mRect.set(left, top, right, bottom);

            bgPaint = new Paint();
            bgPaint.setColor(Color.parseColor(bgColor));

            ruleTextPaint = new Paint();
            ruleTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.light_white));
            ruleTextPaint.setAntiAlias(true);
            ruleTextPaint.setTextSize(CommonUtil.dip2px(11));

            linePaint = new Paint();
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setColor(ContextCompat.getColor(getContext(), R.color.dottet_line));
            linePaint.setStrokeWidth(1);

        }

        public int getWidth(){
            return mRect.right - mRect.left;
        }

        public int getLeft(){
            return mRect.left;
        }

        public int getBottom() {
            return mRect.bottom;
        }

        public int getHeight(){
            return mRect.height();
        }

        /**
         * 计算最大刻度值
         *
         * @param max_length
         * @return 返回每个length的像素值
         */
        public double getPxPerLength(double max_length) {
            max_rule = getMaxRule(max_length);
            return pxPerLength = mRect.bottom * 0.8 / max_rule * 0.9;
        }

        /**
         * 获取最近的 能被10整除的数
         * @param max_length
         * @return
         */
        private int getMaxRule(double max_length){
            int max = (int) max_length;
            if(max < 2) return 2;
            if(max < 10) return max;

            int tmpMax = max;

            for(int i=0;i < 5;i++){
                if(tmpMax % 10 != 0) {
                    tmpMax--;
                }else{
                    break;
                }
            }

            if(tmpMax % 10 != 0){
                for(int i=0;i < 5;i++){
                    if(tmpMax % 10 != 0) {
                        tmpMax++;
                    }else{
                        break;
                    }
                }
            }

            return tmpMax;
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
            float y = mRect.bottom - ruleY + min_length;
            path.moveTo(mRect.left, y);
            path.lineTo(mRect.right , y);
            PathEffect effects = new DashPathEffect(new float[]{ 6, 6, 6, 6}, 1);
            linePaint.setPathEffect(effects);

            canvas.drawPath(path, linePaint);

            canvas.drawText(value+GPSApplication.getContext().getString(R.string.kilometers), mRect.left, y , ruleTextPaint);//baseline和虚线对其

        }

    }

    public double getTotalLength() {
        return totalLength;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public int getLineCount() {
        return lineCount;
    }

}
