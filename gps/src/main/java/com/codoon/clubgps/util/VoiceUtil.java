package com.codoon.clubgps.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 2017/2/8.
 */

public class VoiceUtil {

    private int[] units;
    private int[] nText;
    private int dotRes;
    private int hourRes;
    private int minuteRes;
    private int secRes;

    private VoiceUtil() {
    }

    public VoiceUtil(int[] units, int[] nText, int dotRes, int hourRes, int minuteRes, int secRes) {
        this.units = units;
        this.nText = nText;
        this.dotRes = dotRes;
        this.hourRes = hourRes;
        this.minuteRes = minuteRes;
        this.secRes = secRes;
    }

    /**
     * 阿拉伯转资源
     *
     * @param number
     * @return
     */
    public List<Integer> numToList(double number) {
        //1.拆分出整数和小数
        String[] numStr = String.valueOf(number).split("\\.");
        //2.取整数部分
        String integer = numStr.length > 0 ? numStr[0] : String.valueOf(number);
        //3.取小数部分
        String decimal = numStr.length > 1 ? numStr[1] : "";

        List<Integer> sourceList = new ArrayList<>();
        sourceList.addAll(getIntegerStr(integer));
        sourceList.addAll(getDecimalStr(decimal));
        return sourceList;
    }

    /**
     * 时间转资源
     *
     * @param duration 时间，单位:s
     * @return
     */
    public List<Integer> timeToList(int duration) {
        int hours = (int) Math.floor(duration / 3600);
        int minutes = (int) Math.floor(duration / 60 - Math.floor(duration / 3600) * 60);
        int seconds = (int) (Math.floor(duration) - Math.floor((duration / 60) * 60));
        List<Integer> resList = new ArrayList<>();
        //拼接资源
        if(hours > 0){
            resList.addAll(numToList(hours));
            resList.add(hours);
        }
        if(minutes > 0){
            resList.addAll(numToList(minutes));
            resList.add(minuteRes);
        }
        if(seconds > 0){
            resList.addAll(numToList(seconds));
            resList.add(secRes);
        }
        return resList;
    }

    /**
     * 取整数部分的语音
     *
     * @param integer
     * @return
     */
    private List<Integer> getIntegerStr(String integer) {
        List<Integer> sourceList = new ArrayList<>();
        //1.拆分每个数字
        String[] numbers = integer.split("");
        int unitIndex = integer.length() - 1;
        int number;
        int numRes;
        int unitRes;
        boolean numFlag;//是否取这个数字
        boolean unitFlag;//是否取单位
        for (int i = 0; i < numbers.length; i++) {
            unitFlag = true;
            numFlag = true;
            if (numbers[i].equals("")) continue;
            number = Integer.parseInt(numbers[i]);
            unitRes = 0;
            numRes = 0;
            if (number == 0) {
                //如果是0，判断是否需要取文本和单位
                if (unitIndex == 4) {//万位
                    numFlag = false;
                    unitFlag = true;
                } else {//万位以上或以下逻辑一样
                    //单位不取
                    //数字根据条件判断，如果上一位已经是文字零，那就不取数字
                    unitFlag = false;
                    numFlag = numRes != nText[0];
                }
            }

            if (numFlag) {
                numRes = nText[number];
                sourceList.add(numRes);
            }

            if (unitFlag) {
                unitRes = units[unitIndex];
                sourceList.add(unitRes);
            }

            unitIndex--;
        }

        //去除最后的零
        for (int i = sourceList.size() - 1; i > 0; i--) {
            if (sourceList.get(i) == nText[0]) sourceList.remove(i);
            else break;
        }

        return sourceList;
    }

    /**
     * 取小数部分的语音
     *
     * @param decimal
     * @return
     */
    private List<Integer> getDecimalStr(String decimal) {
        List<Integer> sourceList = new ArrayList<>();
        if (Double.parseDouble(decimal) == 0d) return sourceList;
        sourceList.add(dotRes);
        String[] dArry = decimal.split("");
        for (String number : dArry) {
            if (number.equals("")) continue;
            sourceList.add(nText[Integer.parseInt(number)]);
        }
        return sourceList;
    }

}
