package com.redscarf.weidou.adapter;

import java.util.ArrayList;
import java.util.List;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.GoodsBody;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.DisplayUtil;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuyListAdapter extends BaseRedScarfAdapter<GoodsBody> {

    private int mFlag;

    private int height;
    private int width;

    public BuyListAdapter(Context context, List<GoodsBody> listData, int flag) {
        super(context, listData);
        this.mFlag = flag;
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

//        if (5 != mFlag) {
        viewHolder.subtitle.setText(Html.fromHtml(getItem(position).getSubtitle()));
//        }
        viewHolder.shop_photo.setTag(getItem(position).getPost_thumbnail());
        viewHolder.position = position;
        viewHolder.shop_photo.setImageResource(R.drawable.loading_large);
//        if ((Math.random() * 5) % 2 == 0) {
//            viewHolder.shop_ad_icon.setVisibility(View.VISIBLE);
//        }
//        if ((Math.random() * 5) % 3 == 0) {
//            viewHolder.exclusive.setVisibility(View.VISIBLE);
//        }
        String imageUrl = this.mRedScarfBodies.get(position).getPost_thumbnail();
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            viewHolder.shop_photo.setDefaultImageResId(R.drawable.loading_large);
            viewHolder.shop_photo.setErrorImageResId(R.drawable.null_large);
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
    }

    public static ArrayList<String> denoteCategorys(String categorys) {
        ArrayList<String> items = new ArrayList<String>();
        try {
            JSONArray js = new JSONArray(categorys);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < js.length(); i++) {
                JSONObject jo = js.getJSONObject(i);
                sb.append(jo.getString("title") + ".");
                items.add(jo.getString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

}
