package com.codoon.clubgps.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.adapter.HistoryListAdapter;
import com.codoon.clubgps.adapter.listener.OnItemClickListener;
import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.bean.HistoryListBean;
import com.codoon.clubgps.util.CommonUtil;
import com.codoon.clubgps.widget.CommonDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 运动历史列表
 */

public class HistoryListActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView mRecyclerView;
    private List<HistoryListBean> mHistoryBeanList;
    private HistoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        init();
    }

    private void init(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new CommonDecoration(LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setAdapter(mAdapter = new HistoryListAdapter(mHistoryBeanList = new ArrayList<HistoryListBean>(), this));
        mHistoryBeanList.addAll(getDatas(HistoryDetail.getHistoryList(GPSApplication.getAppContext().getUser_id())));
    }

    /**
     * 将数据按年月分组
     *
     * 需要得到的结果:把datas数据按照年月分组，并且获取该年月下的距离总数
     *
     * 1.全局保存一个与datas[0].startTimesStamp的yyyyMM格式字符串 time,全局是因为方便分页
     *
     * 2.全局新建一个初始值=0的变量distance用于保存总距离
     *
     * 3.遍历datas:
     *      (1)如果发现当前对象的startTimesStamp的yyyyMM格式与time不一致，生成HistoryListBean对象并保存tag所需内容(month、distance)，重置distance、time为当前对象的distance、time
     *      (2)startTimesStamp的yyyyMM格式与time一致，distance+=，生成HistoryListBean并保存HistoryDetail
     *
     * @param datas
     * @return
     */
    private String time = "";
    private double distance = 0;
    private HistoryListBean lastTagBean = null;//每个年月的tag，用于更新distance
    private List<HistoryListBean> getDatas(List<HistoryDetail> datas){
        List<HistoryListBean> historyListBeanList = new ArrayList<>();
        if(datas.size() == 0) return historyListBeanList;

        if("".equals(time)){
            time = CommonUtil.parseyyyyMM(datas.get(0).getStartTimesStamp());
            lastTagBean = new HistoryListBean();
            lastTagBean.isTag = true;
            lastTagBean.month = formatTimeStr(time);
            mHistoryBeanList.add(lastTagBean);
        }

        HistoryListBean historyListBean;
        for(HistoryDetail historyDetail : datas){
            historyListBean = new HistoryListBean();
            String yyyyMM = CommonUtil.parseyyyyMM(historyDetail.getStartTimesStamp());
            if(!yyyyMM.equals(time)){
                //不是同一个年月,新建tag对象
                time = yyyyMM;
                distance = historyDetail.getTotal_length();
                historyListBean.isTag = true;
                historyListBean.month = formatTimeStr(time);
                historyListBean.distance = CommonUtil.getDistanceKm(distance) + getString(R.string.kilometers);
                historyListBeanList.add(historyListBean);
                addHistoryListBean(historyListBeanList, historyDetail);
                lastTagBean = historyListBean;
            }else{
                distance += historyDetail.getTotal_length();
                lastTagBean.distance = CommonUtil.getDistanceKm(distance) + getString(R.string.kilometers);
                addHistoryListBean(historyListBeanList, historyDetail);
            }
        }

        return historyListBeanList;
    }

    private void addHistoryListBean(List<HistoryListBean> historyListBeanList, HistoryDetail historyDetail){
        HistoryListBean historyListBean = new HistoryListBean();
        historyListBean.isTag = false;
        historyListBean.historyDetail = historyDetail;
        historyListBeanList.add(historyListBean);
    }

    private String formatTimeStr(String time){
        return time.substring(0, 4)+getString(R.string.date_year)+time.substring(4, time.length())+getString(R.string.date_month);
    }

    @Override
    public void OnItemClicked(int position) {
        HistoryListBean historyListBean = mHistoryBeanList.get(position);
        Intent intent = new Intent(this, HistoryDetailActivity.class);
        intent.putExtra("bean", historyListBean.historyDetail);
        startActivity(intent);
    }

}
