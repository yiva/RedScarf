package com.redscarf.weidou.activity;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.redscarf.weidou.activity.fragment.IndividualModifyFragment;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.pojo.Member;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GalleryImageLoader;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryHelper;
import cn.finalteam.galleryfinal.model.PhotoInfo;


public class MineActivity extends BaseActivity{

	private static final String TAG = MineActivity.class.getSimpleName();

	private NetworkImageView user_logo;
	private TextView txt_nick_name;
	private TextView txt_gender;
	private TextView txt_address;
	private TextView txt_sign;
	private TextView txt_change_photo;

	protected ImageLoader imageLoader;
	private int MSG_MINE = 1;
	private String response;
	private Member body;
	private File photoFile;


	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Bundle indexObj = msg.getData();
			response = indexObj.getString("response");
			try {
				body = (Member) RedScarfBodyAdapter.parseObj(response, Class.forName("com.redscarf.weidou.pojo.Member"));
				hideProgressDialog();
				initView(body);
			} catch (Exception e) {
				Toast.makeText(MineActivity.this, "信息读取失败", Toast.LENGTH_SHORT).show();
				ExceptionUtil.printAndRecord(TAG, e);

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_mine);
//		mVolleyQueue = Volley.newRequestQueue(this);
		this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
		registerButton();
		doRequestURL(RequestURLFactory.getRequestURL(RequestType.MINE_PROFILE,
				new String[]{"6"}),
//				new String[]{MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_ID)}),
				MineActivity.class, handler, MSG_MINE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInf later().inflate(R.menu.search, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 注册控件
	 */
	private void registerButton(){
		user_logo = (NetworkImageView) findViewById(R.id.mine_user_photo);
		txt_nick_name = (TextView) findViewById(R.id.txt_mine_nickname);
//		txt_gender = (TextView) findViewById(R.id.txt_mine_gender);
//		txt_address = (TextView) findViewById(R.id.txt_mine_address);
//		txt_sign = (TextView) findViewById(R.id.txt_mine_);

		this.photoFile = new File("/sdcard/a.jpg");

		user_logo.setOnClickListener(new changePhoto());


	}

	/**
	 * 初始化控件
	 */
	private void initView(Member item){
		txt_nick_name.setText(item.getNickname());
//		还没有
//		txt_gender
//		txt_address
//		txt_sign

//		加载用户头像
		String profile_image = item.getAvatar();
		user_logo.setBackgroundResource(R.drawable.loading_large);
		if ((profile_image != null) && (!profile_image.equals("")))
		{
			user_logo.setDefaultImageResId(R.drawable.loading_large);
			user_logo.setErrorImageResId(R.drawable.null_large);
			user_logo.setBackgroundColor(0);
			user_logo.setImageUrl(profile_image, imageLoader);
		}


	}

	/**
	 * 修改照片
	 */
	private static int SELECT_PICTURE;

	private class changePhoto implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			GalleryHelper.openGallerySingle(MineActivity.this, true, new GalleryImageLoader());

			initImageLoader(MineActivity.this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ( requestCode == GalleryHelper.GALLERY_REQUEST_CODE) {
			if ( resultCode == GalleryHelper.GALLERY_RESULT_SUCCESS ) {
				PhotoInfo photoInfo = data.getParcelableExtra(GalleryHelper.RESULT_DATA);
				List<PhotoInfo> photoInfoList = (List<PhotoInfo>) data.getSerializableExtra(GalleryHelper.RESULT_LIST_DATA);

				if ( photoInfo != null ) {
					com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("file:/" + photoInfo.getPhotoPath(), user_logo);
					//获得图片地址
					photoFile = new File(photoInfo.getPhotoPath());
					if (photoFile.exists()){
						Toast.makeText(this, photoFile.getAbsolutePath().toString(), Toast.LENGTH_SHORT).show();
					}

				}

				if ( photoInfoList != null ) {
					Toast.makeText(this, "选择了" + photoInfoList.size() + "张", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
	}

	private void onMakeModify(View v) {
		TextView modifyTextLabel = null;
		TextView modifyTextValue = null;
		Bundle data = new Bundle();
		switch (v.getId()) {
//			case R.id.btn_modify_mine_nickname: //修改昵称
//				modifyTextLabel = (TextView) findViewById(R.id.label_mine_nickname);
//				modifyTextValue = (TextView) findViewById(R.id.txt_mine_nickname);
//				data.putString("meta_key","nickname");
//				break;
//			case R.id.btn_modify_mine_gender:  //修改性别
//				modifyTextLabel = (TextView) findViewById(R.id.label_mine_gender);
//				modifyTextValue = (TextView) findViewById(R.id.txt_mine_gender);
//				data.putString("meta_key","gender");
//				break;
//			case R.id.btn_modify_mine_sign:  //修改签名
//				modifyTextLabel = (TextView) findViewById(R.id.label_mine_sign);
//				data.putString("meta_key","sign");
//				break;
			default:
				break;
		}
		Intent i_sub = new Intent(MineActivity.this, IndividualModifyFragment.class);

		data.putString("title",modifyTextLabel.getText().toString());
		data.putString("value", modifyTextValue.getText().toString());
		i_sub.putExtras(data);
		startActivity(i_sub);

	}
}
