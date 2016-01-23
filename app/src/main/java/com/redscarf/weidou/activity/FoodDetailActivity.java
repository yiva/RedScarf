package com.redscarf.weidou.activity;


import com.redscarf.weidou.activity.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class FoodDetailActivity extends BaseActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_detail);
//		this.setActionBarLayout(R.layout.actionbar_food_detail);
		this.registerButton();
	}

	public void registerButton(){
		findViewById(R.id.actionbar_food_detail_back).setOnClickListener(new OnBackClickListener());
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// getMenuInf later().inflate(R.menu.search, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		return super.onOptionsItemSelected(item);
//	}

//	public void onClick(View v){
//		switch (v.getId()) {
//		case R.id.actionbar_food_detail_back:
//			finish();
//			break;
//		case R.id.btn_food_detail_flag:
//			
//			break;
//		case R.id.btn_food_detail_phone:
//			break;
//		default:
//			break;
//		}
//	}

}
