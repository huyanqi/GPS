package com.codoon.clubcodoongps.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.codoon.clubcodoongps.R;
import com.codoon.clubcodoongps.adapter.vh.PaceChatHolder;
import com.codoon.clubgps.bean.PaceChatBean;

import java.util.List;

/**
 * Created by Frankie on 2017/1/12.
 */

public class PaceChatAdapter extends RecyclerView.Adapter<PaceChatHolder> {

    private List<PaceChatBean> mList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private final int TYPE_ITEM = 0;
    private final int TYPE_KMTEXT = 1;
    private int maxDuration;
    private long maxPace;

    public PaceChatAdapter(Context context, List<PaceChatBean> list){
        this.mContext = context;
        this.mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PaceChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PaceChatHolder(mLayoutInflater.inflate(R.layout.item_pace_chat_new, parent, false));
    }

    @Override
    public void onBindViewHolder(PaceChatHolder holder, int position) {
        PaceChatBean bean = mList.get(position);
        if(getItemViewType(position) == TYPE_ITEM){
            holder.updateView(this, position, maxDuration, maxPace, bean);
        }else{
            holder.updateKmText(bean.getKmText());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(TextUtils.isEmpty(mList.get(position).getKmText())){
            return TYPE_ITEM;
        }
        return TYPE_KMTEXT;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setMaxPace(int maxPace){
        this.maxPace = maxPace;
    }

}
