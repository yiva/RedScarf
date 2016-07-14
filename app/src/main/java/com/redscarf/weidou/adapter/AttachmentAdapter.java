package com.redscarf.weidou.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.pojo.AttachmentBody;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by yeahwang on 2016/7/13.
 */
public class AttachmentAdapter extends BaseRedScarfAdapter<AttachmentBody>{
    public AttachmentAdapter(Context context, List<AttachmentBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.listview_attachment, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.attachment_photo = ((NetworkImageView) convertView.findViewById(R.id.img_attachment));
                viewHolder.expires = ((TextView) convertView.findViewById(R.id.txt_expires_attachment));
                viewHolder.subtitle = ((TextView) convertView.findViewById(R.id.txt_subtitle_attachment));
                viewHolder.layout_expires_attachment = (LinearLayout) convertView.findViewById(R.id.layout_expires_attachment);
                setImageViewMeasure(viewHolder.attachment_photo);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if ("1".equals(getItem(position).getExpires_key())) {
                viewHolder.expires.setText("限时折扣");
            }else{
                viewHolder.expires.setText(getItem(position).getExpires().substring(0, 10));
                if (StringUtils.isBlank(getItem(position).getExpires()) ||
                        StringUtils.contains(getItem(position).getExpires(), "0000-00-00")) {
                    viewHolder.layout_expires_attachment.setVisibility(View.GONE);
                }
            }

            viewHolder.subtitle.setText(Html.fromHtml(getItem(position).getSubtitle()));
            viewHolder.attachment_photo.setTag(getItem(position).getPost_thumbnail());
            viewHolder.position = position;
            viewHolder.attachment_photo.setImageResource(R.drawable.loading_large);
            String imageUrl = this.mRedScarfBodies.get(position).getPost_thumbnail();
            if ((imageUrl != null) && (!imageUrl.equals(""))) {
                viewHolder.attachment_photo.setDefaultImageResId(R.drawable.loading_large);
                viewHolder.attachment_photo.setErrorImageResId(R.drawable.loading_large);
                viewHolder.attachment_photo.setBackgroundColor(0);
                viewHolder.attachment_photo.setImageUrl(imageUrl, imageLoader);
            }
            return convertView;
    }

    private static class ViewHolder {
        int position;
        NetworkImageView attachment_photo;
        TextView subtitle;
        TextView expires;//有效期
        TextView title;
        LinearLayout layout_expires_attachment;
    }
}
