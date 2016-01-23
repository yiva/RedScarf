package com.redscarf.weidou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.redscarf.weidou.pojo.GoodsBody;

import java.util.List;

/**
 * Created by yeahwang on 2015/10/23.
 */
public class GoodsDetailAdapter extends BaseRedScarfAdapter<GoodsBody>{
    public GoodsDetailAdapter(Context context, List<GoodsBody> listData) {
        super(context, listData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
