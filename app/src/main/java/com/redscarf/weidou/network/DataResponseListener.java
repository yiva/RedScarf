package com.redscarf.weidou.network;

/**
 * Created by gyzhong on 15/3/2.
 */
public interface DataResponseListener<T> extends ResponseListener<T> {
    public void postData(String data) ;
}
