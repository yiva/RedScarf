package com.redscarf.weidou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.HotBody;
import com.redscarf.weidou.pojo.HotListBody;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 发现list适配器
 * Created by yeahwang on 2016/8/13.
 */
public class HotAdapter extends BaseRecyclerAdapter<HotListBody> {

    private final String TAG = HotAdapter.class.getSimpleName();

    public HotAdapter(Context context, List<HotListBody> data) {
        super(context, data);
    }

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_hot, null);
        //不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new HotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        HotViewHolder holder = (HotViewHolder) viewHolder;
        body = list.get(i);
        holder.title.setText(body.getKey());
    }

    class HotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View rootView;
        private TextView title;

        public HotViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_category_hot);
            rootView = itemView.findViewById(R.id.listview_layout_hot);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(this.getLayoutPosition());
            }
        }
    }

}
