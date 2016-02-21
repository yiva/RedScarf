package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * Created by yeahwang on 2016/2/17.
 */
public class BrandDetailBody implements Parcelable{

    private String id;
    private String title;
    private String url;
    private String website;
    private String customer_service;
    private String deliver_info;
    private String deliver_china;
    private String content;
    private String tags;
    private String subtype;
    private String post_medium;
    private String post_thumbnail;
    private String is_favorate;
    private String attachments;

    public BrandDetailBody() {
    }

    protected BrandDetailBody(Parcel in) {
        id = in.readString();
        title = in.readString();
        url = in.readString();
        website = in.readString();
        customer_service = in.readString();
        deliver_info = in.readString();
        deliver_china = in.readString();
        content = in.readString();
        tags = in.readString();
        subtype = in.readString();
        post_medium = in.readString();
        post_thumbnail = in.readString();
        is_favorate = in.readString();
        attachments = in.readString();
    }

    public static final Creator<BrandDetailBody> CREATOR = new Creator<BrandDetailBody>() {
        @Override
        public BrandDetailBody createFromParcel(Parcel in) {
            return new BrandDetailBody(in);
        }

        @Override
        public BrandDetailBody[] newArray(int size) {
            return new BrandDetailBody[size];
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCustomer_service() {
        return customer_service;
    }

    public void setCustomer_service(String customer_service) {
        this.customer_service = customer_service;
    }

    public String getDeliver_info() {
        return deliver_info;
    }

    public void setDeliver_info(String deliver_info) {
        this.deliver_info = deliver_info;
    }

    public String getDeliver_china() {
        return deliver_china;
    }

    public void setDeliver_china(String deliver_china) {
        this.deliver_china = deliver_china;
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

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getPost_medium() {
        return MyConstants.URL+"wp-content/uploads"+post_medium;
    }

    public void setPost_medium(String post_medium) {
        this.post_medium = post_medium;
    }

    public String getPost_thumbnail() {
        return MyConstants.URL+"wp-content/uploads"+post_thumbnail;
    }

    public void setPost_thumbnail(String post_thumbnail) {
        this.post_thumbnail = post_thumbnail;
    }

    public String getIs_favorate() {
        return is_favorate;
    }

    public void setIs_favorate(String is_favorate) {
        this.is_favorate = is_favorate;
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
        dest.writeString(url);
        dest.writeString(website);
        dest.writeString(customer_service);
        dest.writeString(deliver_info);
        dest.writeString(deliver_china);
        dest.writeString(content);
        dest.writeString(tags);
        dest.writeString(subtype);
        dest.writeString(post_medium);
        dest.writeString(post_thumbnail);
        dest.writeString(is_favorate);
        dest.writeString(attachments);
    }
}
