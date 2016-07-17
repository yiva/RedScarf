package com.redscarf.weidou.activity;


import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.MyConstants;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 首次运行欢迎界面
 * @author yeahwang
 *
 */
public class WelcomeActivity extends BaseActivity {

	private Handler mHandler = null;
	private Intent in_main;

	private GifImageView welcome_gif;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_welcome);
		GlobalApplication.getInstance().addActivity(this);

		welcome_gif = (GifImageView) findViewById(R.id.img_welcome);
		// 加载本地gif显示
		try {
			GifDrawable gifDrawable = new GifDrawable(getResources(),
					R.drawable.w1);
			welcome_gif.setImageDrawable(gifDrawable);
		} catch (Resources.NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.mHandler = new Handler();
		this.mHandler.postDelayed(new Runnable() {
			public void run() {

				in_main = new Intent(WelcomeActivity.this, LoginActivity.class);
				startActivity(in_main);
				finish();
			}
		}
				, MyConstants.WELCOME_TIME);
	}

	@Override
	public void initView() {

	}

}
