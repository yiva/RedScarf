package com.redscarf.weidou.activity;


import java.util.ArrayList;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;
import com.redscarf.weidou.network.VolleyUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 欢迎界面
 * @author yeahwa
 *
 */
public class SplashActivity extends BaseActivity {

	private static final String TAG = SplashActivity.class.getSimpleName();

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Bundle indexObj = msg.getData();
			switch (msg.what) {
				case MSG_VALID:
					loginValid(indexObj.getBoolean("valid"));
					break;
			}
		}
	};
	private Intent in_main;
	private JsonObjectRequest jsonObjRequest;

	private ImageView splash_view;

	private final int MSG_VALID = 12; //msg.what valid cookie

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_splash);
		splash_view = (ImageView) findViewById(R.id.splash_bg);
		if (MyPreferences.isFirst(SplashActivity.this)) {
			splash_view.setBackgroundResource(R.drawable.index_bg);

			Handler jumpHandler = new Handler();
			jumpHandler.postDelayed(new SplashHandler(), 3000);
		}else {
			splash_view.setBackgroundResource(this.randomIndexBg());
			Uri.Builder builder = Uri.parse(MyConstants.API_BASE).buildUpon();
			builder.appendEncodedPath("user/validate_auth_cookie/");
			builder.appendQueryParameter("cookie", MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_COOKIE));
			Log.i(TAG, builder.toString());
			jsonObjRequest = new JsonObjectRequest(Request.Method.GET,
					builder.toString(), new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					Log.i(TAG, "success");
					boolean res = false;
					try {
						res = response.getBoolean("valid");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Bundle data = new Bundle();
					data.putBoolean("valid", res);
					Message message = Message.obtain(mHandler, MSG_VALID);
					message.setData(data);
					mHandler.sendMessage(message);
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e(TAG, "error", error);
				}
			});
			//Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
			jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			jsonObjRequest.setTag(TAG);
			VolleyUtil.getRequestQueue().add(jsonObjRequest);
		}
	}

	/**
	 *
	 * @param valid 根据已存在cookie判断登录
	 */
	private void loginValid(Boolean valid) {
		Intent i_login;
		if (valid) {
			i_login = new Intent(SplashActivity.this, BasicViewActivity.class);
		} else {
			MyPreferences.setAppPerenceAttribute(MyConstants.PREF_USER_COOKIE, null);
			i_login = new Intent(SplashActivity.this, LoginActivity.class);

		}
		startActivity(i_login);
		finish();

	}
	private int randomIndexBg(){
		ArrayList<Integer> index_bgs = new ArrayList<Integer>();
		index_bgs.add(R.drawable.w_food);
		index_bgs.add(R.drawable.w_share);
		index_bgs.add(R.drawable.w_shop);
		int num = (int) (Math.random()*3);
		return index_bgs.get(num);
	}

	private class SplashHandler implements Runnable{

		@Override
		public void run() {
			in_main = new Intent(SplashActivity.this, WelcomeActivity.class);
			startActivity(in_main);
			finish();
		}
	}

}
