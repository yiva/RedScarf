package com.redscarf.weidou.activity;


import com.redscarf.weidou.activity.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class UserReviewActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_review);
		this.setActionBarLayout(R.layout.actionbar_user_review);
	}
}
