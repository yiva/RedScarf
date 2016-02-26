package com.redscarf.weidou.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.redscarf.weidou.network.VolleyUtil;

public class GlobalApplication extends Application{
	
	private static Context context;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		GlobalApplication.context = getApplicationContext();
		initRequestQueue();
	}
	
	public static Context getAppContext(){
		return GlobalApplication.context;
	}

	//初始化请求队列
	private void initRequestQueue(){
		//初始化 volley
		VolleyUtil.initialize(context);
	}
	

}
