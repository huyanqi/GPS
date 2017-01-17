package com.codoon.clubgps.adapter.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.HistoryDetail;
import com.codoon.clubgps.util.CommonUtil;
import com.codoon.clubgps.util.ImageLoadUtil;
import com.codoon.clubgps.widget.CImageView;

/**
 * Created by Frankie on 2017/1/6.
 */

public class HistoryListHolder extends RecyclerView.ViewHolder {

    private CImageView thumIv;
    private TextView distanceTv;
    private TextView timeTv;
    private TextView durationTv;
    private TextView avgPaceTv;

    private TextView tagMonthTv;
    private TextView tagDistanceTv;

    public HistoryListHolder(View itemView) {
        super(itemView);
        thumIv = (CImageView) itemView.findViewById(R.id.thum_iv);
        distanceTv = (TextView) itemView.findViewById(R.id.distance_tv);
        timeTv = (TextView) itemView.findViewById(R.id.time_tv);
        durationTv = (TextView) itemView.findViewById(R.id.duration_tv);
        avgPaceTv = (TextView) itemView.findViewById(R.id.avg_pace_tv);

        tagMonthTv = (TextView) itemView.findViewById(R.id.tag_month_tv);
        tagDistanceTv = (TextView) itemView.findViewById(R.id.tag_distance_tv);
    }

    public void updateView(HistoryDetail historyDetail){
        ImageLoadUtil.loadCommonImage(historyDetail.getThum(), thumIv);
        distanceTv.setText(CommonUtil.format2(historyDetail.getTotal_length() / 1000));
        avgPaceTv.setText(CommonUtil.getPaceTimeStr(historyDetail.getAvg_pace()));
        durationTv.setText(CommonUtil.getPeriodTime(historyDetail.getTotal_time()));
        timeTv.setText(CommonUtil.parseTime(historyDetail.getStartTimesStamp()));

        CommonUtil.setCustomTypeFace(distanceTv);
        CommonUtil.setCustomTypeFace(avgPaceTv);
        CommonUtil.setCustomTypeFace(durationTv);
    }

    public void updateTag(String month, String distance){
        tagMonthTv.setText(month);
        tagDistanceTv.setText(distance);
    }

}
