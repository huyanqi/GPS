package com.codoon.clubgps.bean;

/**
 * Created by Frankie on 2017/1/13.
 *
 * 封装每列
 * @see com.codoon.clubgps.widget.PaceChatView
 * 数据的POJO类
 *
 */

public class PaceChatViewPojo {

    private String text;//显示在底部的文字
    private float leftValue;//对应的数据
    private String leftText;

    private PaceChatViewPojo() {
    }

    public PaceChatViewPojo(String text, float value) {
        this.text = text;
        this.leftValue = value;
    }

    public String getText() {
        return text;
    }

    public float getLeftValue() {
        return leftValue;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setText(String text) {
        this.text = text;
    }
}