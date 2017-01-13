package com.codoon.clubgps.ui.fragment.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.bean.PaceChatViewPojo;
import com.codoon.clubgps.widget.PaceChatView;

import java.util.ArrayList;
import java.util.List;

public class HistoryPagerChatFragment extends Fragment {

    private View rootView;
    private HistoryDetail mHistoryDetail;
    private PaceChatView mPaceChatView;

    public HistoryPagerChatFragment() {}
    public static HistoryPagerChatFragment newInstance(HistoryDetail historyDetail){
        HistoryPagerChatFragment historyPagerPaceFragment = new HistoryPagerChatFragment();
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
        rootView = inflater.inflate(R.layout.fragment_history_pager_chat, container, false);
        mHistoryDetail = getArguments().getParcelable("bean");
        init();
        return rootView;
    }

    private void init (){
        mPaceChatView = (PaceChatView) rootView.findViewById(R.id.pace_chat_view);
        List<PaceChatViewPojo> datas = new ArrayList<>();
        datas.add(new PaceChatViewPojo("1公里", 100));
        datas.add(new PaceChatViewPojo("3公里", 170));
        datas.add(new PaceChatViewPojo("5公里", 85 ));
        datas.add(new PaceChatViewPojo("7公里", 32 ));
        datas.add(new PaceChatViewPojo("9公里", 1 ));
        datas.add(new PaceChatViewPojo("16公里", 87 ));
        datas.add(new PaceChatViewPojo("20公里", 0 ));
        mPaceChatView.setDatas(datas);
    }

}
