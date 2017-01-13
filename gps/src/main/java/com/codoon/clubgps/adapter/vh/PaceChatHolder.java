package com.codoon.clubgps.adapter.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codoon.clubgps.R;
import com.codoon.clubgps.bean.PaceChatBean;

/**
 * Created by Frankie on 2017/1/12.
 */

public class PaceChatHolder extends RecyclerView.ViewHolder {

    private TextView kmTv;
    private View fastTv;
    private TextView paceTv;
    private TextView kmTagTv;
    private ProgressBar durationProgressBar;

    public PaceChatHolder(View itemView) {
        super(itemView);
        kmTv = (TextView) itemView.findViewById(R.id.km_tv);
        fastTv = itemView.findViewById(R.id.fast_tv);
        paceTv = (TextView) itemView.findViewById(R.id.pace_tv);
        kmTagTv = (TextView) itemView.findViewById(R.id.km_tag_tv);
        durationProgressBar = (ProgressBar) itemView.findViewById(R.id.duration_pb);
    }

    public void updateView(int maxDuration, long minPace, PaceChatBean paceChatBean){
        durationProgressBar.setMax(maxDuration);
        durationProgressBar.setProgress((int) paceChatBean.getDuration());

        kmTv.setText(paceChatBean.getKmNumber()+"");
        paceTv.setText(paceChatBean.getAvg_pace());

        kmTv.setVisibility(View.VISIBLE);

        paceTv.setVisibility(View.VISIBLE);
        durationProgressBar.setVisibility(View.VISIBLE);

        kmTagTv.setVisibility(View.INVISIBLE);

        if(minPace == paceChatBean.getAvg_pace_long()){
            fastTv.setVisibility(View.VISIBLE);
        }else{
            fastTv.setVisibility(View.INVISIBLE);
        }

    }

    public void updateKmText(String kmtext){
        kmTagTv.setText(kmtext);

        kmTv.setVisibility(View.INVISIBLE);
        fastTv.setVisibility(View.INVISIBLE);
        paceTv.setVisibility(View.INVISIBLE);
        durationProgressBar.setVisibility(View.INVISIBLE);

        kmTagTv.setVisibility(View.VISIBLE);
    }

}
