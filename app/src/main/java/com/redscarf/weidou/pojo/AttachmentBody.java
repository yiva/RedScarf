package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * Created by yeahwang on 2016/7/13.
 */
public class AttachmentBody implements Parcelable {
    private String id;
    private String subtitle;
    private String exclusive;
    private String expires_key;
    private String expires;
    private String post_thumbnail;

    public AttachmentBody() {

    }

    protected AttachmentBody(Parcel in) {
        id = in.readString();
        subtitle = in.readString();
        exclusive = in.readString();
        expires_key = in.readString();
        expires = in.readString();
        post_thumbnail = in.readString();
    }

    public static final Creator<AttachmentBody> CREATOR = new Creator<AttachmentBody>() {
        @Override
        public AttachmentBody createFromParcel(Parcel in) {
            return new AttachmentBody(in);
        }

        @Override
        public AttachmentBody[] newArray(int size) {
            return new AttachmentBody[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        dest.writeString(subtitle);
        dest.writeString(exclusive);
        dest.writeString(expires_key);
        dest.writeString(expires);
        dest.writeString(post_thumbnail);
    }
}
