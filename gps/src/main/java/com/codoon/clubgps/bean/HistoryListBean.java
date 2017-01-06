package com.codoon.clubgps.bean;

/**
 * Created by Frankie on 2017/1/6.
 *
 * 运动历史列表adapter用
 */

public class HistoryListBean {

    public boolean isTag;//是否为tag,每个月都有一个tag展示时间和总公里数

    /* isTag=true */
    public String month;//格式:yyyyM   20171
    public String distance;// 3.12公里

    /* isTag=false */
    public HistoryDetail historyDetail;

}
