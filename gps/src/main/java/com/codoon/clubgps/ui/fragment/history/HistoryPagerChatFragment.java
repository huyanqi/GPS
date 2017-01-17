package com.codoon.clubgps.ui.fragment.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.bean.HistoryGPSPoint;
import com.codoon.clubgps.bean.PaceChatViewPojo;
import com.codoon.clubgps.util.CommonUtil;
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
        //获取公里数和平均配速
        List<HistoryGPSPoint> list = mHistoryDetail.findAllGPSPoints();

        int currentKm = 1;
        long pace = 0;//临时存储某一段公里的配速
        int count = 0;//存储某一段公里的打点数量
        String kmUnit = getString(R.string.kilometers);
        PaceChatViewPojo paceChatViewPojo = null;
        HistoryGPSPoint historyGPSPoint;
        for(int i=0;i<list.size();i++){
            historyGPSPoint = list.get(i);
            count++;
            pace += historyGPSPoint.getPace();
            if(historyGPSPoint.getTotal_length() / 1000 > currentKm){
                int km = CommonUtil.getKmNumber(historyGPSPoint.getTotal_length() / 1000);
                paceChatViewPojo = new PaceChatViewPojo(km+kmUnit, pace / count);
                paceChatViewPojo.setLeftText(CommonUtil.getPaceTimeStr(pace / count));
                datas.add(paceChatViewPojo);
                currentKm = km + 1;
            }
            pace = 0;
            count = 0;
        }

        mPaceChatView.setDatas(datas);
    }

}
