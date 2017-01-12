package com.codoon.clubgps.ui.fragment.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codoon.clubgps.R;

public class HistoryPagerDetailFragment extends Fragment {

    private View rootView;
    private TextView distanceTv;

    public HistoryPagerDetailFragment() {}

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
        distanceTv = (TextView) rootView.findViewById(R.id.distance_tv);
    }

}
