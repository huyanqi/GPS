package com.codoon.clubgps.ui.fragment.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.widget.PaceListView;

public class HistoryPagerPaceFragment extends Fragment {

    private View rootView;
    private PaceListView mPaceChatView;
    private HistoryDetail mHistoryDetail;

    public HistoryPagerPaceFragment() {}

    public static HistoryPagerPaceFragment newInstance(HistoryDetail historyDetail){
        HistoryPagerPaceFragment historyPagerPaceFragment = new HistoryPagerPaceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bean", historyDetail);
        historyPagerPaceFragment.setArguments(bundle);
        return historyPagerPaceFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history_pager_pace, container, false);
        mHistoryDetail = getArguments().getParcelable("bean");
        init();
        return rootView;
    }

    private void init(){
        mPaceChatView = (PaceListView) rootView.findViewById(R.id.pace_chat_view);
        mPaceChatView.setHistoryDetailBean(mHistoryDetail);
    }

}
