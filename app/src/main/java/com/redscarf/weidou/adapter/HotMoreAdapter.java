package com.redscarf.weidou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.GoodsBody;

import java.util.List;

/**
 * Created by yeahwang on 2016/8/22.
 */
public class HotMoreAdapter extends BaseRecyclerAdapter<GoodsBody>{

    public HotMoreAdapter(Context context, List<GoodsBody> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_buy, null);
        //不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new HotMoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    private class HotMoreViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        private View rootView;
        private TextView title;
        private RecyclerView hot_detail;
        private TextView more;

        public HotMoreViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_category_hot);
            hot_detail = (RecyclerView) itemView.findViewById(R.id.grid_hot_item);
            more = (TextView) itemView.findViewById(R.id.txt_category_more);
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
