package com.codoon.clubcodoongps.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.codoon.clubgps.util.CommonUtil;

/**
 * Created by Frankie on 2017/1/23.
 */

public class CalendarGroupView extends LinearLayout {

    private int paddingDP = 12;//单位:dp
    private int calendarViewHeightDP = 32;//子view的高度，单位:dp

    public CalendarGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        paddingDP = CommonUtil.dip2px(paddingDP);
        setPadding(paddingDP, paddingDP, paddingDP ,paddingDP);
        calendarViewHeightDP = CommonUtil.dip2px(calendarViewHeightDP);
    }

    public void setDatas(CalendarPojo... datas){
        for(CalendarPojo data : datas){
            addView(new CalendarView(getContext(), data), new LayoutParams(0, calendarViewHeightDP, 1));
        }
    }



}
