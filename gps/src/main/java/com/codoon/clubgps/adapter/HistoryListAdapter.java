package com.codoon.clubgps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codoon.clubgps.R;
import com.codoon.clubgps.adapter.listener.OnItemClickListener;
import com.codoon.clubgps.adapter.vh.HistoryListHolder;
import com.codoon.clubgps.application.GPSApplication;
import com.codoon.clubgps.bean.HistoryListBean;

import java.util.List;

/**
 * Created by Frankie on 2017/1/6.
 */

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListHolder> {

    private List<HistoryListBean> mList;
    private LayoutInflater mLayoutInflater;

    private final int TYPE_HEADER = 0;
    private final int TYPE_TAG = 1;
    private final int TYPE_ITEM = 2;

    private int HEADER_COUNT = 1;

    private OnItemClickListener mOnItemClickListener;

    public HistoryListAdapter(List<HistoryListBean> list, OnItemClickListener onItemClickListener) {
        this.mList = list;
        this.mOnItemClickListener = onItemClickListener;
        mLayoutInflater = LayoutInflater.from(GPSApplication.getContext());
    }

    @Override
    public HistoryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER)
            return new HistoryListHolder(mLayoutInflater.inflate(R.layout.item_history_list_header, parent, false));
        else if(viewType == TYPE_TAG){
            return new HistoryListHolder(mLayoutInflater.inflate(R.layout.item_history_list_tag, parent, false));
        }else{
            return new HistoryListHolder(mLayoutInflater.inflate(R.layout.item_history_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(HistoryListHolder holder, int position) {
        int viewType = getItemViewType(position);
        if(viewType == TYPE_HEADER) {
            //更新header

        } else {
            final int finalPosition = getRealPosition(position);
            HistoryListBean historyBean = mList.get(finalPosition);
            if(viewType == TYPE_TAG){
                holder.updateTag(historyBean.month, historyBean.distance);
                holder.itemView.setOnClickListener(null);
            }else{
                holder.updateView(historyBean.historyDetail);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.OnItemClicked(finalPosition);
                    }
                });
            }
        }
    }

    private int getRealPosition(int position){
        if(position == 0) return position;
        return position - HEADER_COUNT;
    }

    @Override
    public int getItemCount() {
        return mList.size() + HEADER_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        if(mList.get(position - HEADER_COUNT).isTag)
            return TYPE_TAG;
        return TYPE_ITEM;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
