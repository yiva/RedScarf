package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yeahwang on 2015/10/10.
 */
public class CookieBody implements Parcelable {

    private String status;
    private String cookie;
    private String cookie_name;
    private String user;
    private String error;

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

    public String getCookie_name() {
        return cookie_name;
    }

    public void setCookie_name(String cookie_name) {
        this.cookie_name = cookie_name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public CookieBody() {

    }

    public CookieBody(Parcel in) {
        super();
        this.status = in.readString();
        this.cookie = in.readString();
        this.cookie_name = in.readString();
        this.user = in.readString();
        this.error = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(cookie);
        dest.writeString(cookie_name);
        dest.writeString(user);
        dest.writeString(error);
    }

    public static final Creator<CookieBody> CREATOR = new Creator<CookieBody>() {
        @Override
        public CookieBody createFromParcel(Parcel in) {
            return new CookieBody(in);
        }

        @Override
        public CookieBody[] newArray(int size) {
            return new CookieBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
