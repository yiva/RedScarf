package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yeahwa on 2015/11/18.
 */
public class BrandBody implements Parcelable{
    private String id;
    private String title;
    private String phone;
    private String website;
    private String others;
    private String content;
    private String tags;
    private String post_medium;
    private String share;
    private String attachments;

    public BrandBody(){

    }
    protected BrandBody(Parcel in) {
        id = in.readString();
        title = in.readString();
        phone = in.readString();
        website = in.readString();
        others = in.readString();
        content = in.readString();
        tags = in.readString();
        post_medium = in.readString();
        share = in.readString();
        attachments = in.readString();
    }

    public static final Creator<BrandBody> CREATOR = new Creator<BrandBody>() {
        @Override
        public BrandBody createFromParcel(Parcel in) {
            return new BrandBody(in);
        }

        @Override
        public BrandBody[] newArray(int size) {
            return new BrandBody[size];
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getPost_medium() {
        return post_medium;
    }

    public void setPost_medium(String post_medium) {
        this.post_medium = post_medium;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
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
        dest.writeString(phone);
        dest.writeString(website);
        dest.writeString(others);
        dest.writeString(content);
        dest.writeString(tags);
        dest.writeString(post_medium);
        dest.writeString(share);
        dest.writeString(attachments);
    }
}

