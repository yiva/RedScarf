package com.redscarf.weidou.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.HotShopBody;
import com.redscarf.weidou.pojo.SearchBody;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by yeahwang on 2016/2/16.
 */
public class ShopSearchAdapter extends BaseRedScarfAdapter<HotShopBody> {

    public ShopSearchAdapter(Context context, List<HotShopBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.listview_buy, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.shop_photo = ((NetworkImageView) convertView.findViewById(R.id.img_shop));
//            viewHolder.title = ((TextView) convertView.findViewById(R.id.txt_title_shop));
            viewHolder.expires = ((TextView) convertView.findViewById(R.id.txt_expires_shop));
            viewHolder.subtitle = ((TextView) convertView.findViewById(R.id.txt_subtitle_shop));
            viewHolder.exclusive = (TextView) convertView.findViewById(R.id.txt_exclusive);
//            viewHolder.shop_ad_icon = ((ImageView) convertView.findViewById(R.id.img_ad_shop));
//            viewHolder.exclusive = ((TextView) convertView.findViewById(R.id.txt_ad_shop));
            viewHolder.layout_expires_shop = (LinearLayout) convertView.findViewById(R.id.layout_expires_shop);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setImageViewMeasure(viewHolder.shop_photo);
//        viewHolder.title.setText(getItem(position).getTitle());
        if ("1".equals(getItem(position).getExpires_key())) {
            viewHolder.layout_expires_shop.setVisibility(View.VISIBLE);
            viewHolder.expires.setText("限时折扣");
        }else{
            viewHolder.expires.setText(getItem(position).getExpires().substring(0, 10));
            if (StringUtils.isBlank(getItem(position).getExpires()) ||
                    StringUtils.contains(getItem(position).getExpires(), "0000-00-00")) {
                viewHolder.layout_expires_shop.setVisibility(View.GONE);
            }
        }
        if ("1".equals(getItem(position).getExclusive())) {
            viewHolder.exclusive.setVisibility(View.VISIBLE);
        }

//        if (5 != mFlag) {
        viewHolder.subtitle.setText(Html.fromHtml(getItem(position).getSubtitle()));
//        }

        String imageUrl = this.mRedScarfBodies.get(position).getPost_thumbnail();
        viewHolder.shop_photo.setTag(imageUrl);
        viewHolder.position = position;
        viewHolder.shop_photo.setImageResource(R.drawable.loading_large);
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            viewHolder.shop_photo.setDefaultImageResId(R.drawable.loading_large);
            viewHolder.shop_photo.setErrorImageResId(R.drawable.loading_large);
            viewHolder.shop_photo.setBackgroundColor(0);
            viewHolder.shop_photo.setImageUrl(imageUrl, imageLoader);
        }
        return convertView;
    }

    private static class ViewHolder {
        int position;
        //        ImageView shop_ad_icon;
        NetworkImageView shop_photo;
        //        TextView exclusive;
        TextView subtitle;
        TextView expires;//有效期
        TextView title;
        LinearLayout layout_expires_shop;
        TextView exclusive;
    }
}
