package com.redscarf.weidou.network;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.redscarf.weidou.pojo.FormImage;

import java.util.ArrayList;
import java.util.List;

//import moon.volley.network.Constant;

/**
 * Created by moon.zhong on 2015/3/2.
 */
public class UploadApi {

    /**
     * 上传图片接口
     * param url 地址
     * @param image 需要上传的图片
     * @param listener 请求回调
     */
    public static void uploadImg(String url,FormImage image,ResponseListener listener){
        List<FormImage> imageList = new ArrayList<FormImage>() ;
        imageList.add(image) ;
        Request request = new PostUploadRequest(url,imageList,listener) ;
        VolleyUtil.getRequestQueue().add(request) ;
    }
}
