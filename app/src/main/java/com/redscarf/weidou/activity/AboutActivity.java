package com.redscarf.weidou.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.redscarf.weidou.util.GlobalApplication;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by yeahwang on 2016/5/14.
 */
public class AboutActivity extends BaseActivity{

    private TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);
    }

    @Override
    public void initView() {
        version = (TextView) findViewById(R.id.txt_about_version);
        String str_version = GlobalApplication.getVersion();
        if (StringUtils.isNotBlank(str_version)) {
            version.setText(str_version);
        }
    }

}
