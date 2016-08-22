package com.redscarf.weidou.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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

    private final String TAG = BuyListAdapter.class.getSimpleName();

    private int mFlag;

    private int height;
    private int width;

    public BuyListAdapter(Context context, List<GoodsBody> listData) {
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
        } else {
            if (StringUtils.isBlank(getItem(position).getExpires()) ||
                    StringUtils.contains(getItem(position).getExpires(), "0000-00-00")) {
                viewHolder.layout_expires_shop.setVisibility(View.GONE);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    String nowStr = sdf.format(new Date());
                    Date now = sdf.parse(nowStr);
                    Date date = sdf.parse(getItem(position).getExpires());
                    Calendar cNow = Calendar.getInstance();
                    Calendar cDate = Calendar.getInstance();
                    cNow.setTime(now);
                    cDate.setTime(date);
                    int result = cDate.compareTo(cDate);
                    if (0 > result) {
                        viewHolder.expires.setText("已错过");
                        viewHolder.expires.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    } else {
                        viewHolder.expires.setText(getItem(position).getExpires().substring(0, 10));
                    }
                } catch (ParseException e) {
                    ExceptionUtil.printAndRecord(TAG, new Exception("折扣日期格式有误: " + getItem(position) +
                            "--" + getItem(position).getExpires() + "/r/n" + e.getMessage()));
                }
            }
        }
        if ("1".equals(getItem(position).getExclusive())) {
            viewHolder.exclusive.setVisibility(View.VISIBLE);
        } else {
            viewHolder.exclusive.setVisibility(View.GONE);
        }

        viewHolder.subtitle.setText(Html.fromHtml(getItem(position).getSubtitle()));

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
