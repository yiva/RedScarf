package com.redscarf.weidou.activity;


import com.redscarf.weidou.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SearchActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setActionBarLayout(R.layout.actionbar_search);
	}
}
