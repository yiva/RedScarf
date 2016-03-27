package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * Created by yeahwang on 2016/3/28.
 */
public class MyFavouriteBody implements Parcelable{

    private String id;
    private String title;
    private String subtitle;
    private String exclusive;
    private String expires;
    private String post_thumbnail;
    private String category;
    private String attachments;

    public MyFavouriteBody(){

    }

    protected MyFavouriteBody(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        exclusive = in.readString();
        expires = in.readString();
        post_thumbnail = in.readString();
        category = in.readString();
        attachments = in.readString();
    }

    public static final Creator<MyFavouriteBody> CREATOR = new Creator<MyFavouriteBody>() {
        @Override
        public MyFavouriteBody createFromParcel(Parcel in) {
            return new MyFavouriteBody(in);
        }

        @Override
        public MyFavouriteBody[] newArray(int size) {
            return new MyFavouriteBody[size];
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
        return MyConstants.URL+"wp-content/uploads"+post_thumbnail;
    }

    public void setPost_thumbnail(String post_thumbnail) {
        this.post_thumbnail = post_thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
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
        dest.writeString(category);
        dest.writeString(attachments);
    }
}
