package com.codoon.clubgps.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.codoon.clubgps.R;
import com.codoon.clubgps.adapter.ViewPagerAdapter;
import com.codoon.clubgps.bean.HistoryCount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 主要任务：
 *      将生成的统计，传给
 *      @see HistoryListView
 */

public class HistoryListGroupView extends LinearLayout {

    private View rootView;
    private ViewPager mWeekViewPager;

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

        loadWeekCount();

        addView(rootView);
    }

    /**
     * 加载周统计
     */
    private void loadWeekCount(){
        List<HistoryCount> historyCountList = HistoryCount.findAllCount(0);
        List<View> weekList = new ArrayList<>();
        for(HistoryCount historyCount : historyCountList){
            weekList.add(new HistoryListView(getContext(), historyCount));
        }

        mWeekViewPager.setAdapter(new ViewPagerAdapter(weekList));
    }

}
