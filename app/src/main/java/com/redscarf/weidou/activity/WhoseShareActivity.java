package com.redscarf.weidou.activity;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.network.VolleyUtil;

import android.os.Bundle;
import android.widget.TextView;

/**
 * 分享二级页
 */
public class WhoseShareActivity extends BaseActivity{

	private TextView title;
	private TextView label_user;
	private TextView location;
	private NetworkImageView img_item;
	private TextView content;

	protected ImageLoader imageLoader;
	private Bundle datas;
	private RedScarfBody body;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_whose_share);
		this.setActionBarLayout(R.layout.actionbar_whose_share);
		initView();


	}

	private void initView(){
		datas = getIntent().getExtras();
		body = datas.getParcelable("body");
		this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());

		title = (TextView) findViewById(R.id.title_whose_share);
		label_user = (TextView) findViewById(R.id.txt_whose_share_name);
		location = (TextView) findViewById(R.id.txt_location_whose_share);
		img_item = (NetworkImageView) findViewById(R.id.img_whose_share_photo);
		content = (TextView) findViewById(R.id.txt_whose_share_content);

		title.setText(body.getAuthor()+"的分享");
		label_user.setText(body.getAuthor());
		content.setText(body.getContent());
		String imageUrl = body.getPost_medium();
		img_item.setBackgroundResource(R.drawable.loading_large);
		if ((imageUrl != null) && (!imageUrl.equals("")))
		{
			img_item.setDefaultImageResId(R.drawable.loading_large);
			img_item.setErrorImageResId(R.drawable.null_large);
			img_item.setBackgroundColor(0);
			img_item.setImageUrl(imageUrl, imageLoader);
		}
	}

	private void initTitle(String name){

	}


}
