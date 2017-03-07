package com.codoon.clubcodoongps.clubcodoongps;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TestBean implements Parcelable, Serializable {
        
        private int id;

        protected TestBean(Parcel in) {
            id = in.readInt();
        }
        
        public TestBean(){}

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TestBean> CREATOR = new Creator<TestBean>() {
            @Override
            public TestBean createFromParcel(Parcel in) {
                return new TestBean(in);
            }

            @Override
            public TestBean[] newArray(int size) {
                return new TestBean[size];
            }
        };

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

    }