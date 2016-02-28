package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.HotFoodBody;
import com.redscarf.weidou.pojo.SearchBody;

import java.util.List;

/**
 * Created by yeahwang on 2016/2/16.
 */
public class FoodSearchAdapter extends BaseRedScarfAdapter<HotFoodBody> {

    public FoodSearchAdapter(Context context, List<HotFoodBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_search_food, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.content = (TextView) convertView.findViewById(R.id.txt_search_food_content);
            viewHolder.title = (TextView) convertView.findViewById(R.id.txt_search_food_title);
            viewHolder.type = (TextView) convertView.findViewById(R.id.txt_search_food_type);
            viewHolder.money = (TextView) convertView.findViewById(R.id.txt_search_food_money);
            viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.img_search_food);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.content.setText(getItem(position).getSubtitle());
        viewHolder.title.setText(getItem(position).getTitle());
        viewHolder.type.setText(getItem(position).getSubtype());
        viewHolder.money.setText(getItem(position).getCost());
        String imageUrl = getItem(position).getPost_medium();
        viewHolder.image.setBackgroundResource(R.drawable.loading_large);
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            viewHolder.image.setDefaultImageResId(R.drawable.loading_large);
            viewHolder.image.setErrorImageResId(R.drawable.null_large);
            viewHolder.image.setBackgroundColor(0);
            viewHolder.image.setImageUrl(imageUrl, imageLoader);
        }
        return convertView;
    }

    private static class ViewHolder {
        int position;
        TextView content;
        TextView title;
        TextView type;
        TextView money;
        NetworkImageView image;
    }
}
