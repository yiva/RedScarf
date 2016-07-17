package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.FoodSeriesBody;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 菜系列表
 * Created by yeahwang on 2016/6/29.
 */
public class FoodSeriesAdapter extends BaseRedScarfAdapter<FoodSeriesBody> {

    public FoodSeriesAdapter(Context context, List<FoodSeriesBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_food_series, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.food_pic = (CircleImageView) convertView.findViewById(R.id.img_food_series);
            viewHolder.food_bg = (CircleImageView) convertView.findViewById(R.id.bg_food_series);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (0 == position) {
            viewHolder.food_pic.setImageResource(R.drawable.all_food);
        } else {
            String imageUrl = this.mRedScarfBodies.get(position).getImage();

            if ((imageUrl != null) && (!imageUrl.equals(""))) {
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.food_pic, R.drawable.loading_middle, R.drawable.loading_min);
                imageLoader.get(imageUrl, listener);
//            viewHolder.food_pic.setDefaultImageResId(R.drawable.loading_middle);
//            viewHolder.food_pic.setErrorImageResId(R.drawable.null_large);
//            viewHolder.food_pic.setBackgroundColor(0);
//            viewHolder.food_pic.setImageUrl(imageUrl, imageLoader);
            }
        }
        if(selectedPosition == position)
        {
            viewHolder.food_bg.setImageResource(R.color.dark_purple);
        } else {
            viewHolder.food_bg.setImageResource(R.color.weidou_purple);
        }
        return convertView;
    }

    private static class ViewHolder {
        int position;
        CircleImageView food_pic;
        CircleImageView food_bg;
    }

}
