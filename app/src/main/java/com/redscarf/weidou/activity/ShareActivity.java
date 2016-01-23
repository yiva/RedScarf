package com.redscarf.weidou.activity;


import com.redscarf.weidou.activity.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareActivity extends BaseActivity {

	private ImageView photo;
	private TextView content;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_share);
		this.setActionBarLayout(R.layout.actionbar_share);
		this.showPhotoAndContent();
	}

	/*
	 * 对图片和内容进行显示和更新
	 */
	private void showPhotoAndContent() {

		photo = (ImageView) findViewById(R.id.img_share_photo);
		content = (TextView) findViewById(R.id.txt_share_content);

		photo.setBackgroundResource(R.drawable.photo_share_food);
		StringBuffer str = new StringBuffer();
		str.append("其实，到了这家店，汉堡真的就算了……到哪儿都能吃，"
				+ "给你一个当高富帅的机会难道你真的不珍惜么？……咱当然是"
				+ "冲着龙虾去啊！一盘大大的龙虾，回味无穷……另外再加一分"
				+ "薯条和莎拉，好吧，这个这个就算刚刚提到……");
		content.setText(str);
	}



}
