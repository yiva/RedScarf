package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.SearchDetailBody;

import java.util.List;

/**
 * Created by yeahwa on 2016/3/3.
 */
public class SearchDetailAdapter extends BaseRedScarfAdapter<SearchDetailBody>{
    public SearchDetailAdapter(Context context, List<SearchDetailBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_search, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.search_content = (TextView) convertView.findViewById(R.id.search_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.search_content.setText(getItem(position).getTitle());
        return convertView;
    }

    private static class ViewHolder {
        int position;
        TextView search_content;
    }
}
