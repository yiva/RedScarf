package com.redscarf.weidou.listener;

import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by yeahwang on 2016/8/3.
 */
public abstract class HidingScrollListener implements AbsListView.OnScrollListener {

    public abstract void onHide();

    public abstract void onShow();
}
