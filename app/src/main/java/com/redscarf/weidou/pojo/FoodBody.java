package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * Created by yeahwang on 2015/10/23.
 */
public class FoodBody implements Parcelable {
    private String id;
    private String title;
    private String subtitle;
    private String subtype;
    private String modified;
    private String post_thumbnail;

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

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getPost_thumbnail() {
        return MyConstants.URL+"wp-content/uploads"+post_thumbnail;
    }

    public void setPost_thumbnail(String post_thumbnail) {
        this.post_thumbnail = post_thumbnail;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public FoodBody(){

    }

    protected FoodBody(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.subtitle = in.readString();
        this.subtype = in.readString();
        this.modified = in.readString();
        this.post_thumbnail = in.readString();

    }

    public static final Creator<FoodBody> CREATOR = new Creator<FoodBody>() {
        @Override
        public FoodBody createFromParcel(Parcel in) {
            return new FoodBody(in);
        }

        @Override
        public FoodBody[] newArray(int size) {
            return new FoodBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(subtype);
        dest.writeString(modified);
        dest.writeString(post_thumbnail);

    }
}
