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
    private float value;//对应的数据

    private PaceChatViewPojo() {
    }

    public PaceChatViewPojo(String text, float value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public float getValue() {
        return value;
    }
}