package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.FoodSeriesBody;

import java.util.List;

/**
 * Created by yeahwang on 2016/2/17.
 */
public class FoodSeriesDetailAdapter extends BaseRedScarfAdapter<FoodSeriesBody> {

    public FoodSeriesDetailAdapter(Context context, List<FoodSeriesBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_food_series_detail, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.food_series_detail = (TextView) convertView.findViewById(R.id.btn_food_series_detail);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.food_series_detail.setText(getItem(position).getTitle());

        if(selectedPosition == position)
        {
            viewHolder.food_series_detail.setBackgroundResource(R.drawable.corners_bg_dark_purple);
        } else {
            viewHolder.food_series_detail.setBackgroundResource(R.drawable.corners_bg_purple);
        }
        return convertView;
    }
    private static class ViewHolder {
        int position;
        TextView food_series_detail;
    }
}
