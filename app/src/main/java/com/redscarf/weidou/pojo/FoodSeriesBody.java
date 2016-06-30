package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * Created by XZR on 2016/6/29.
 */
public class FoodSeriesBody implements Parcelable {
    private String id;
    private String slug;
    private String title;
    private String description;
    private String subcategory;
    private String post_count;
    private String image;

    public FoodSeriesBody() {

    }

    protected FoodSeriesBody(Parcel in) {
        id = in.readString();
        slug = in.readString();
        title = in.readString();
        description = in.readString();
        subcategory = in.readString();
        post_count = in.readString();
        image = in.readString();
    }

    public static final Creator<FoodSeriesBody> CREATOR = new Creator<FoodSeriesBody>() {
        @Override
        public FoodSeriesBody createFromParcel(Parcel in) {
            return new FoodSeriesBody(in);
        }

        @Override
        public FoodSeriesBody[] newArray(int size) {
            return new FoodSeriesBody[size];
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

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getPost_count() {
        return post_count;
    }

    public void setPost_count(String post_count) {
        this.post_count = post_count;
    }

    public String getImage() {
        return MyConstants.URL + "wp-content/uploads" + image;
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
        dest.writeString(subcategory);
        dest.writeString(post_count);
        dest.writeString(image);
    }
}
