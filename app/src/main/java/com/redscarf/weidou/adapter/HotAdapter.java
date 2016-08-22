package com.redscarf.weidou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.redscarf.weidou.activity.GoodsDetailActivity;
import com.redscarf.weidou.activity.HotMoreActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.HotBody;
import com.redscarf.weidou.pojo.HotListBody;
import com.redscarf.weidou.util.DisplayUtil;
import com.redscarf.weidou.util.GlobalApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现list适配器
 * Created by yeahwang on 2016/8/13.
 */
public class HotAdapter extends BaseRecyclerAdapter<HotListBody> {

    private final String TAG = HotAdapter.class.getSimpleName();

    private final int ONE_LINE_SHOW_NUMBER = 2;

    public HotAdapter(Context context, List<HotListBody> data) {
        super(context, data);
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
        ArrayList<HotBody> items = new ArrayList<>();
        items = body.getHotItems();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, ONE_LINE_SHOW_NUMBER);
        holder.hot_detail.setLayoutManager(layoutManager);
        ViewGroup.LayoutParams layoutParams = holder.hot_detail.getLayoutParams();
        //计算行数
        int lineNumber = items.size() % ONE_LINE_SHOW_NUMBER == 0 ? items.size() / ONE_LINE_SHOW_NUMBER : items
                .size() / ONE_LINE_SHOW_NUMBER + 1;
        //计算高度=行数＊每行的高度 ＋(行数－1)＊10dp的margin ＋ 10dp（为了居中）
        //因为每行显示3个条目，为了保持正方形，那么高度应该是也是宽度/3
        //高度的计算需要自己好好理解，否则会产生嵌套recyclerView可以滑动的现象

        layoutParams.height = getItemHeight(lineNumber);
        holder.hot_detail.setLayoutParams(layoutParams);
        holder.hot_detail.setBackgroundResource(R.color.white);

        //设置间距
//        int spacingInPixels = mContext.getResources().getDimensionPixelOffset(R.dimen.layout_margin3);
//        holder.hot_detail.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        HotDetailAdapter adapter = new HotDetailAdapter(mContext, items);
        holder.hot_detail.setAdapter(adapter);
        adapter.setOnRecyclerViewListener(new OnHotDetailClick(items));

        holder.title.setText(body.getKey());
        holder.more.setOnClickListener(new OnEnterHotMoreClick(body.getId(), body.getKey()));
    }

    class HotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View rootView;
        private TextView title;
        private RecyclerView hot_detail;
        private TextView more;

        public HotViewHolder(View itemView) {
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

    /**
     * recyclerview间距
     */
    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = this.space;
            outRect.right = this.space;
        }
    }

    private int getItemHeight(int lineNumber) {
        int width = (GlobalApplication.getScreenWidth() - DisplayUtil.dip2px(mContext, 48)) /
                ONE_LINE_SHOW_NUMBER; //单个item宽度
        int height = lineNumber * (width * 2 / 3) +
                (lineNumber - 1) * DisplayUtil.dip2px(mContext, 8)
                + lineNumber * DisplayUtil.dip2px(mContext, 48);//textView高度
        return height;
    }

    /**
     * 发现item点击事件
     */
    private class OnHotDetailClick implements HotDetailAdapter.OnRecyclerViewListener {
        private ArrayList<HotBody> items = new ArrayList<>();

        public OnHotDetailClick(ArrayList<HotBody> arr) {
            this.items = arr;
        }

        @Override
        public void onItemClick(int position) {
            Bundle data = new Bundle();
            HotBody item = items.get(position);
            data.putString("id", item.getId());
            data.putString("title", item.getTitle());

            Intent in_shop_detail = new Intent(mContext, GoodsDetailActivity.class);
            in_shop_detail.putExtras(data);
            mContext.startActivity(in_shop_detail);
        }

        @Override
        public boolean onItemLongClick(int position) {
            return false;
        }
    }

    /**
     * 更多
     */
    private class OnEnterHotMoreClick implements View.OnClickListener {

        private String id;
        private String title;

        public OnEnterHotMoreClick(String id, String title) {
            this.id = id;
            this.title = title;
        }

        @Override
        public void onClick(View v) {
            Bundle data = new Bundle();
            data.putString("id", this.id);
            data.putString("title", this.title);

            Intent in_hot_more = new Intent(mContext, HotMoreActivity.class);
            in_hot_more.putExtras(data);
            mContext.startActivity(in_hot_more);
        }
    }

}
