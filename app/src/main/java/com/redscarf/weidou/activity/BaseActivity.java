package com.redscarf.weidou.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.redscarf.weidou.activity.WebActivity;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.VolleyUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 基础类
 * 继承FragmentActivity
 * @author yeahwa
 *
 */
public abstract class BaseActivity extends FragmentActivity{

	protected final int MSG_INDEX = 1;
	protected final int MSG_IS_FAVOURITE = 2;//make favourite
	protected final int MSG_IS_NOT_FAVOURITE = 3;//unmake favourite

	protected final int PROGRESS_DISVISIBLE = 0;
	protected final int PROGRESS_NO_CANCLE = 1;
	protected final int PROGRESS_CANCLE = 2;

	private ProgressDialog progressDialog;
	private StringRequest stringRequest;

	protected HashMap<String,String> url_map;
	protected FragmentManager basicFragment = getSupportFragmentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		url_map = new HashMap<String,String>();
	}

	/**
	 * 设置ActionBar的布局
	 * @param layoutId
	 *            布局Id
	 */
	protected void setActionBarLayout(int layoutId) {
		final ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			actionBar.setCustomView(layoutId);
		}
	}

	/*
	 * actionbar返回按钮操作
	 */
	public void onBackClick(View v){
		finish();
	}

	protected class OnBackClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			finish();
		}
	}

	/*
       * 提示加载
       */
	protected void showProgressDialog(String title, String message) {
		if (progressDialog == null) {

			progressDialog = ProgressDialog.show(this, title,
					message, true, false);
		} else if (progressDialog.isShowing()) {
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
		}
		progressDialog.setCancelable(true);
		progressDialog.show();
	}

	public void showProgressDialogNoCancelable(String title, String message)
	{
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, title,
					message, true, false);
		} else if (progressDialog.isShowing()) {
			progressDialog.setTitle(title);
			progressDialog.setMessage(message);
		}
		progressDialog.show();
	}

	/*
       * 隐藏提示加载
       */
	protected void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

	/**
	 *
	 * @param url http头
	 * @param map 请求参数
	 * @param clazz 调用Class
	 * @param handler 消息对象
	 * @param MSG 消息标识
	 */
	protected void doRequestURL(String url,HashMap<String,String> map,final Class clazz,final Handler handler,final int MSG) {
		showProgressDialog("", MyConstants.LOADING);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
		}

		stringRequest = new StringRequest(StringRequest.Method.GET, builder.toString(), new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.i(clazz.getSimpleName(), "success");
				Bundle data = new Bundle();
				data.putString("response", s);
				Message message = Message.obtain(handler, MSG);
				message.setData(data);
				handler.sendMessage(message);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.e(clazz.getSimpleName(), "error", volleyError);
			}
		});
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		stringRequest.setTag(clazz.getSimpleName());
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	protected void doRequestURL(String url,final Class clazz,final Handler handler,final int MSG) {
		showProgressDialog("", MyConstants.LOADING);
		Uri.Builder builder = Uri.parse(url).buildUpon();
		stringRequest = new StringRequest(StringRequest.Method.GET, builder.toString(), new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.i(clazz.getSimpleName(), "success");
				Bundle data = new Bundle();
				data.putString("response", s);
				Message message = Message.obtain(handler, MSG);
				message.setData(data);
				handler.sendMessage(message);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.e(clazz.getSimpleName(), "error", volleyError);
			}
		});
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		stringRequest.setTag(clazz.getSimpleName());
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	/**
	 * @param method
	 * @param url
	 * @param clazz
	 * @param handler
	 * @param MSG
	 * @param progressType 1:showProgressDialog
	 */
	protected void doRequestURL(int method, String url, final Class clazz, final Handler handler,
								final int MSG, int progressType) {
		if (progressType == PROGRESS_NO_CANCLE) {
			showProgressDialog("", MyConstants.LOADING);
		}else if(progressType == PROGRESS_CANCLE){
			showProgressDialogNoCancelable("", MyConstants.LOADING);
		}
		Uri.Builder builder = Uri.parse(url).buildUpon();
		stringRequest = new StringRequest(method, builder.toString(), new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.i(clazz.getSimpleName(), "success");
				Bundle data = new Bundle();
				data.putString("response", s);
				Message message = Message.obtain(handler, MSG);
				message.setData(data);
				handler.sendMessage(message);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.e(clazz.getSimpleName(), "error", volleyError);
			}
		});
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		stringRequest.setTag(clazz.getSimpleName());
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	protected void doUploadFile(int method, String url,  final Class clazz, final Handler handler,
								final int MSG, int progressType) {
		if (progressType == PROGRESS_NO_CANCLE) {
			showProgressDialog("", MyConstants.LOADING);
		}else if(progressType == PROGRESS_CANCLE){
			showProgressDialogNoCancelable("", MyConstants.LOADING);
		}
		Uri.Builder builder = Uri.parse(url).buildUpon();
		stringRequest = new StringRequest(method, builder.toString(), new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.i(clazz.getSimpleName(), "success");
				Bundle data = new Bundle();
				data.putString("response", s);
				Message message = Message.obtain(handler, MSG);
				message.setData(data);
				handler.sendMessage(message);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.e(clazz.getSimpleName(), "error", volleyError);
			}
		});
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		stringRequest.setTag(clazz.getSimpleName());
		VolleyUtil.getRequestQueue().add(stringRequest);
	}

	protected class OnJumpToPageClick implements View.OnClickListener{

		private String url;
		private String title;
		private Context context;
		public OnJumpToPageClick(Context mContext, String t, String u){
			this.context = mContext;
			this.title = t;
			this.url = u;
		}
		@Override
		public void onClick(View v) {
			Bundle datas = new Bundle();
			datas.putString("url", url);
			datas.putString("title", title);
			Intent i_web = new Intent(context, WebActivity.class);
			i_web.putExtras(datas);
			startActivity(i_web);
		}
	}


}
