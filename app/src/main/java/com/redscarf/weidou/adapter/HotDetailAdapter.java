package com.redscarf.weidou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.HotBody;
import com.redscarf.weidou.util.DisplayUtil;
import com.redscarf.weidou.util.GlobalApplication;

import java.util.List;

/**
 * Created by yeahwang on 2016/8/17.
 */
public class HotDetailAdapter extends BaseRecyclerAdapter<HotBody>{

    private final String TAG = HotDetailAdapter.class.getSimpleName();

    public HotDetailAdapter(Context context, List<HotBody> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_hot, null);
        //不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new HotDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        HotDetailHolder holder = (HotDetailHolder) viewHolder;
        body = list.get(position);

        String imageUrl = body.getPost_thumbnail();
        holder.image.setTag(imageUrl);
        holder.position = position;
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.image,
                    R.drawable.loading_middle, R.drawable.loading_middle);
            imageLoader.get(imageUrl, listener);
        }
        holder.title.setText(body.getSubtitle());
    }

    class HotDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        private View rootView;
        private TextView title;
        private ImageView image;

        public HotDetailHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_hot_item_title);
            image = (ImageView) itemView.findViewById(R.id.img_hot_item);
            setImageViewMeasure(image, (GlobalApplication.getScreenWidth() - DisplayUtil.dip2px
                    (mContext, 40)) / 2);
            rootView = itemView.findViewById(R.id.layout_grid_hot_item);
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
