package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.GridBody;

import java.util.List;

/**
 * Created by yeahwang on 2015/12/23.
 */
public class ShopGridAdapter extends BaseRedScarfAdapter<GridBody> {
    public ShopGridAdapter(Context context, List<GridBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        // String logo_url = makeUserProfile();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.grid_buy_header, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.photo = (NetworkImageView) convertView.findViewById(R.id.img_grid_buy_header);
            viewHolder.title = (TextView) convertView.findViewById(R.id.txt_grid_buy_header);
            viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.layout_grid_buy_header);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.layout.setBackgroundResource(getItem(position).getBackgroudColor());
        viewHolder.photo.setBackgroundResource(getItem(position).getDrawableSource());
        viewHolder.title.setText(getItem(position).getTitle());

        return convertView;
    }

    private static class ViewHolder {
        private RelativeLayout layout;
        private NetworkImageView photo;
        private TextView title;
    }


}
