package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * Created by yeahwang on 2016/2/24.
 */
public class DiscountBody implements Parcelable {
    private String id;
    private String title;
    private String subtitle;
    private String website;
    private String exclusive;
    private String expires;
    private String others;
    private String content;
    private String tags;
    private String post_medium;
    private String deliver_china;
    private String is_favorate;
    private String brand;

    public DiscountBody() {
    }

    protected DiscountBody(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        website = in.readString();
        exclusive = in.readString();
        expires = in.readString();
        others = in.readString();
        content = in.readString();
        tags = in.readString();
        post_medium = in.readString();
        deliver_china = in.readString();
        is_favorate = in.readString();
        brand = in.readString();
    }

    public static final Creator<DiscountBody> CREATOR = new Creator<DiscountBody>() {
        @Override
        public DiscountBody createFromParcel(Parcel in) {
            return new DiscountBody(in);
        }

        @Override
        public DiscountBody[] newArray(int size) {
            return new DiscountBody[size];
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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
        return MyConstants.URL + "wp-content/uploads" + post_medium;
    }

    public void setPost_medium(String post_medium) {
        this.post_medium = post_medium;
    }

    public String getDeliver_china() {
        return deliver_china;
    }

    public void setDeliver_china(String deliver_china) {
        this.deliver_china = deliver_china;
    }

    public String getIs_favorate() {
        return is_favorate;
    }

    public void setIs_favorate(String is_favorate) {
        this.is_favorate = is_favorate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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
        dest.writeString(website);
        dest.writeString(exclusive);
        dest.writeString(expires);
        dest.writeString(others);
        dest.writeString(content);
        dest.writeString(tags);
        dest.writeString(post_medium);
        dest.writeString(deliver_china);
        dest.writeString(is_favorate);
        dest.writeString(brand);
    }
}
