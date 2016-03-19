package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yeahwang on 2016/3/19.
 */
public class AvatarResultBody implements Parcelable{
    private String success;
    private String data;

    public AvatarResultBody(){

    }

    protected AvatarResultBody(Parcel in) {
        success = in.readString();
        data = in.readString();
    }

    public static final Creator<AvatarResultBody> CREATOR = new Creator<AvatarResultBody>() {
        @Override
        public AvatarResultBody createFromParcel(Parcel in) {
            return new AvatarResultBody(in);
        }

        @Override
        public AvatarResultBody[] newArray(int size) {
            return new AvatarResultBody[size];
        }
    };

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(success);
        dest.writeString(data);
    }
}
