package com.codoon.clubgps.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.codoon.clubgps.R;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 主要任务：
 *      将生成的统计，传给HistoryListView
 */

public class HistoryListGroupView extends LinearLayout {

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
        addView(LayoutInflater.from(getContext()).inflate(R.layout.widget_history_list_groupview, null));
    }

}
