package com.codoon.clubgps.ui.fragment.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.util.CommonUtil;
import com.codoon.clubgps.widget.TypeItemView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryPagerDetailFragment extends Fragment {

    private View rootView;
    private HistoryDetail mHistoryDetail;
    private SimpleDateFormat mSimpleDateFormat;

    private TextView timeTv;
    private TextView distanceTv;
    private TypeItemView durationTv;
    private TypeItemView caloriesTv;
    private TypeItemView avgPaceTv;
    private TypeItemView maxPaceTv;
    private TypeItemView avgSpeedTv;
    private TypeItemView maxSpeedTv;

    public HistoryPagerDetailFragment() {}

    public static HistoryPagerDetailFragment newInstance(HistoryDetail historyDetail){
        HistoryPagerDetailFragment historyPagerDetailFragment = new HistoryPagerDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", historyDetail);
        historyPagerDetailFragment.setArguments(bundle);
        return historyPagerDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history_pager_detail, container, false);
        init();
        return rootView;
    }

    private void init(){
        mSimpleDateFormat = new SimpleDateFormat("yyyy"+getResources().getString(R.string.date_year)+"M"+getResources().getString(R.string.date_month)+"d"+getResources().getString(R.string.date_day)+" H:m");
        mHistoryDetail = getArguments().getParcelable("bean");

        timeTv = (TextView) rootView.findViewById(R.id.time_tv);
        distanceTv = (TextView) rootView.findViewById(R.id.distance_tv);

        durationTv = (TypeItemView) rootView.findViewById(R.id.duration_tv);
        caloriesTv = (TypeItemView) rootView.findViewById(R.id.calories_tv);
        avgPaceTv = (TypeItemView) rootView.findViewById(R.id.avg_pace_tv);
        maxPaceTv = (TypeItemView) rootView.findViewById(R.id.max_pace_tv);
        avgSpeedTv = (TypeItemView) rootView.findViewById(R.id.avg_speed_tv);
        maxSpeedTv = (TypeItemView) rootView.findViewById(R.id.max_speed_tv);

        timeTv.setText(mSimpleDateFormat.format(new Date(mHistoryDetail.getStartTimesStamp())));
        distanceTv.setText(CommonUtil.format2(mHistoryDetail.getTotal_length() / 1000));
        durationTv.setData(CommonUtil.getPeriodTime(mHistoryDetail.getTotal_time()));
        caloriesTv.setData(CommonUtil.format1(mHistoryDetail.getTotal_calories()));
        avgPaceTv.setData(CommonUtil.getPaceTimeStr(mHistoryDetail.getAvg_pace()));
        maxPaceTv.setData(CommonUtil.getPaceTimeStr(mHistoryDetail.getMax_pace()));
        avgSpeedTv.setData(CommonUtil.format1(mHistoryDetail.getAvg_speed()));
        maxSpeedTv.setData(CommonUtil.format1(mHistoryDetail.getMax_speed()));

        CommonUtil.setCustomTypeFace(distanceTv);

    }

}
