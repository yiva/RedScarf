package com.redscarf.weidou.adapter;

import java.util.List;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.FoodBody;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.BitmapCache;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

public class FoodListAdapter extends BaseRedScarfAdapter<FoodBody> {

    private int width;
    private int height;

    public FoodListAdapter(Context mContext, List<FoodBody> listData) {
        super(mContext, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_food, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.food_photo = ((NetworkImageView) convertView.findViewById(R.id.img_photo_food));
            viewHolder.title = ((TextView) convertView.findViewById(R.id.txt_title_food));
            viewHolder.subtitle = ((TextView) convertView.findViewById(R.id.txt_subtitle_food));
            viewHolder.distance = (TextView) convertView.findViewById(R.id.txt_distance);
            viewHolder.food_style = ((TextView) convertView.findViewById(R.id.txt_style_food));
            viewHolder.cost = (TextView) convertView.findViewById(R.id.txt_food_cost);
            viewHolder.label_michelin = (LinearLayout) convertView.findViewById(R.id.label_michelin);
            viewHolder.star1 = (ImageView) convertView.findViewById(R.id.ico_michelin_1);
            viewHolder.star2 = (ImageView) convertView.findViewById(R.id.ico_michelin_2);
            viewHolder.star3 = (ImageView) convertView.findViewById(R.id.ico_michelin_3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setImageViewMeasure(viewHolder.food_photo);
        viewHolder.title.setText(getItem(position).getTitle());
        if (StringUtils.isNotEmpty(getItem(position).getDistance())) {
            viewHolder.distance.setText(getItem(position).getDistance()+" mi");
        }
        viewHolder.subtitle.setText(getItem(position).getUnderground());
        viewHolder.food_style.setText(getItem(position).getSubtype());
        viewHolder.position = position;
        viewHolder.food_photo.setImageResource(R.drawable.loading_large);
        viewHolder.cost.setText(getItem(position).getCost());

        if (StringUtils.isNumeric(getItem(position).getPost_michelin()) && Integer.parseInt(getItem
                (position).getPost_michelin()) != 0) {
            viewHolder.label_michelin.setVisibility(View.VISIBLE);
            switch (Integer.parseInt(getItem(position).getPost_michelin())) {
                case 3:
                    viewHolder.star3.setVisibility(View.VISIBLE);
                case 2:
                    viewHolder.star2.setVisibility(View.VISIBLE);
                case 1:
                    viewHolder.star1.setVisibility(View.VISIBLE);
                    break;
                default:
                    viewHolder.label_michelin.setVisibility(View.GONE);
                    break;
            }
        }

        String imageUrl = this.mRedScarfBodies.get(position).getPost_medium();
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            viewHolder.food_photo.setDefaultImageResId(R.drawable.loading_large);
            viewHolder.food_photo.setErrorImageResId(R.drawable.loading_large);
            viewHolder.food_photo.setBackgroundColor(0);
            viewHolder.food_photo.setImageUrl(imageUrl, imageLoader);
        }
        return convertView;
    }

    private static class ViewHolder {
        NetworkImageView food_photo;
        TextView food_style;
        int position;
        TextView title;
        TextView subtitle;
        TextView distance;
        TextView cost;
        LinearLayout label_michelin;
        ImageView star1;
        ImageView star2;
        ImageView star3;
    }
}
