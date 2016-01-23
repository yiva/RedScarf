package com.redscarf.weidou.util;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 实现Android相册自定义，包括拍照、图片选择（单选/多选）和裁剪。基础接口
 * Created by yeahwang on 2015/11/1.
 */
public class GalleryImageLoader implements cn.finalteam.galleryfinal.ImageLoader {
    @Override
    public void displayImage(final ImageView imageView, String url) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }
}
