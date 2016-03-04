package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * 搜索列表实体类
 * Created by yeahwa on 2016/3/3.
 */
public class SearchDetailBody implements Parcelable{
    private String id;
    private String title;
    private String subtitle;
    private String subtype;
    private String underground;
    private String cost;
    private String modified;
    private String exclusive;
    private String expires;
    private String url;
    private String website;
    private String deliver_china;
    private String post_thumbnail;
    private String post_medium;
    private String category;

    public SearchDetailBody() {
    }


    protected SearchDetailBody(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        subtype = in.readString();
        underground = in.readString();
        cost = in.readString();
        modified = in.readString();
        exclusive = in.readString();
        expires = in.readString();
        url = in.readString();
        website = in.readString();
        deliver_china = in.readString();
        post_thumbnail = in.readString();
        post_medium = in.readString();
        category = in.readString();
    }

    public static final Creator<SearchDetailBody> CREATOR = new Creator<SearchDetailBody>() {
        @Override
        public SearchDetailBody createFromParcel(Parcel in) {
            return new SearchDetailBody(in);
        }

        @Override
        public SearchDetailBody[] newArray(int size) {
            return new SearchDetailBody[size];
        }
    };

    public String getId() {

        return id;
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

    public String getDeliver_china() {
        return deliver_china;
    }

    public void setDeliver_china(String deliver_china) {
        this.deliver_china = deliver_china;
    }

    public String getPost_thumbnail() {
        return MyConstants.URL+"wp-content/uploads"+post_thumbnail;
    }

    public void setPost_thumbnail(String post_thumbnail) {
        this.post_thumbnail = post_thumbnail;
    }

    public String getPost_medium() {
        return MyConstants.URL+"wp-content/uploads"+post_medium;
    }

    public void setPost_medium(String post_medium) {
        this.post_medium = post_medium;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        dest.writeString(exclusive);
        dest.writeString(expires);
        dest.writeString(url);
        dest.writeString(website);
        dest.writeString(deliver_china);
        dest.writeString(post_thumbnail);
        dest.writeString(post_medium);
        dest.writeString(category);
    }
}
