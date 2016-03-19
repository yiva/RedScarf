package com.redscarf.weidou.activity;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.redscarf.weidou.activity.fragment.IndividualModifyFragment;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.network.MultipartEntity;
import com.redscarf.weidou.network.MultipartRequest;
import com.redscarf.weidou.pojo.AvatarResultBody;
import com.redscarf.weidou.pojo.Member;
import com.redscarf.weidou.pojo.NonceBody;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GalleryImageLoader;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryHelper;
import cn.finalteam.galleryfinal.model.PhotoInfo;


public class MineActivity extends BaseActivity {

    private static final String TAG = MineActivity.class.getSimpleName();

    private NetworkImageView user_logo;
    private TextView txt_nick_name;
    private TextView txt_gender;
    private TextView txt_address;
    private TextView txt_sign;
    private TextView txt_change_photo;

    protected ImageLoader imageLoader;
    private int MSG_MINE = 1;
    private int MSG_NONCE = 2;
    private int MSG_UPLOAD = 3;
    private String response;
    private Member body;
    private NonceBody nonce;
    private AvatarResultBody avatar_result;
    private File photoFile;
    private ByteArrayOutputStream bos = null;
    private FileInputStream fStream = null;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            response = indexObj.getString("response");
            try {
                if (msg.what == MSG_NONCE) {
                    nonce = (NonceBody) RedScarfBodyAdapter.parseObj(response, Class.forName("com.redscarf.weidou.pojo.NonceBody"));
//                    uploadImg(nonce.getNonce());
                }else if (msg.what == MSG_MINE) {
                    body = (Member) RedScarfBodyAdapter.parseObj(response, Class.forName("com.redscarf.weidou.pojo.Member"));
                    initView(body);
                }else if (msg.what == MSG_UPLOAD) {
                    if (response != null) {
                        avatar_result = (AvatarResultBody) RedScarfBodyAdapter.parseObj(response, Class.forName("com" +
                                ".redscarf.weidou.pojo.AvatarResultBody"));
                        if(avatar_result.getSuccess() == "true"){
                            Toast.makeText(MineActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MineActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgressDialog();
                }
            } catch (Exception e) {
                Toast.makeText(MineActivity.this, "信息读取失败", Toast.LENGTH_SHORT).show();
                ExceptionUtil.printAndRecord(TAG, e);

            }finally {
                hideProgressDialog();
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
        doRequestURL(RequestURLFactory.sysRequestURL(RequestType.MINE_PROFILE,
                        new String[]{MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_ID)}),
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
    private void registerButton() {
        user_logo = (NetworkImageView) findViewById(R.id.mine_user_photo);
        txt_nick_name = (TextView) findViewById(R.id.txt_mine_nickname);
//		txt_gender = (TextView) findViewById(R.id.txt_mine_gender);
//		txt_address = (TextView) findViewById(R.id.txt_mine_address);
//		txt_sign = (TextView) findViewById(R.id.txt_mine_);

//        this.photoFile = new File("/sdcard/a.jpg");

        user_logo.setOnClickListener(new changePhoto());


    }

    /**
     * 初始化控件
     */
    private void initView(Member item) {
        txt_nick_name.setText(item.getNickname());
//		加载用户头像
        String profile_image = item.getAvatar();
        user_logo.setBackgroundResource(R.drawable.loading_large);
        if ((profile_image != null) && (!profile_image.equals(""))) {
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

    private class changePhoto implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            GalleryHelper.openGallerySingle(MineActivity.this, true, new GalleryImageLoader());

            initImageLoader(MineActivity.this);
        }
    }

    private void uploadImg() {
        doRequestURL(StringRequest.Method.POST, RequestURLFactory.getRequestURL(RequestType
                        .UPLOAD_AVATOR, new String[]{MyPreferences.getAppPerenceAttribute
                        ("user_cookie")}),
                MineActivity.class, handler, MSG_UPLOAD, 1);
        try {
            fStream.close();
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GalleryHelper.GALLERY_REQUEST_CODE)
            if (resultCode == GalleryHelper.GALLERY_RESULT_SUCCESS) {
                PhotoInfo photoInfo = data.getParcelableExtra(GalleryHelper.RESULT_DATA);
                List<PhotoInfo> photoInfoList = (List<PhotoInfo>) data.getSerializableExtra(GalleryHelper.RESULT_LIST_DATA);

                if (photoInfo != null) {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("file:/" + photoInfo.getPhotoPath(), user_logo);
                    //获得图片地址
                    photoFile = new File(photoInfo.getPhotoPath());
                    if (photoFile.exists()) {
                        try {
                            showProgressDialogNoCancelable("","图片上传中......");
                            String filename = photoFile.getName();
                            int dot = filename.lastIndexOf('.');
                            if ((dot > -1) && (dot < (filename.length()))) {
                                filename = filename.substring(0, dot);
                            }
//                            RequestQueue queue = Volley.newRequestQueue(this);

                            MultipartRequest multipartRequest = new MultipartRequest(
                                    RequestURLFactory.sysRequestURL(RequestType
                                            .UPLOAD_AVATOR, new String[]{MyPreferences
                                            .getAppPerenceAttribute("user_cookie"), filename}),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.i(MineActivity.class.getSimpleName(), response);
                                            Bundle data = new Bundle();
                                            data.putString("response", response);
                                            Message message = Message.obtain(handler, MSG_UPLOAD);
                                            message.setData(data);
                                            handler.sendMessage(message);
                                        }

                                    });
                            // 添加header
                            multipartRequest.addHeader("header-name", "value");
                            // 通过MultipartEntity来设置参数
                            MultipartEntity multi = multipartRequest.getMultiPartEntity();
                            // 文本参数
                            multi.addStringPart("userCookie", MyPreferences
                                    .getAppPerenceAttribute(MyConstants.PREF_USER_COOKIE));
                            multi.addStringPart("userCookie_name", MyPreferences
                                    .getAppPerenceAttribute(MyConstants.PREF_USER_COOKIE_NAME));
                            // 上传文件
                            multi.addFilePart("file", photoFile);
                            // 将请求添加到队列中
                            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            multipartRequest.setTag(MineActivity.class.getSimpleName());
                            VolleyUtil.getRequestQueue().add(multipartRequest);
//                            queue.add(multipartRequest);
                        } catch (Exception ex) {
                            hideProgressDialog();
                            Toast.makeText(MineActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            ExceptionUtil.printAndRecord(TAG,ex);
                        }

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

        data.putString("title", modifyTextLabel.getText().toString());
        data.putString("value", modifyTextValue.getText().toString());
        i_sub.putExtras(data);
        startActivity(i_sub);

    }
}
