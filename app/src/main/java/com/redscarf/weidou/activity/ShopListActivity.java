package com.redscarf.weidou.activity;

import android.os.Bundle;

import com.redscarf.weidou.util.GlobalApplication;

/**
 * Created by yeahwang on 2015/12/27.
 */
public class ShopListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_shop_list);
        GlobalApplication.getInstance().addActivity(this);
    }

    @Override
    public void initView() {

    }
}
