package com.codoon.clubgps.bean;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Frankie on 2017/1/7.
 *
 * 历史统计
 *
 */

public class HistoryCount extends DataSupport {

    private String uuid;
    private String time;//格式:20171
    private int type;//0:周统计 1:月统计

    public String getUuid() {
        return uuid;
    }

    private HistoryCount(){}

    public HistoryCount(String time, int type){
        this.time = time;
        this.type = type;
        uuid = UUID.randomUUID().toString();
    }

    /**
     * 添加到统计表中
     * @param historyDetail
     */
    public static void add2Count(HistoryDetail historyDetail){
        //1.添加记录到周统计
        SimpleDateFormat format = new SimpleDateFormat("yyyyw");
        String week = format.format(new Date(historyDetail.getStartTimesStamp()));
        add2Table(historyDetail, week, 0);

        //2.添加记录到月统计
        format = new SimpleDateFormat("yyyyM");
        String month = format.format(new Date(historyDetail.getStartTimesStamp()));
        add2Table(historyDetail, month, 1);
    }

    private static void add2Table(HistoryDetail historyDetail, String time, int type){
        List<HistoryCount> countList = DataSupport.where("time = ? AND type = ?", time, type+"").find(HistoryCount.class);
        HistoryCount historyCount;
        if(countList.size() > 0){
            //存在该时间段的统计,直接添加到关系表
            historyCount = countList.get(0);
        }else{
            //不存在该时间段的统计，添加统计表，再添加到关系表
            historyCount = new HistoryCount(time, type);
            historyCount.save();
        }

        new HistoryCountRelation(historyCount.getUuid(), historyDetail.getUuid()).save();
    }


}
