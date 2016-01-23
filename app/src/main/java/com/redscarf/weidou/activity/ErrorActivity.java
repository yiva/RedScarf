package com.redscarf.weidou.activity;


import com.redscarf.weidou.activity.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ErrorActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_error);
		this.setActionBarLayout(R.layout.actionbar_error);
		
		this.findViewById(R.id.txt_error_submit).setOnClickListener(new onSendMsg());
	}
	
	private class onSendMsg implements OnClickListener{
		@Override
		public void onClick(View v) {
			Toast.makeText(ErrorActivity.this, "send", Toast.LENGTH_SHORT).show();
		}
		
	}
	

}
