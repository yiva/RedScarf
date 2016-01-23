package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 注册用户
 * Created by yeahwang on 2015/10/10.
 */
public class UserInfo implements Parcelable{


    public UserInfo(){

    }
    protected UserInfo(Parcel in) {
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
