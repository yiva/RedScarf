package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * Created by yeahwang on 2016/2/29.
 */
public class HotFoodBody implements Parcelable{

    private String id;
    private String title;
    private String subtitle;
    private String subtype;
    private String underground;
    private String cost;
    private String modified;
    private String post_thumbnail;
    private String post_medium;

    public HotFoodBody() {
    }

    protected HotFoodBody(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        subtype = in.readString();
        underground = in.readString();
        cost = in.readString();
        modified = in.readString();
        post_thumbnail = in.readString();
        post_medium = in.readString();
    }

    public static final Creator<HotFoodBody> CREATOR = new Creator<HotFoodBody>() {
        @Override
        public HotFoodBody createFromParcel(Parcel in) {
            return new HotFoodBody(in);
        }

        @Override
        public HotFoodBody[] newArray(int size) {
            return new HotFoodBody[size];
        }
    };

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getUnderground() {
        return underground;
    }

    public void setUnderground(String underground) {
        this.underground = underground;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getPost_medium() {
        return MyConstants.URL + "wp-content/uploads" + post_medium;
    }

    public void setPost_medium(String post_medium) {
        this.post_medium = post_medium;
    }

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

    public String getPost_thumbnail() {
        return MyConstants.URL + "wp-content/uploads" +post_thumbnail;
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
        dest.writeString(subtype);
        dest.writeString(underground);
        dest.writeString(cost);
        dest.writeString(modified);
        dest.writeString(post_thumbnail);
        dest.writeString(post_medium);
    }
}
