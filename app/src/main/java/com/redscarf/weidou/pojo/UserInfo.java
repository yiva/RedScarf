package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 注册用户
 * Created by yeahwang on 2015/10/10.
 */
public class UserInfo implements Parcelable{


    private String status;
    private String cookie;
    private String user_id;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserInfo(){

    }
    protected UserInfo(Parcel in) {
        status = in.readString();
        cookie = in.readString();
        user_id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(cookie);
        dest.writeString(user_id);
    }
}
