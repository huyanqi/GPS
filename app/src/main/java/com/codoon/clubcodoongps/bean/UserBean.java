package com.codoon.clubcodoongps.bean;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by codoon on 2017/2/4.
 */

public class UserBean extends DataSupport {

    private String name;

    private AvatarBean mAvatarBean;

    private Date birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AvatarBean getAvatarBean() {
        return mAvatarBean;
    }

    public void setAvatarBean(AvatarBean avatarBean) {
        mAvatarBean = avatarBean;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", mAvatarBean=" + mAvatarBean +
                ", birthday=" + birthday +
                '}';
    }
}
