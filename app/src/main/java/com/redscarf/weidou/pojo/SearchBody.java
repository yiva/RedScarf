package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yeahwang on 2016/2/17.
 */
public class SearchBody implements Parcelable{
    private String terms;
    private String times;
    private String hits;

    public SearchBody() {
    }

    protected SearchBody(Parcel in) {
        terms = in.readString();
        times = in.readString();
        hits = in.readString();
    }

    public static final Creator<SearchBody> CREATOR = new Creator<SearchBody>() {
        @Override
        public SearchBody createFromParcel(Parcel in) {
            return new SearchBody(in);
        }

        @Override
        public SearchBody[] newArray(int size) {
            return new SearchBody[size];
        }
    };

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(terms);
        dest.writeString(times);
        dest.writeString(hits);
    }
}
