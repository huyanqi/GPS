package com.codoon.clubcodoongps.adapter.vh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codoon.clubcodoongps.R;
import com.codoon.clubcodoongps.adapter.PaceChatAdapter;
import com.codoon.clubgps.bean.PaceChatBean;

/**
 * Created by Frankie on 2017/1/12.
 */

public class PaceChatHolder extends RecyclerView.ViewHolder {

    private ViewGroup contentLy;
    private TextView kmTv;
    private LinearLayout itemLy;
    private TextView paceTv;
    private TextView fastTv;
    private TextView tagTv;

    public PaceChatHolder(View itemView) {
        super(itemView);
        contentLy = (ViewGroup) itemView.findViewById(R.id.content_ly);
        kmTv = (TextView) itemView.findViewById(R.id.km_tv);
        itemLy = (LinearLayout) itemView.findViewById(R.id.item_ly);
        paceTv = (TextView) itemView.findViewById(R.id.pace_tv);
        fastTv = (TextView) itemView.findViewById(R.id.fast_tv);
        tagTv = (TextView) itemView.findViewById(R.id.tag_tv);
    }

    public void updateView(final PaceChatAdapter adapter, final int position, double maxDuration, long maxPace, PaceChatBean paceChatBean){
        paceTv.setText(paceChatBean.getAvg_pace());
        kmTv.setText(paceChatBean.getKmNumber()+"");
        System.out.println("paceTv.getWidth():"+paceTv.getWidth());
        if(paceTv.getWidth() != 0) {
            int width = (int) ((paceChatBean.getDuration() / maxDuration) * itemLy.getWidth());
            width = width < paceTv.getWidth() ? paceTv.getWidth() : width;
            System.out.println("width:"+width);
            //paceTv.setWidth(width);
            ViewGroup.LayoutParams lp = paceTv.getLayoutParams();
            lp.width = width;
            paceTv.setLayoutParams(lp);
            fastTv.setVisibility(paceChatBean.getAvg_pace_long() == maxPace ? View.VISIBLE : View.INVISIBLE);
            //fastTv.setX(width);//+ CommonUtil.dip2px(5)
            tagTv.setVisibility(View.INVISIBLE);
            contentLy.setVisibility(View.VISIBLE);
        }else{
            paceTv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    paceTv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    adapter.notifyItemChanged(position);
                }
            });
        }
    }

    public void updateKmText(String kmtext){
        tagTv.setText(kmtext);
        tagTv.setVisibility(View.VISIBLE);
        contentLy.setVisibility(View.INVISIBLE);
    }

}
