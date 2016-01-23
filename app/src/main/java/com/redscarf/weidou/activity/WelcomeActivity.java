package com.redscarf.weidou.activity;

import pl.droidsonroids.gif.GifImageView;

import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/**
 * 首次运行欢迎界面
 * @author yeahwang
 *
 */
public class WelcomeActivity extends BaseActivity{

	private Handler mHandler = null;
	private Intent in_main;

	private GifImageView welcome_gif;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_welcome);
		welcome_gif = (GifImageView) findViewById(R.id.img_welcome);
		welcome_gif.setBackgroundResource(R.drawable.w1);
		this.mHandler = new Handler();
		this.mHandler.postDelayed(new Runnable()
		{
			public void run()
			{

				in_main = new Intent(WelcomeActivity.this, LoginActivity.class);
				startActivity(in_main);
				finish();
			}
		}
				, MyConstants.WELCOME_TIME);
	}

}
