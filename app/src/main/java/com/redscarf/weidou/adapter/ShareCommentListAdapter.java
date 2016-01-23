package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.redscarf.weidou.pojo.ShareCommentBody;

import java.util.List;

/**
 * 分享二级页评论ListView
 * Created by yeahwang on 2015/11/28.
 */
public class ShareCommentListAdapter extends BaseRedScarfAdapter<ShareCommentBody>{
    public ShareCommentListAdapter(Context context, List<ShareCommentBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
