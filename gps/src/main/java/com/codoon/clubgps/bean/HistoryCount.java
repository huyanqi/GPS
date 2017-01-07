package com.codoon.clubgps.bean;

import com.codoon.clubgps.util.LogUtil;

import org.litepal.annotation.Column;
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

    @Column(ignore = true)
    private static final String TAG = "add2Count";

    private String uuid;
    private String time;//格式:20171
    private int type;//0:周统计 1:月统计

    private long timestamp;//统计生成时间

    public String getUuid() {
        return uuid;
    }

    private HistoryCount(){}

    public HistoryCount(String time, int type){
        this.time = time;
        this.type = type;
        timestamp = System.currentTimeMillis();
        uuid = UUID.randomUUID().toString();
    }

    public int getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    /**
     * 获取所有统计
     * 用于知道哪些周、哪些月有统计数据
     * @return
     */
    public static List<HistoryCount> findAllCount(int type){
        return DataSupport.where("type = ?", type+"").find(HistoryCount.class);
    }

    public List<HistoryCountData> findChildren(){
        return DataSupport.where("count_id = ?", this.uuid).find(HistoryCountData.class);
    }

    /**
     * 添加到统计表中
     * @param historyDetail
     */
    public static void add2Count(HistoryDetail historyDetail){
        //1.添加记录到周统计
        SimpleDateFormat format = new SimpleDateFormat("yyyyw");
        String week = format.format(new Date(historyDetail.getStartTimesStamp()));
        LogUtil.i(TAG, "添加记录到周统计"+week);
        add2Table(historyDetail, week, 0);

        //2.添加记录到月统计
        format = new SimpleDateFormat("yyyyM");
        String month = format.format(new Date(historyDetail.getStartTimesStamp()));
        LogUtil.i(TAG, "添加记录到月统计:"+month);
        add2Table(historyDetail, month, 1);
    }

    private static void add2Table(HistoryDetail historyDetail, String time, int type){
        String saveDataTime = new SimpleDateFormat("yyyyMMdd").format(new Date(historyDetail.getStartTimesStamp()));
        List<HistoryCount> countList = DataSupport.where("time = ? AND type = ?", time, type+"").find(HistoryCount.class);
        HistoryCount historyCount;
        if(countList.size() > 0){
            //存在该时间段的统计
            LogUtil.i(TAG, "存在该时间段的统计");
            historyCount = countList.get(0);
        }else{
            //不存在该时间段的统计
            LogUtil.i(TAG, "不存在该时间段的统计");
            historyCount = new HistoryCount(time, type);
            historyCount.save();
        }

        //保存关系表
        new HistoryCountRelation(historyCount.getUuid(), historyDetail.getUuid()).save();
        //需要更新数据表
        HistoryCountData historyCountData = HistoryCountData.getHistoryCountData(historyCount.getUuid(), saveDataTime);
        LogUtil.i(TAG, "老数据:"+historyDetail.getTotal_length());
        historyCountData.addLength(historyDetail.getTotal_length());
        LogUtil.i(TAG, "新数据:"+historyDetail.getTotal_length());
    }


}
