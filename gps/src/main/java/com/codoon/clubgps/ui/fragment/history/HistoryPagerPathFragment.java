package com.codoon.clubgps.ui.fragment.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.ui.HistoryDetailActivity;
import com.codoon.clubgps.util.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryPagerPathFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View rootView;
    private HistoryDetailActivity mActivity;
    private HistoryDetail mHistoryDetail;
    private SimpleDateFormat mSimpleDateFormat;
    private TextView timeTv;
    private TextView distanceTv;
    private TextView durationTv;
    private TextView speedTv;
    private TextView caloriesTv;

    public HistoryPagerPathFragment() {}

    public static HistoryPagerPathFragment newInstance(HistoryDetail historyDetail){
        HistoryPagerPathFragment historyPagerPathFragment = new HistoryPagerPathFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", historyDetail);
        historyPagerPathFragment.setArguments(bundle);
        return historyPagerPathFragment;
    }

    public void setActivity(HistoryDetailActivity activity){
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history_path, container, false);
        mHistoryDetail = getArguments().getParcelable("bean");
        init();
        return rootView;
    }



    private void init() {
        mSimpleDateFormat = new SimpleDateFormat("yyyy"+getResources().getString(R.string.date_year)+"M"+getResources().getString(R.string.date_month)+"d"+getResources().getString(R.string.date_day)+" H:m");

        timeTv = (TextView) rootView.findViewById(R.id.time_tv);
        distanceTv = (TextView) rootView.findViewById(R.id.distance_tv);
        durationTv = (TextView) rootView.findViewById(R.id.duration_tv);
        speedTv = (TextView) rootView.findViewById(R.id.speed_tv);
        caloriesTv = (TextView) rootView.findViewById(R.id.calories_tv);

        timeTv.setText(mSimpleDateFormat.format(new Date(mHistoryDetail.getStartTimesStamp())));
        distanceTv.setText(CommonUtil.format2(mHistoryDetail.getTotal_length() / 1000));
        durationTv.setText(CommonUtil.getPeriodTime(mHistoryDetail.getTotal_time()));
        speedTv.setText(CommonUtil.format1(mHistoryDetail.getAvg_speed()));
        caloriesTv.setText(CommonUtil.format1(mHistoryDetail.getTotal_calories()));

        CommonUtil.setCustomTypeFace(distanceTv);
        CommonUtil.setCustomTypeFace(durationTv);
        CommonUtil.setCustomTypeFace(speedTv);
        CommonUtil.setCustomTypeFace(caloriesTv);

        rootView.findViewById(R.id.map_holder).setOnClickListener(this);

        ((CheckBox)rootView.findViewById(R.id.mil_cb)).setOnCheckedChangeListener(this);
        ((CheckBox)rootView.findViewById(R.id.map_visible_cb)).setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.map_holder) {
            //将HistoryDetailFragment展示到界面最前面
            mActivity.switchViewPager();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.mil_cb){
            //切换公里数的显示
            mActivity.switchKmView();
        }else if(buttonView.getId() == R.id.map_visible_cb){
            //切换地图类型
            mActivity.switchMapType();
        }

    }

}
