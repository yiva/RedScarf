package com.redscarf.weidou.activity;

import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONException;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.pojo.BrandBody;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GoodsDetailActivity extends BaseActivity{
	
	private final String TAG = GoodsDetailActivity.class.getSimpleName();
	
	private Bundle datas;
	protected ImageLoader imageLoader;
	private final int MSG_INDEX = 3; //msg.what goods

	private BrandBody body;
	private String response;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if (msg.what == MSG_INDEX) {
				Bundle indexObj = msg.getData();
				response = indexObj.getString("response");
				ArrayList<BrandBody> arrRed = null;
				try {
					arrRed = RedScarfBodyAdapter.fromJSON(response, BrandBody.class);
				} catch (JSONException e) {
					ExceptionUtil.printAndRecord(TAG, e);
				}
				body = arrRed.get(0);
				initView();
				hideProgressDialog();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.setActionBarLayout(R.layout.actionbar_goods_detail);
//		this.queue = Volley.newRequestQueue(this);
//		mVolleyQueue = Volley.newRequestQueue(this);
		this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
		//get datas
		datas = this.getIntent().getExtras();

		TextView activity_title = (TextView)findViewById(R.id.title_good_detail);
		activity_title.setText(datas.getString("title"));

		//请求参数
		url_map = new HashMap<String,String>();
		url_map.put("json", "get_brandpost");
		url_map.put("post_id", datas.getString("key"));
		doRequestURL(RequestURLFactory.getRequestURL(RequestType.BRAND_POST, new String[]{datas.getString("key")}), GoodsDetailActivity.class, handler, MSG_INDEX);

		goodsListener();
	}
	
	private void goodsListener(){
		Button buy_submit = (Button) findViewById(R.id.btn_buy_submit_goods_detail);
		
		buy_submit.setOnClickListener(new onBuySubmitClick());
	}
	
	private void initView(){
		TextView title = (TextView) findViewById(R.id.txt_goods_detail_title);
		TextView content = (TextView) findViewById(R.id.txt_goods_detail_business_content);
		TextView subtitle = (TextView) findViewById(R.id.txt_goods_detail_subtitle);
		TextView exclusive = (TextView) findViewById(R.id.txt_exclusive_good_detail);
		TextView expires = (TextView) findViewById(R.id.txt_expires_goods_detail);
		NetworkImageView goodsImage = (NetworkImageView) findViewById(R.id.goods_detail_image);
//		BaseRedScarfAdapter.formatRedScarfBody(body);
		String imageUrl = body.getPost_medium();
		goodsImage.setBackgroundResource(R.drawable.loading_large);
	      if ((imageUrl != null) && (!imageUrl.equals("")))
	      {
	    	  goodsImage.setDefaultImageResId(R.drawable.loading_large);
	    	  goodsImage.setErrorImageResId(R.drawable.null_large);
	    	  goodsImage.setBackgroundColor(0);
	    	  goodsImage.setImageUrl(imageUrl, imageLoader);
	      }
		title.setText(body.getTitle());
		content.setText(Html.fromHtml(body.getContent()));
		subtitle.setText(body.getPhone());
//		if (null != body.getExclusive() && 0!=Integer.parseInt(body.getExclusive())) {
//			exclusive.setVisibility(View.VISIBLE);
//		}
//		expires.setText(body.getExpires().substring(0, 10));
	}

	private class onBuySubmitClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent in_web = new Intent(GoodsDetailActivity.this, WebActivity.class);
			
			Bundle data = new Bundle();
			data.putString("title", body.getTitle());
			data.putString("url", body.getWebsite());
			in_web.putExtras(data);
			
			startActivity(in_web);
		}
		
	}
	
}
