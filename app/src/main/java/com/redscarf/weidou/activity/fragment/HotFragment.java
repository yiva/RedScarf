package com.redscarf.weidou.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redscarf.weidou.activity.R;

/**
 * 发现
 * Created by yeahwang on 2016/8/8.
 */
public class HotFragment extends BaseFragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_hot, container,
                false);

        initView();

        return rootView;
    }

    @Override
    public void initView() {

    }
}
