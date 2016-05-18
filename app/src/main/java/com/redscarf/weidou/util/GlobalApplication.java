package com.redscarf.weidou.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.redscarf.weidou.network.VolleyUtil;

import java.util.LinkedList;
import java.util.List;

public class GlobalApplication extends Application{
	
	private static Context context;
	//运用list来保存们每一个activity是关键
	private List<Activity> mList = new LinkedList<Activity>();
	//为了实现每次使用该类时不创建新的对象而创建的静态对象
	private static GlobalApplication instance;
	//实例化一次
	public synchronized static GlobalApplication getInstance(){
		if (null == instance) {
			instance = new GlobalApplication();
		}
		return instance;
	}
	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}
	//关闭每一个list内的activity
	public void exit() {
		try {
			for (Activity activity:mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
	//杀进程
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

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

	/**
	 * 获取屏幕宽度像素值（需转为dip进行计算）
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	public static int getScreenWidth(){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	/**
	 * 获取屏幕高度像素值（需转为dip进行计算）
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static int getScreenHeight(){
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static String getVersion() {
		 try {
				 PackageManager manager = context.getPackageManager();
				 PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
				 String version = info.versionName;
				 return version;
			 } catch (Exception e) {
				 e.printStackTrace();
				return "";
			 }
	 }

}
