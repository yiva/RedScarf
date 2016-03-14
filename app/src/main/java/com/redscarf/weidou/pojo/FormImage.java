package com.redscarf.weidou.pojo;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * 上传图片加载类
 * Created by yeahwang on 2016/1/17.
 */
public class FormImage {
    //参数的名称
    private String mName;
    //文件名
    private String mFileName;
    //文件的 mime，需要根据文档查询
    private String mMime;
    //需要上传的图片资源，因为这里测试为了方便起见，直接把 bigmap 传进来，真正在项目中一般不会这般做，而是把图片的路径传过来，在这里对图片进行二进制转换
    private Bitmap mBitmap;

    public FormImage(String name, String filename, String mime, Bitmap mBitmap) {
        this.mName = name;
        this.mFileName = filename;
        this.mMime = mime;
        this.mBitmap = mBitmap;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }


    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public void setmMime(String mMime) {
        this.mMime = mMime;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getName() {
        //return mName;
        //测试，把参数名称写死
        return this.mName;
    }

    public String getFileName() {
        //测试，直接写死文件的名字
        return this.mFileName;
    }

    //对图片进行二进制转换
    public byte[] getValue() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        return bos.toByteArray();
    }

    //文本类型
    public String getMime() {
        return this.mMime;
    }
}
