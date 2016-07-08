package com.redscarf.weidou.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.pojo.MyFavouriteBody;
import com.redscarf.weidou.util.BitmapCache;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by Will on 6/07/2016.
 */
public class FavouriteAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<MyFavouriteBody> mfavourites;
    private ImageLoader imageLoader;

    public FavouriteAdapter(Context mContext,List<MyFavouriteBody> listData){
        this.mContext = mContext;
        mfavourites = listData;
    }
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        final ViewHolder viewHolder;
        imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_myfavourite, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        viewHolder = new ViewHolder();
        viewHolder.title = (TextView) v.findViewById(R.id.txt_title_my_favourite);
        viewHolder.content = (TextView) v.findViewById(R.id.txt_content_my_favourite);
        viewHolder.time = (TextView) v.findViewById(R.id.txt_time_my_favourite);
        viewHolder.image = (NetworkImageView) v.findViewById(R.id.img_my_favourite);
        viewHolder.expires = (LinearLayout) v.findViewById(R.id.layout_expires_my_favourite);
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
            }
        });
        setImageViewMeasure(viewHolder.image);
        viewHolder.content.setText(getItem(position).getSubtitle());
        viewHolder.title.setText(getItem(position).getTitle());
        viewHolder.time.setText(getItem(position).getExpires());
        if (StringUtils.contains(getItem(position).getExpires(), "0000-00-00") || StringUtils
                .isBlank(getItem(position).getExpires())) {
            viewHolder.expires.setVisibility(View.GONE);
        }

        String imageUrl = getItem(position).getPost_thumbnail();
        viewHolder.image.setBackgroundResource(R.drawable.loading_large);
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            viewHolder.image.setDefaultImageResId(R.drawable.loading_large);
            viewHolder.image.setErrorImageResId(R.drawable.null_large);
            viewHolder.image.setBackgroundColor(0);
            viewHolder.image.setImageUrl(imageUrl, imageLoader);
        }
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {

    }

    @Override
    public int getCount() {
        return mfavourites.size();
    }

    @Override
    public MyFavouriteBody getItem(int i) {
        return mfavourites.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

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
    private static class ViewHolder {
        int position;
        TextView content;
        TextView title;
        TextView time;
        NetworkImageView image;
        LinearLayout expires;
    }
}
