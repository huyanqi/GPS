package com.codoon.clubgps.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Frankie on 2017/1/7.
 *
 * @see HistoryCount 和
 * @see HistoryDetail 的关系表
 *
 */

public class HistoryCountRelation extends DataSupport {

    private String countId;

    private String historyDetailId;

    private HistoryCountRelation(){}

    public HistoryCountRelation(String countId, String historyDetailId){
        this.countId = countId;
        this.historyDetailId = historyDetailId;
    }

    @Override
    public synchronized boolean save() {
        if(DataSupport.where("countId = ? AND historyDetailId = ?", countId, historyDetailId).count(HistoryCountRelation.class) > 0){
            return false;
        }else{
            return super.save();
        }
    }

}
