package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redscarf.weidou.activity.R;

import java.util.List;

/**
 * Created by XZR on 2016/6/28.
 */
public class FoodSelectListAdapter extends BaseRedScarfAdapter<String>  {
    public FoodSelectListAdapter(Context context, List<String> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_food_select, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.food_select = (TextView) convertView.findViewById(R.id.food_select);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.food_select.setText(getItem(position));
        return convertView;
    }

    private static class ViewHolder {
        int position;
        TextView food_select;
    }
}
