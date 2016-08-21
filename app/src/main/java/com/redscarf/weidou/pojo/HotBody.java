package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * 发现实体类
 * Created by yeahwang on 2016/8/7.
 */
public class HotBody implements Parcelable{
    private String id;
    private String title;
    private String subtitle;
    private String exclusive;
    private String expires_key;
    private String expires;
    private String deliver_china;
    private String post_thumbnail;

    public HotBody(){

    }

    protected HotBody(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        exclusive = in.readString();
        expires_key = in.readString();
        expires = in.readString();
        deliver_china = in.readString();
        post_thumbnail = in.readString();
    }

    public static final Creator<HotBody> CREATOR = new Creator<HotBody>() {
        @Override
        public HotBody createFromParcel(Parcel in) {
            return new HotBody(in);
        }

        @Override
        public HotBody[] newArray(int size) {
            return new HotBody[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getExclusive() {
        return exclusive;
    }

    public void setExclusive(String exclusive) {
        this.exclusive = exclusive;
    }

    public String getExpires_key() {
        return expires_key;
    }

    public void setExpires_key(String expires_key) {
        this.expires_key = expires_key;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getDeliver_china() {
        return deliver_china;
    }

    public void setDeliver_china(String deliver_china) {
        this.deliver_china = deliver_china;
    }

    public String getPost_thumbnail() {
        return MyConstants.URL + "wp-content/uploads" + post_thumbnail;
    }

    public void setPost_thumbnail(String post_thumbnail) {
        this.post_thumbnail = post_thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(exclusive);
        dest.writeString(expires_key);
        dest.writeString(expires);
        dest.writeString(deliver_china);
        dest.writeString(post_thumbnail);
    }
}
