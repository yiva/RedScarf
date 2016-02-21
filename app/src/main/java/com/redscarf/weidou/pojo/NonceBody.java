package com.redscarf.weidou.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yeahwang on 2016/1/24.
 */
public class NonceBody implements Parcelable{

    private String method;
    private String nonce;

    public NonceBody() {
    }

    protected NonceBody(Parcel in) {
        method = in.readString();
        nonce = in.readString();
    }

    public static final Creator<NonceBody> CREATOR = new Creator<NonceBody>() {
        @Override
        public NonceBody createFromParcel(Parcel in) {
            return new NonceBody(in);
        }

        @Override
        public NonceBody[] newArray(int size) {
            return new NonceBody[size];
        }
    };

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(method);
        dest.writeString(nonce);
    }
}
