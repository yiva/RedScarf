package com.redscarf.weidou.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SearchActivity extends BaseActivity {

	private ImageButton btn_search;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setActionBarLayout(R.layout.actionbar_search);
	}

	private void initView() {
		btn_search = (ImageButton) findViewById(R.id.btnSearch);
		btn_search.setOnClickListener(new OnJumpSearchDetailLinstener());
	}

	private class OnJumpSearchDetailLinstener implements View.OnClickListener{

		@Override
		public void onClick(View v) {

		}
	}
}
