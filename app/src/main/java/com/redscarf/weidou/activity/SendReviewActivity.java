package com.redscarf.weidou.activity;


import com.redscarf.weidou.activity.fragment.FoodDetailFragment;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SendReviewActivity extends BaseActivity{

	private final String TAG = FoodDetailFragment.class.getSimpleName();

	private TextView contents;//评价内容

	private Bundle datas;
	private int MSG_INDEX; //what.msg
	private String comment_id;

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Bundle indexObj = msg.getData();
			String response = indexObj.getString("response");
			try {
				validCommentResult(new JSONObject(response));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			hideProgressDialog();
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_send_review);

		datas = this.getIntent().getExtras();

		findViewById(R.id.txt_send_review_submit).setOnClickListener(new ReviewPublishListener());

		doRequestURL(RequestURLFactory.getRequestURL(RequestType.SEND_COMMENT,
						new String[]{""+datas.getInt("key"),contents.getText().toString().trim(),"1"}),
				SendReviewActivity.class, handler, MSG_INDEX);

	}


	/**
	 *
	 * @return 评价返回信息
	 */
	public void validCommentResult(JSONObject json) {
		MSG_INDEX = 0;
		comment_id ="-1";
		String info = "评价失败";
		try {
			if (json.getBoolean("status")) {
				MSG_INDEX = 1;
				comment_id = json.getString("comment_id");
				info = "评价成功";
            }
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			info = "评价失败！";
		}finally {
			Toast.makeText(SendReviewActivity.this, info, Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * 评论发布
	 */
	private class ReviewPublishListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Toast.makeText(SendReviewActivity.this, "published", Toast.LENGTH_SHORT).show();
		}

	}
}
