package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 品牌查询列表
 * Created by yeahwang on 2016/1/29.
 */
public class BrandsListAdapter extends BaseRedScarfAdapter<String> {

    public BrandsListAdapter(Context context, List<String> listData) {
        super(context, listData);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_brand, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.brand_title = (TextView) convertView.findViewById(R.id.brands);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        String key = "";
//        try {
//            JSONArray sub_jas = new JSONArray(getItem(position));
//            key = new JSONObject(sub_jas.get(0).toString()).getString("title").substring(0, 1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        viewHolder.brand_title.setText(getItem(position));
        return convertView;
    }

    private static class ViewHolder {
        int position;
        TextView brand_title;
    }

    private List<String> parseBrandDetailList(JSONArray jsonArray) {
        
        return null;
    }
}
