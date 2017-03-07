package com.codoon.clubcodoongps.widget;

/**
 * Created by Frankie on 2017/3/2.
 *
 * 图表对象
 */

public class ChatPojo {

    /**
     * 对象的值，画图表用
     */
    private float value;

    /**
     * 左侧显示的文字
     */
    private String leftText;

    /**
     * 底部显示的文字
     */
    private String bottomText;

    private ChatPojo(){}

    public ChatPojo(float value, String leftText, String bottomText) {
        this.value = value;
        this.leftText = leftText;
        this.bottomText = bottomText;
    }

    public float getValue() {
        return value;
    }

    public String getLeftText() {
        return leftText;
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    @Override
    public String toString() {
        return "ChatPojo{" +
                "value=" + value +
                ", bottomText='" + bottomText + '\'' +
                '}';
    }
}
