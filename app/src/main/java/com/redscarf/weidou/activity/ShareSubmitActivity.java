package com.redscarf.weidou.activity;


import android.os.Bundle;

import com.redscarf.weidou.util.GlobalApplication;

public class ShareSubmitActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_share_submit);
		this.setActionBarLayout(R.layout.actionbar_share_submit);
		GlobalApplication.getInstance().addActivity(this);
	}

	@Override
	public void initView() {

	}

}
