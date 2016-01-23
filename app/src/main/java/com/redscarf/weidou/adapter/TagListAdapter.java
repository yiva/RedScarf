package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.BitmapCache;

import java.util.List;

/**
 * Created by yeahwang on 2015/11/13.
 */
public class TagListAdapter extends BaseRedScarfAdapter<String> {

    private final static String TAG = TagListAdapter.class.getSimpleName();

    protected ImageLoader logoLoader;

    public TagListAdapter(Context context, List<String> listData) {
        super(context, listData);
        this.logoLoader = new ImageLoader(queue, new BitmapCache());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.listview_tag, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tag = (TextView) convertView.findViewById(R.id.tags);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tag.setText("#"+getItem(position));
        return convertView;
    }

    private static class ViewHolder{
        TextView tag;
    }
}
