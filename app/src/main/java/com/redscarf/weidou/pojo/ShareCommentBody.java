package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yeahwang on 2015/12/1.
 */
public class ShareCommentBody implements Parcelable{

    private String ImageLogo;
    private String name;
    private String contents;
    private String date;

    protected ShareCommentBody(Parcel in) {
        ImageLogo = in.readString();
        name = in.readString();
        contents = in.readString();
        date = in.readString();
    }

    public static final Creator<ShareCommentBody> CREATOR = new Creator<ShareCommentBody>() {
        @Override
        public ShareCommentBody createFromParcel(Parcel in) {
            return new ShareCommentBody(in);
        }

        @Override
        public ShareCommentBody[] newArray(int size) {
            return new ShareCommentBody[size];
        }
    };

    public String getImageLogo() {
        return ImageLogo;
    }

    public void setImageLogo(String imageLogo) {
        ImageLogo = imageLogo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ShareCommentBody(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ImageLogo);
        dest.writeString(name);
        dest.writeString(contents);
        dest.writeString(date);
    }
}
