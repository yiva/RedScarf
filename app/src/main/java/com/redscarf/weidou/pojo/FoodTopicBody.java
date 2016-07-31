package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by XZR on 2016/7/1.
 */
public class FoodTopicBody implements Parcelable{
    private String id;
    private String slug;
    private String title;
    private String description;
    private String parent;
    private String post_count;
    private String image;

    public FoodTopicBody() {
    }

    protected FoodTopicBody(Parcel in) {
        id = in.readString();
        slug = in.readString();
        title = in.readString();
        description = in.readString();
        parent = in.readString();
        post_count = in.readString();
        image = in.readString();
    }

    public static final Creator<FoodTopicBody> CREATOR = new Creator<FoodTopicBody>() {
        @Override
        public FoodTopicBody createFromParcel(Parcel in) {
            return new FoodTopicBody(in);
        }

        @Override
        public FoodTopicBody[] newArray(int size) {
            return new FoodTopicBody[size];
        }
    };

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getPost_count() {
        return post_count;
    }

    public void setPost_count(String post_count) {
        this.post_count = post_count;
    }

    public String getImage() throws UnsupportedEncodingException {
        return MyConstants.URL + "wp-content/uploads" + URLEncoder.encode(image,"UTF-8");
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(slug);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(parent);
        dest.writeString(post_count);
        dest.writeString(image);
    }
}
