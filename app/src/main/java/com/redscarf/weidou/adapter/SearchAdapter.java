package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.SearchBody;

import java.util.List;

/**
 * Created by yeahwang on 2016/2/16.
 */
public class SearchAdapter extends BaseRedScarfAdapter<SearchBody> {

    public SearchAdapter(Context context, List<SearchBody> listData) {
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
        viewHolder.search_content.setText("#"+getItem(position).getTerms());
        return convertView;
    }

    private static class ViewHolder {
        int position;
        TextView search_content;
    }
}
