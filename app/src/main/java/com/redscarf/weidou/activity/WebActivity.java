package com.redscarf.weidou.activity;


import com.redscarf.weidou.customwidget.ProgressWebView;
import com.redscarf.weidou.util.GlobalApplication;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebActivity extends BaseActivity {

	private Bundle datas;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_web);

		//get datas
		datas = this.getIntent().getExtras();
		ProgressWebView webview = (ProgressWebView) findViewById(R.id.web_view);
		TextView title = (TextView) findViewById(R.id.txt_title_web);
		title.setText(String.valueOf(datas.getString("title")));

		webview.requestFocusFromTouch();
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		//禁止跳转到系统浏览器
		webview.setWebViewClient(new LocalWebViewClient());
		// 加载需要显示的网页
		webview.loadUrl(datas.getString("url"));
		GlobalApplication.getInstance().addActivity(this);


	}

	@Override
	public void initView() {

	}

	/**
	 * 不跳转到系统浏览器
	 */
	private class LocalWebViewClient extends WebViewClient{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}
	}


}
