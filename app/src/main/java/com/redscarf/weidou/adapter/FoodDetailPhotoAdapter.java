package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.BitmapCache;

import java.util.List;

/**
 * Created by yeahwang on 2016/1/25.
 */
public class FoodDetailPhotoAdapter extends BaseRedScarfAdapter<String> {

    private final static String TAG = TagListAdapter.class.getSimpleName();

    protected ImageLoader logoLoader;

    public FoodDetailPhotoAdapter(Context context, List<String> listData) {
        super(context, listData);
        this.logoLoader = new ImageLoader(queue, new BitmapCache());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_food_detail_photo, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.img = (NetworkImageView) convertView.findViewById(R.id.food_photos);
            viewHolder.big_img = (NetworkImageView) convertView.findViewById(R.id.food_photos_big);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.img.setTag(getItem(position));
        viewHolder.big_img.setTag(getItem(position));
        String imageUrl = getItem(position);
        viewHolder.img.setBackgroundResource(R.drawable.loading_large);
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            viewHolder.img.setDefaultImageResId(R.drawable.loading_large);
            viewHolder.img.setErrorImageResId(R.drawable.null_large);
            viewHolder.img.setBackgroundColor(0);
            viewHolder.img.setImageUrl(imageUrl, imageLoader);
//            viewHolder.big_img.setImageUrl(imageUrl,imageLoader);
        }
        return convertView;
    }
    private static class ViewHolder{
        NetworkImageView img;
        NetworkImageView big_img;
    }
}
