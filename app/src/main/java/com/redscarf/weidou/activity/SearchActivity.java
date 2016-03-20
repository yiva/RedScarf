package com.redscarf.weidou.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.redscarf.weidou.util.GlobalApplication;

public class SearchActivity extends BaseActivity {

	private ImageButton btn_search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		GlobalApplication.getInstance().addActivity(this);
	}

	@Override
	public void initView() {

	}


}
