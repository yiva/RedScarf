package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.redscarf.weidou.util.MyConstants;

/**
 * Created by yeahwang on 2015/10/23.
 */
public class GoodsBody implements Parcelable{

    private String id;
    private String type;
    private String slug;
    private String status;
    private String title;
    private String title_plain;
    private String subtitle;
    private String subtype;
    private String location;
    private String phone;
    private String opentime;
    private String website;
    private String discount_id;
    private String exclusive;
    private String expires;
    private String others;
    private String content;
    private String date;
    private String modified;
    private String categories;
    private String author;
    private String author_thumb;
    private String comments;
    private String comment_count;
    private String comment_status;
    private String post_medium;
    private String post_thumbnail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_plain() {
        return title_plain;
    }

    public void setTitle_plain(String title_plain) {
        this.title_plain = title_plain;
    }

    public String getSubtitle() {
        if (subtitle == null){
            return "";
        }
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

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(String discount_id) {
        this.discount_id = discount_id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor_thumb() {
        return "htttp:"+author_thumb;
    }

    public void setAuthor_thumb(String author_thumb) {
        this.author_thumb = author_thumb;
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

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
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

    public GoodsBody(){

    }
    protected GoodsBody(Parcel in) {
        this.id= in.readString();
        this.type= in.readString();
        this.slug= in.readString();
        this.status= in.readString();
        this.title= in.readString();
        this.title_plain= in.readString();
        this.subtitle= in.readString();
        this.subtype= in.readString();
        this.location= in.readString();
        this.phone= in.readString();
        this.opentime= in.readString();
        this.website= in.readString();
        this.discount_id= in.readString();
        this.exclusive= in.readString();
        this.expires= in.readString();
        this.others= in.readString();
        this.content= in.readString();
        this.date= in.readString();
        this.modified= in.readString();
        this.categories= in.readString();
        this.author= in.readString();
        this.author_thumb= in.readString();
        this.comments= in.readString();
        this.comment_count= in.readString();
        this.comment_status= in.readString();
        this.post_medium= in.readString();
        this.post_thumbnail= in.readString();
    }

    public static final Creator<GoodsBody> CREATOR = new Creator<GoodsBody>() {
        @Override
        public GoodsBody createFromParcel(Parcel in) {
            return new GoodsBody(in);
        }

        @Override
        public GoodsBody[] newArray(int size) {
            return new GoodsBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(slug);
        dest.writeString(status);
        dest.writeString(title);
        dest.writeString(title_plain);
        dest.writeString(subtitle);
        dest.writeString(subtype);
        dest.writeString(location);
        dest.writeString(phone);
        dest.writeString(opentime);
        dest.writeString(website);
        dest.writeString(discount_id);
        dest.writeString(exclusive);
        dest.writeString(expires);
        dest.writeString(others);
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(modified);
        dest.writeString(categories);
        dest.writeString(author);
        dest.writeString(author_thumb);
        dest.writeString(comments);
        dest.writeString(comment_count);
        dest.writeString(comment_status);
        dest.writeString(post_medium);
        dest.writeString(post_thumbnail);
    }
}
