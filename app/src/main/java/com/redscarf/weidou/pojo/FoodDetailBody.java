package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by yeahwang on 2016/1/4.
 */
public class FoodDetailBody implements Parcelable{
    private String id;
    private String title;
    private String subtitle;
    private String subtype;
    private String location;
    private String phone;
    private String underground;
    private String website;
    private Integer sponsored;
    private String reservation;
    private String cost;
    private String menu;
    private String others;
    private String content;
    private String tags;
    private String comments;
    private String comment_count;
    private String is_favorate;
    private String post_medium;
    private String post_thumbnail;


    protected FoodDetailBody(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        subtype = in.readString();
        location = in.readString();
        phone = in.readString();
        underground = in.readString();
        website = in.readString();
        reservation = in.readString();
        cost = in.readString();
        menu = in.readString();
        others = in.readString();
        content = in.readString();
        tags = in.readString();
        comments = in.readString();
        comment_count = in.readString();
        is_favorate = in.readString();
        post_medium = in.readString();
        post_thumbnail = in.readString();
    }

    public static final Creator<FoodDetailBody> CREATOR = new Creator<FoodDetailBody>() {
        @Override
        public FoodDetailBody createFromParcel(Parcel in) {
            return new FoodDetailBody(in);
        }

        @Override
        public FoodDetailBody[] newArray(int size) {
            return new FoodDetailBody[size];
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

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUnderground() {
        return underground;
    }

    public void setUnderground(String underground) {
        this.underground = underground;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getSponsored() {
        return sponsored;
    }

    public void setSponsored(Integer sponsored) {
        this.sponsored = sponsored;
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getIs_favorate() {
        return is_favorate;
    }

    public void setIs_favorate(String is_favorate) {
        this.is_favorate = is_favorate;
    }

    public String getPost_medium() {
        return post_medium;
    }

    public void setPost_medium(String post_medium) {
        this.post_medium = post_medium;
    }

    public String getPost_thumbnail() {
        return post_thumbnail;
    }

    public void setPost_thumbnail(String post_thumbnail) {
        this.post_thumbnail = post_thumbnail;
    }

    public FoodDetailBody() {
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
        dest.writeString(location);
        dest.writeString(phone);
        dest.writeString(underground);
        dest.writeString(website);
        dest.writeString(reservation);
        dest.writeString(cost);
        dest.writeString(menu);
        dest.writeString(others);
        dest.writeString(content);
        dest.writeString(tags);
        dest.writeString(comments);
        dest.writeString(comment_count);
        dest.writeString(is_favorate);
        dest.writeString(post_medium);
        dest.writeString(post_thumbnail);
    }
}
