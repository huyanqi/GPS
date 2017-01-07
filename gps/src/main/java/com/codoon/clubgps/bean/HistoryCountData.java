package com.codoon.clubgps.bean;

import android.content.ContentValues;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Frankie on 2017/1/7.
 *
 * 历史记录的具体统计数字
 */

public class HistoryCountData extends DataSupport {

    private String time;//统计的具体哪一天时间,格式:yyyyMMdd

    private String count_id;//所属统计 HistoryCount.uuid

    private double total_length;

    private HistoryCountData(){}

    public HistoryCountData(String time, double total_length, String count_id){
        this.time = time;
        this.total_length = total_length;
        this.count_id = count_id;
    }

    public double getTotal_length() {
        return total_length;
    }

    /**
     * 更新总长度
     * @param newLength
     */
    public void addLength(double newLength){
        ContentValues values = new ContentValues();
        values.put("total_length", this.total_length += newLength);
        DataSupport.update(HistoryCountData.class, values, getBaseObjId());
    }

    /**
     * 检查是否存在
     * @param count_id
     * @param time
     * @return
     */
    public static HistoryCountData getHistoryCountData(String count_id, String time){
        List<HistoryCountData> list = DataSupport.where("count_id = ? AND time = ?", count_id, time).find(HistoryCountData.class);
        HistoryCountData historyCountData;
        if(list.size() > 0){
            historyCountData = list.get(0);
        }else{
            historyCountData = new HistoryCountData(time, 0, count_id);
            historyCountData.save();
        }
        return historyCountData;
    }

}
