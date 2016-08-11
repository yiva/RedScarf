package com.redscarf.weidou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.network.MultipartEntity;
import com.redscarf.weidou.network.MultipartRequest;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.pojo.AvatarResultBody;
import com.redscarf.weidou.pojo.Member;
import com.redscarf.weidou.pojo.NonceBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GalleryImageLoader;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryHelper;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 个人信息界面
 * Created by yeahwang on 2016/3/25.
 */
public class IndividualInfoActivity extends BaseActivity {

    private static final String TAG = IndividualInfoActivity.class.getSimpleName();

    private TextView txt_nickname;
    private TextView txt_gender;
    private TextView txt_birth;
    private TextView txt_addr;
    private NetworkImageView img_photo;

    private Bundle datas;
    protected ImageLoader imageLoader;
    private File photoFile;
    private ByteArrayOutputStream bos = null;
    private FileInputStream fStream = null;

    private int MSG_MINE = 1;
    private int MSG_NONCE = 2;
    private String response;
    private Member body;
    private NonceBody nonce;
    private AvatarResultBody avatar_result;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            response = indexObj.getString("response");
            try {
                if (msg.what == MSG_NONCE) {
                    nonce = (NonceBody) RedScarfBodyAdapter.parseObj(response, Class.forName("com.redscarf.weidou.pojo.NonceBody"));
                }else if (msg.what == MSG_MINE) {
                    body = (Member) RedScarfBodyAdapter.parseObj(response, Class.forName("com.redscarf.weidou.pojo.Member"));
                    initView();
                }else if (msg.what == MSG_UPLOAD) {
                    if (response != null) {
                        avatar_result = (AvatarResultBody) RedScarfBodyAdapter.parseObj(response, Class.forName("com" +
                                ".redscarf.weidou.pojo.AvatarResultBody"));
                        if(avatar_result.getSuccess() == "true"){
                            Toast.makeText(IndividualInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(IndividualInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                    hideProgressDialog();
                }
            } catch (Exception e) {
                Toast.makeText(IndividualInfoActivity.this, "信息读取失败", Toast.LENGTH_SHORT).show();
                ExceptionUtil.printAndRecord(TAG, e);

            }finally {
                hideProgressDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_info);

        this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
        //get datas
        datas = this.getIntent().getExtras();
        initView();
    }

    @Override
    public void initView() {
        setActionBarLayout(getResources().getString(R.string.title_individual_info), ActionBarType.WITHBACK);
        Member m = datas.getParcelable("profile_body");

        txt_nickname = (TextView) findViewById(R.id.txt_nickname_individual_info);
        txt_gender = (TextView) findViewById(R.id.txt_gender_individual_info);
        txt_birth = (TextView) findViewById(R.id.txt_birthday_individual_info);
        txt_addr = (TextView) findViewById(R.id.txt_address_individual_info);
        img_photo = (NetworkImageView) findViewById(R.id.img_photo_individual_info);

        txt_nickname.setText(m.getNickname());

        //加载用户头像
        String profile_image = m.getAvatar();
        img_photo.setBackgroundResource(R.drawable.loading_large);
        if ((profile_image != null) && (!profile_image.equals(""))) {
            img_photo.setDefaultImageResId(R.drawable.loading_large);
            img_photo.setErrorImageResId(R.drawable.loading_large);
            img_photo.setBackgroundColor(0);
            img_photo.setImageUrl(profile_image, imageLoader);
        }
        img_photo.setOnClickListener(new changePhoto());

    }

    private class OnModifyClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView label = null;
            TextView content = null;
            switch (v.getId()){
                case R.id.txt_nickname_individual_info: //修改昵称
                    label = (TextView) findViewById(R.id.label_nickname_individual_info);
                    content = (TextView) findViewById(R.id.txt_nickname_individual_info);
                    break;
                case R.id.txt_gender_individual_info: //修改性别
                    label = (TextView) findViewById(R.id.label_gender_individual_info);
                    content = (TextView) findViewById(R.id.txt_gender_individual_info);
                    break;
                case R.id.txt_birthday_individual_info: //修改生日
                    label = (TextView) findViewById(R.id.label_birthday_individual_info);
                    content = (TextView) findViewById(R.id.txt_birthday_individual_info);
                    break;
                case R.id.txt_address_individual_info: //修改地址
                    label = (TextView) findViewById(R.id.label_address_individual_info);
                    content = (TextView) findViewById(R.id.txt_address_individual_info);
                    break;
            }
            String title = String.valueOf(label.getText().toString().trim());
            String value = String.valueOf(content.getText().toString().trim());
            if (!StringUtils.isBlank(value)) {
                Bundle data = new Bundle();
                data.putString("title", title);
                data.putString("value", value);
                Intent intent = new Intent(IndividualInfoActivity.this, ModifyActivity.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        }
    }
    /**
     * 修改照片
     */

    private class changePhoto implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            GalleryHelper.openGallerySingle(IndividualInfoActivity.this, true, new GalleryImageLoader());

            initImageLoader(IndividualInfoActivity.this);
        }
    }

    public static void initImageLoader(Context context) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GalleryHelper.GALLERY_REQUEST_CODE)
            if (resultCode == GalleryHelper.GALLERY_RESULT_SUCCESS) {
                PhotoInfo photoInfo = data.getParcelableExtra(GalleryHelper.RESULT_DATA);
                List<PhotoInfo> photoInfoList = (List<PhotoInfo>) data.getSerializableExtra(GalleryHelper.RESULT_LIST_DATA);

                if (photoInfo != null) {
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage
                            ("file:/" + photoInfo.getPhotoPath(), img_photo);
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
                            Toast.makeText(IndividualInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            ExceptionUtil.printAndRecord(TAG, ex);
                        }

                    }

                }

            }
    }
}
