package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 存储发现list
 * Created by yeahwang on 2016/8/14.
 */
public class HotListBody implements Parcelable{
    private String key;
    private ArrayList<HotBody> hotItems;

    public HotListBody() {

    }

    protected HotListBody(Parcel in) {
        key = in.readString();
        hotItems = in.createTypedArrayList(HotBody.CREATOR);
    }

    public static final Creator<HotListBody> CREATOR = new Creator<HotListBody>() {
        @Override
        public HotListBody createFromParcel(Parcel in) {
            return new HotListBody(in);
        }

        @Override
        public HotListBody[] newArray(int size) {
            return new HotListBody[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<HotBody> getHotItems() {
        return hotItems;
    }

    public void setHotItems(ArrayList<HotBody> hotItems) {
        this.hotItems = hotItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeTypedList(hotItems);
    }
}
