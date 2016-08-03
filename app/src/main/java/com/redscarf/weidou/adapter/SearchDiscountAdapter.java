package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.DiscountBody;
import com.redscarf.weidou.pojo.SearchDetailBody;

import java.util.List;

/**
 * Created by yeahwa on 2016/8/3.
 */
public class SearchDiscountAdapter extends BaseRedScarfAdapter<DiscountBody>{
    public SearchDiscountAdapter(Context context, List<DiscountBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_search_detail, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.search_title = (TextView) convertView.findViewById(R.id.txt_search_detail_title);
            viewHolder.search_content = (TextView) convertView.findViewById(R.id.txt_search_detail_content);
            viewHolder.search_type = (TextView) convertView.findViewById(R.id.txt_search_detail_type);
            viewHolder.search_cost = (TextView) convertView.findViewById(R.id.txt_search_detail_money);
            viewHolder.photo = (NetworkImageView) convertView.findViewById(R.id.img_search_detail);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.search_title.setText(getItem(position).getTitle());
        String imageUrl = "";
        viewHolder.search_content.setText(getItem(position).getSubtitle());

        imageUrl = getItem(position).getPost_thumbnail();
        viewHolder.photo.setBackgroundResource(R.drawable.loading_middle);
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            viewHolder.photo.setDefaultImageResId(R.drawable.loading_middle);
            viewHolder.photo.setErrorImageResId(R.drawable.loading_middle);
            viewHolder.photo.setBackgroundColor(0);
            viewHolder.photo.setImageUrl(imageUrl, imageLoader);
        }
        return convertView;
    }

    private static class ViewHolder {
        int position;
        TextView search_title;
        TextView search_content;
        TextView search_type;
        TextView search_cost;
        NetworkImageView photo;

    }
}
