package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.pojo.SearchDetailBody;

import java.util.List;

/**
 * Created by yeahwa on 2016/3/3.
 */
public class SearchDetailAdapter extends BaseRedScarfAdapter<SearchDetailBody>{
    private String mCcategory;
    public SearchDetailAdapter(Context context, List<SearchDetailBody> listData) {
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
        switch (getItem(position).getCategory()){
            case "4":
                viewHolder.search_content.setText(getItem(position).getUnderground());
                viewHolder.search_type.setVisibility(View.VISIBLE);
                viewHolder.search_type.setText(getItem(position).getSubtype());
                viewHolder.search_cost.setVisibility(View.VISIBLE);
                viewHolder.search_cost.setText(getItem(position).getCost());

                imageUrl = getItem(position).getPost_medium();
                viewHolder.photo.setBackgroundResource(R.drawable.loading_large);
                if ((imageUrl != null) && (!imageUrl.equals(""))) {
                    viewHolder.photo.setDefaultImageResId(R.drawable.loading_large);
                    viewHolder.photo.setErrorImageResId(R.drawable.null_large);
                    viewHolder.photo.setBackgroundColor(0);
                    viewHolder.photo.setImageUrl(imageUrl, imageLoader);
                }
                break;
            case "5":
                viewHolder.search_content.setText(getItem(position).getSubtitle());

                imageUrl = getItem(position).getPost_thumbnail();
                viewHolder.photo.setBackgroundResource(R.drawable.loading_large);
                if ((imageUrl != null) && (!imageUrl.equals(""))) {
                    viewHolder.photo.setDefaultImageResId(R.drawable.loading_large);
                    viewHolder.photo.setErrorImageResId(R.drawable.null_large);
                    viewHolder.photo.setBackgroundColor(0);
                    viewHolder.photo.setImageUrl(imageUrl, imageLoader);
                }
                break;
            case "283":
                imageUrl = getItem(position).getPost_medium();
                viewHolder.photo.setBackgroundResource(R.drawable.loading_large);
                if ((imageUrl != null) && (!imageUrl.equals(""))) {
                    viewHolder.photo.setDefaultImageResId(R.drawable.loading_large);
                    viewHolder.photo.setErrorImageResId(R.drawable.null_large);
                    viewHolder.photo.setBackgroundColor(0);
                    viewHolder.photo.setImageUrl(imageUrl, imageLoader);
                }
                break;
            default:
                break;
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
