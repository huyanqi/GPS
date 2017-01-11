package com.codoon.clubgps.ui.fragment.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.codoon.clubgps.R;
import com.codoon.clubgps.ui.HistoryDetailActivity;

public class HistoryPagerPathFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View rootView;
    private HistoryDetailActivity mActivity;

    public HistoryPagerPathFragment() {}

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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_history_path, container, false);
        init();
        return rootView;
    }

    private void init() {
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
