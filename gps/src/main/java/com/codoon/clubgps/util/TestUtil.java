package com.codoon.clubgps.util;

/**
 * Created by Frankie on 2017/2/8.
 */

public class TestUtil {

    private static String[] units = new String[]{"","十","百","千","万"};
    private static String[] nText = new String[]{"零","一","二","三","四","五","六","七","八","九"};

    /**
     * 将秒数转换成 HH:mm:ss格式
     * @param duration
     * @return
     */
    public String timeToText(int duration){
        int hours = (int) Math.floor(duration / 3600);
        int minutes = (int) Math.floor(duration / 60 - Math.floor(duration / 3600) * 60);
        int srconds = (int) (Math.floor(duration) - Math.floor((duration/60)*60));

        return hours+"小时"+minutes+"分"+srconds+"秒";
    }

    /**
     * 阿拉伯转中文
     * @param number
     * @return
     */
    public String numToCN(double number){
        //1.拆分出整数和小数
        String[] numStr = String.valueOf(number).split("\\.");
        //2.取整数部分
        String integer = numStr.length > 0 ? numStr[0] : String.valueOf(number);
        //3.取小数部分
        String decimal = numStr.length > 1 ? numStr[1] : "";

        return getIntegerStr(integer) + getDecimalStr(decimal);
    }

    /**
     * 取整数部分的语音
     * @param integer
     * @return
     */
    private String getIntegerStr(String integer){
        //1.拆分每个数字
        String[] numbers = integer.split("");
        int unitIndex = integer.length() - 1;
        int number;
        StringBuffer sb = new StringBuffer();
        String numStr;
        String unit;
        boolean numFlag;//是否取这个数字
        boolean unitFlag;//是否取单位
        for(int i=0;i<numbers.length;i++){
            unitFlag = true;
            numFlag = true;
            unit = "";
            number = Integer.parseInt(numbers[i]);
            numStr = "";
            if(number == 0) {
                //如果是0，判断是否需要取文本和单位
                if(unitIndex == 4){//万位
                    numFlag = false;
                    unitFlag = true;
                } else {//万位以上或以下逻辑一样
                    //单位不取
                    //数字根据条件判断，如果上一位已经是文字零，那就不取数字
                    unitFlag = false;
                    int length = sb.length();
                    String lastText = sb.substring(length-1,length);
                    numFlag = !lastText.equals(nText[0]);
                }
            }

            if(numFlag){
                numStr = nText[number];
            }

            if(unitFlag){
                unit = units[unitIndex];
            }

            unitIndex --;
            sb.append(numStr+unit);
        }

        //去除最后的零
        String sbStr = sb.toString();
        for(int i = sbStr.length(); i > 0 ; i--){
            if(sbStr.substring(i-1,i).equals(nText[0])){
                sbStr = sbStr.substring(0, i-1);
            }else{
                break;
            }
        }

        return sbStr;
    }

    /**
     * 取小数部分的语音
     * @param decimal
     * @return
     */
    private String getDecimalStr(String decimal){
        if(Double.parseDouble(decimal) == 0d) return "";
        StringBuffer sb = new StringBuffer("点");
        String[] dArry = decimal.split("");
        for(String number : dArry){
            sb.append(nText[Integer.parseInt(number)]);
        }
        return sb.toString();
    }

}