package com.codoon.clubcodoongps.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by codoon on 2017/2/4.
 */

public class AvatarBean extends DataSupport {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AvatarBean{" +
                "url='" + url + '\'' +
                '}';
    }
}
