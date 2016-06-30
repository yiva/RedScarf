package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.FoodSeriesBody;
import com.redscarf.weidou.pojo.FoodTopicBody;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 菜系列表
 * Created by yeahwang on 2016/6/29.
 */
public class FoodMoreAdapter extends BaseRedScarfAdapter<FoodTopicBody> {

    public FoodMoreAdapter(Context context, List<FoodTopicBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_food_more, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.food_topic = (CircleImageView) convertView.findViewById(R.id.img_food_more);
            viewHolder.title = (TextView) convertView.findViewById(R.id.txt_food_more);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String imageUrl = this.mRedScarfBodies.get(position).getImage();
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.food_topic,
                    R.drawable.loading_middle, R.drawable.loading_min);
            imageLoader.get(imageUrl, listener);
        }
        viewHolder.title.setText(this.mRedScarfBodies.get(position).getTitle());
        return convertView;
    }

    private static class ViewHolder {
        int position;
        CircleImageView food_topic;
        TextView title;
    }

}
