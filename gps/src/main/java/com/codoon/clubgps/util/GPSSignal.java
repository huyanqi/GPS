package com.codoon.clubgps.util;

/**
 * Created by huan on 15/5/25.
 *
 * GPS信号强度
 */
public class GPSSignal {

    private GPSSignal(){}

    private Signal signal = Signal.SEARCHING;

    public GPSSignal (float accuracy){
        if(accuracy > 200){
            signal = Signal.WEAK;
        }else if(accuracy > 30){
            signal = Signal.NORMAL;
        }else if(accuracy > 0){
            signal = Signal.STRONG;
        }else if(accuracy == 0){
            signal = Signal.SEARCHING;
        }else if(accuracy == -1){
            signal = Signal.NONE;
        }
    }

    public Signal getSignal() {
        return signal;
    }

    public enum Signal {
        /* 信号强 */
        STRONG,
        /* 信号一般 */
        NORMAL,
        /* 信号弱 */
        WEAK,
        /* 没有信号 */
        NONE,
        /* 信号搜索中 */
        SEARCHING
    }

}