package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redscarf.weidou.activity.R;

import java.util.List;

import cn.finalteam.toolsfinal.StringUtils;

/**
 * Created by yeahwang on 2016/2/17.
 */
public class BrandDetailAdapter extends BaseRedScarfAdapter<String> {

    public BrandDetailAdapter(Context context, List<String> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_brand_detail, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.brand_detail = (TextView) convertView.findViewById(R.id.txt_brand_detail);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.brand_detail.setText(getItem(position).split("#")[1]);
        return convertView;
    }
    private static class ViewHolder {
        int position;
        TextView brand_detail;
    }
}
