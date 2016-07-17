package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * Created by yeahwang on 2016/2/29.
 */
public class HotShopBody implements Parcelable{

    private String id;
    private String title;
    private String subtitle;
    private String exclusive;
    private String expires;
    private String post_thumbnail;
    private String expires_key;

    public HotShopBody() {
    }

    protected HotShopBody(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        exclusive = in.readString();
        expires = in.readString();
        post_thumbnail = in.readString();
        expires_key = in.readString();
    }

    public static final Creator<HotShopBody> CREATOR = new Creator<HotShopBody>() {
        @Override
        public HotShopBody createFromParcel(Parcel in) {
            return new HotShopBody(in);
        }

        @Override
        public HotShopBody[] newArray(int size) {
            return new HotShopBody[size];
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

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getPost_thumbnail() {
        return MyConstants.URL + "wp-content/uploads" +post_thumbnail;
    }

    public String getExpires_key() {
        return expires_key;
    }

    public void setExpires_key(String expires_key) {
        this.expires_key = expires_key;
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
        dest.writeString(expires);
        dest.writeString(post_thumbnail);
        dest.writeString(expires_key);
    }
}
