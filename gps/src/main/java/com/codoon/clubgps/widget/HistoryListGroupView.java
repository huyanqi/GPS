package com.codoon.clubgps.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.adapter.ViewPagerAdapter;
import com.codoon.clubgps.bean.HistoryCount;
import com.codoon.clubgps.bean.HistoryCountData;
import com.codoon.clubgps.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import static com.codoon.clubgps.util.CommonUtil.getHistoryDisplayMonthTime;
import static com.codoon.clubgps.util.CommonUtil.getHistoryDisplayWeekTime;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 主要任务：
 *      将生成的统计，传给
 *      @see HistoryListView
 */

public class HistoryListGroupView extends LinearLayout implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    private View rootView;
    private ViewPager mWeekViewPager;
    private ViewPager mMonthViewPager;

    private ViewPagerAdapter mWeekPagerAdapter;
    private ViewPagerAdapter mMonthPagerAdapter;

    private List<HistoryCount> weekHistoryCountList;
    private List<HistoryCount> monthHistoryCountList;

    private List<View> weekPagerList;
    private List<View> monthPagerList;

    private TextView distanceTv;
    private TextView timeTv;//显示当前统计的时间段,如果是周:格式为M月d日,月:yyyy年M月

    private int currentType = 0;//0:周统计 1:月统计

    public HistoryListGroupView(Context context) {
        super(context);
        init();
    }

    public HistoryListGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setOrientation(LinearLayout.VERTICAL);
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_history_list_groupview, null);

        mWeekViewPager = (ViewPager) rootView.findViewById(R.id.week_view_pager);
        mMonthViewPager = (ViewPager) rootView.findViewById(R.id.month_view_pager);
        distanceTv = (TextView) rootView.findViewById(R.id.distance_tv);
        timeTv = (TextView) rootView.findViewById(R.id.time_tv);

        mWeekViewPager.addOnPageChangeListener(this);
        mWeekViewPager.setAdapter(mWeekPagerAdapter = new ViewPagerAdapter(weekPagerList = new ArrayList<View>()));

        loadWeekCount();

        addView(rootView);

        ((RadioGroup)rootView.findViewById(R.id.radio_group_rg)).setOnCheckedChangeListener(this);

    }

    /**
     * 加载周统计
     */
    private void loadWeekCount(){
        weekHistoryCountList = HistoryCount.findAllCount(0);
        if(weekHistoryCountList.size() == 0) return;
        for(HistoryCount historyCount : weekHistoryCountList){
            weekPagerList.add(new HistoryListView(getContext(), historyCount));
        }
        mWeekPagerAdapter.notifyDataSetChanged();
        mWeekViewPager.setCurrentItem(weekHistoryCountList.size());//跳到最后一页
        initDataDiaplay(((HistoryListView)weekPagerList.get(weekPagerList.size() - 1)).getLineCount());
    }

    /**
     * 加载月统计
     */
    private void loadMonthCount(){
        monthHistoryCountList = HistoryCount.findAllCount(1);
        if(monthHistoryCountList.size() == 0) return;

        for(HistoryCount historyCount : monthHistoryCountList){
            monthPagerList.add(new HistoryListView(getContext(), historyCount));
        }
        mMonthPagerAdapter.notifyDataSetChanged();
        mMonthViewPager.setCurrentItem(monthPagerList.size() - 1);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        updateInfo();
    }

    private void updateInfo(){
        HistoryListView historyListView;
        if(currentType == 0){
            historyListView = (HistoryListView) weekPagerList.get(mWeekViewPager.getCurrentItem());
        }else{
            historyListView = (HistoryListView) monthPagerList.get(mMonthViewPager.getCurrentItem());
        }

        distanceTv.setText(CommonUtil.format2(historyListView.getTotalLength() / 1000));
        timeTv.setText(historyListView.getCurrentTime());
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    /**
     * 首次初始化顶部内容的显示
     * 第一次切换标签页要用到
     * @param day_count 展示的天数
     */
    private void initDataDiaplay(int day_count){
        //1.解析日期数据
        HistoryCount historyCount;
        String displayTime;
        if(currentType == 0){
            historyCount = weekHistoryCountList.get(weekHistoryCountList.size() - 1);
            displayTime = getHistoryDisplayWeekTime(historyCount.getTime(), day_count);
        }else{
            historyCount = monthHistoryCountList.get(monthHistoryCountList.size() - 1);
            displayTime = getHistoryDisplayMonthTime(historyCount.getTime());
        }

        timeTv.setText(displayTime);

        //2.获取总距离
        double totalLength = 0;
        for(HistoryCountData child : historyCount.findChildren()){
            totalLength += child.getTotal_length();
        }
        distanceTv.setText(CommonUtil.format2(totalLength / 1000));
    }

    private void switchData(){
        mWeekViewPager.setVisibility(currentType == 0 ? View.VISIBLE : View.INVISIBLE);
        mMonthViewPager.setVisibility(currentType == 1 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.tab_1){//切换到周统计
            if(currentType == 0) return;
            currentType = 0;
            switchData();
        }else if(checkedId == R.id.tab_2){
            if(currentType == 1) return;//切换到月统计
            currentType = 1;
            if(monthPagerList == null) {
                mMonthViewPager.setAdapter(mMonthPagerAdapter = new ViewPagerAdapter(monthPagerList = new ArrayList<View>()));
                mMonthViewPager.addOnPageChangeListener(this);
                loadMonthCount();
                initDataDiaplay(((HistoryListView)monthPagerList.get(monthPagerList.size() - 1)).getLineCount());
            }
            switchData();
        }
    }

}
