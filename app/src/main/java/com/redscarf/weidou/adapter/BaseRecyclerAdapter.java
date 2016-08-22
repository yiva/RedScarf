package com.redscarf.weidou.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.util.BitmapCache;

import java.util.List;

/**
 * RecyclerView Base Adapter
 * Created by yeahwang on 2016/8/14.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter{

    protected Context mContext;
    protected List<T> list;
    protected T body;

    protected ImageLoader imageLoader;
    protected RequestQueue queue;

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    protected OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.list = data;
        this.queue = VolleyUtil.getRequestQueue();
        this.imageLoader = new ImageLoader(queue, new BitmapCache());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 设置图片大小比为3：2
     * @param v 需设置图片
     */
    protected void setImageViewMeasure(final View v,final Integer width){
        //按比例设置图片大小
        ViewTreeObserver vto = v.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                v.getHeight();
                int height = width * 2 / 3;
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.width = width;
                layoutParams.height = height;
                v.setLayoutParams(layoutParams);
            }
        });
    }

    /**
     * 设置图片大小比为3：2
     * @param v 需设置图片
     */
    protected void setImageViewMeasure(final View v){
        //按比例设置图片大小
        ViewTreeObserver vto = v.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                v.getHeight();
                int width = v.getWidth();
                int height = width * 2 / 3;
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.width = width;
                layoutParams.height = height;
                v.setLayoutParams(layoutParams);
            }
        });
    }
}
