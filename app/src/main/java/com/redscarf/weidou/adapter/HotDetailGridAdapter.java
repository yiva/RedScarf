package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.makeramen.roundedimageview.RoundedImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.HotBody;
import com.redscarf.weidou.util.DisplayUtil;
import com.redscarf.weidou.util.GlobalApplication;

import java.util.List;

/**
 * Created by yeahwang on 2016/8/17.
 */
public class HotDetailGridAdapter extends BaseRedScarfAdapter<HotBody>{

    private final String TAG = HotDetailGridAdapter.class.getSimpleName();

    public HotDetailGridAdapter(Context context, List<HotBody> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.grid_hot, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.title = (TextView) convertView.findViewById(R.id.txt_hot_item_title);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.img_hot_item);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setImageViewMeasure(viewHolder.image, (GlobalApplication.getScreenWidth() - DisplayUtil.dip2px
                (mContext, 40)) / 2);
        viewHolder.position = position;
        viewHolder.title.setText(getItem(position).getSubtitle());
        viewHolder.image.setImageResource(R.drawable.loading_middle);
        String imageUrl = getItem(position).getPost_thumbnail();
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.image,
                    R.drawable.loading_middle, R.drawable.loading_middle);
            imageLoader.get(imageUrl, listener);
        }
        return convertView;
    }

    private static class ViewHolder {
        private int position;
        private View rootView;
        private TextView title;
        private ImageView image;
    }
}
