package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.MyFavouriteBody;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by yeahwang on 2016/3/28.
 */
public class MyFavouriteAdapter extends BaseRedScarfAdapter<MyFavouriteBody> {
    public MyFavouriteAdapter(Context context, List<MyFavouriteBody> listData) {
        super(context, listData);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_my_favourite, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.txt_title_my_favourite);
            viewHolder.content = (TextView) convertView.findViewById(R.id.txt_content_my_favourite);
            viewHolder.time = (TextView) convertView.findViewById(R.id.txt_time_my_favourite);
            viewHolder.image = (NetworkImageView) convertView.findViewById(R.id.img_my_favourite);
            viewHolder.expires = (LinearLayout) convertView.findViewById(R.id.layout_expires_my_favourite);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
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
        return convertView;
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
