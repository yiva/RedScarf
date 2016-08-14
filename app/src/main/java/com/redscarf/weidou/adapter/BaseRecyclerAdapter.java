package com.redscarf.weidou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * RecyclerView Base Adapter
 * Created by yeahwang on 2016/8/14.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> list;
    protected T body;

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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
