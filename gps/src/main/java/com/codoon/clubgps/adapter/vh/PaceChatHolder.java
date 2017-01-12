package com.codoon.clubgps.adapter.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
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
    private RelativeLayout durationLy;
    private TextView kmTagTv;

    public PaceChatHolder(View itemView) {
        super(itemView);
        kmTv = (TextView) itemView.findViewById(R.id.km_tv);
        fastTv = itemView.findViewById(R.id.fast_tv);
        paceTv = (TextView) itemView.findViewById(R.id.pace_tv);
        durationLy = (RelativeLayout) itemView.findViewById(R.id.duration_ly);
        kmTagTv = (TextView) itemView.findViewById(R.id.km_tag_tv);
    }

    public void updateView(PaceChatBean paceChatBean){
        System.out.println(durationLy.getWidth());
        kmTv.setText(paceChatBean.getKmNumber()+"");
        paceTv.setText(paceChatBean.getAvg_pace());

        kmTv.setVisibility(View.VISIBLE);
        fastTv.setVisibility(View.VISIBLE);
        durationLy.setVisibility(View.VISIBLE);

        kmTagTv.setVisibility(View.INVISIBLE);
    }

    public void updateKmText(String kmtext){
        kmTagTv.setText(kmtext);

        kmTv.setVisibility(View.INVISIBLE);
        fastTv.setVisibility(View.INVISIBLE);
        durationLy.setVisibility(View.INVISIBLE);

        kmTagTv.setVisibility(View.VISIBLE);
    }

}
