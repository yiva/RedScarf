package com.redscarf.weidou.activity;


import android.os.Bundle;

public class SearchActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setActionBarLayout(R.layout.actionbar_search);
	}
}
