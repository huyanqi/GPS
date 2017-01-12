package com.codoon.clubgps.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.codoon.clubgps.R;
import com.codoon.clubgps.adapter.PaceChatAdapter;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.bean.HistoryGPSPoint;
import com.codoon.clubgps.bean.PaceChatBean;
import com.codoon.clubgps.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by codoon on 2017/1/12.
 */

public class PaceChatView extends LinearLayout {

    private Context mContext;
    private HistoryDetail mHistoryDetail;
    private RecyclerView mRecyclerView;
    private PaceChatAdapter mAdapter;
    private List<PaceChatBean> mList;

    public PaceChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_pace_chat, this, true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter = new PaceChatAdapter(context, mList = new ArrayList<PaceChatBean>()));
    }

    public void setHistoryDetailBean(HistoryDetail historyDetailBean){
        this.mHistoryDetail = historyDetailBean;
        getDatas();
    }

    /**
     * 组装成recycleview可用的数据
     *
     * 1.获取公里数
     * 2.获取当前公里数的平均配速
     * 3.获取当前公里数的耗时
     * 4.在5、10、21、42公里处有特殊标记
     *
     */
    private void getDatas(){
        int currentKm = 1;
        long pace = 0;//临时存储某一段公里的配速
        int count = 0;//存储某一段公里的打点数量
        long lastTimeStamp = 0;
        long maxDuration = 0;
        long totalDuration = 0;//某一段距离的总耗时

        int[] tagKm = new int[]{5, 10, 21, 42};
        String[] tagKmStr = new String[]{5+mContext.getString(R.string.kilometers), 10+mContext.getString(R.string.kilometers), mContext.getString(R.string.half_marathon), mContext.getString(R.string.full_marathon)};
        int currentTag = 0;

        PaceChatBean paceChatBean;
        List<HistoryGPSPoint> list = mHistoryDetail.findAllGPSPoints();
        for(HistoryGPSPoint historyGPSPoint : list){
            count++;
            pace += historyGPSPoint.getPace();
            if(historyGPSPoint.getTotal_length() / 1000 > currentKm){
                paceChatBean = new PaceChatBean();

                //到了一个公里数记录点
                //1.获取真实公里整数,有可能上一个点是 2公里，下一个点是4公里，中间会有漏掉的点
                int km = CommonUtil.getKmNumber(historyGPSPoint.getTotal_length() / 1000);
                paceChatBean.setKmNumber(km);

                //2.计算平均配速
                paceChatBean.setAvg_pace(CommonUtil.getPaceTimeStr(pace / count));

                //3.获取时间段的耗时,用当前时间段耗时，减去上一个时间段耗时
                long duration;
                if(lastTimeStamp == 0){
                    lastTimeStamp = list.get(0).getTimestamp();
                }
                duration = (historyGPSPoint.getTimestamp() - lastTimeStamp) / 1000;
                lastTimeStamp = historyGPSPoint.getTimestamp();
                paceChatBean.setDuration(duration);
                maxDuration = Math.max(duration, maxDuration);

                totalDuration += duration;

                pace = 0;
                count = 0;

                mList.add(paceChatBean);

                //4.查询是否到标记处
                if(currentTag < tagKm.length && km >= tagKm[currentTag]){
                    //到达标记处
                    paceChatBean = new PaceChatBean();
                    String kmText = tagKmStr[currentTag]+"  "+CommonUtil.getPeriodTime(totalDuration);
                    paceChatBean.setKmText(kmText);
                    mList.add(paceChatBean);
                    currentTag++;
                }

                currentKm = km + 1;

            }

        }

        mAdapter.notifyDataSetChanged();

    }

}
