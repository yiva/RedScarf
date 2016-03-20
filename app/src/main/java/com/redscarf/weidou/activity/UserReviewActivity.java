package com.redscarf.weidou.activity;


import android.os.Bundle;

import com.redscarf.weidou.util.GlobalApplication;

public class UserReviewActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_review);
		this.setActionBarLayout(R.layout.actionbar_user_review);
		GlobalApplication.getInstance().addActivity(this);
	}

	@Override
	public void initView() {

	}
}
