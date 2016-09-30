package com.redscarf.weidou.activity;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
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
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GalleryImageLoader;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
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
    private TableRow btn_mine_logoff;
    private TableRow btn_mine_my_favourite;
    private TableRow btn_mine_about_weidou;
    private ImageButton img_jump_individual;
    private LinearLayout layout_info;
    private View view_404;

    protected ImageLoader imageLoader;
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
                switch (msg.what) {
                    case MSG_NONCE:
                        nonce = (NonceBody) RedScarfBodyAdapter.parseObj(response, Class.forName("com.redscarf.weidou.pojo.NonceBody"));
                        break;
                    case MSG_INDEX:
                        body = (Member) RedScarfBodyAdapter.parseObj(response, Class.forName("com.redscarf.weidou.pojo.Member"));
                        setCurrentContentView();
                        break;
                    case MSG_UPLOAD:
                        if (response != null) {
                            avatar_result = (AvatarResultBody) RedScarfBodyAdapter.parseObj(response, Class.forName("com" +
                                    ".redscarf.weidou.pojo.AvatarResultBody"));
                            if (avatar_result.getSuccess() == "true") {
                                Toast.makeText(MineActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MineActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            }
                        }
                        hideProgressDialog();
                        break;
                    case MSG_ERROR:
                        hideProgressDialog();
                        Bundle errObj = msg.getData();
                        String error = errObj.getString("error");
                        layout_info.setVisibility(View.VISIBLE);
                        view_404 = LayoutInflater.from(MineActivity.this).inflate(R.layout.view_404, layout_info, true);
                        TextView text_404 = (TextView) view_404.findViewById(R.id.txt_404);
                        view_404.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                layout_info.removeAllViews();
                                layout_info.setVisibility(View.GONE);
                                doRequestURL(Request.Method.GET, RequestURLFactory.sysRequestURL(RequestType.MINE_PROFILE,
                                        new String[]{MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_ID)}),
                                        MineActivity.class, handler, MSG_INDEX, PROGRESS_NO_CANCELABLE, "index");
                            }
                        });
                        switch (error) {
                            case "index":
                                text_404.setText("网络出点小故障，再摁下试试!");
                                break;
                            default:
                                text_404.setText("@_@");
                                break;
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                Toast.makeText(MineActivity.this, "信息读取失败", Toast.LENGTH_SHORT).show();
                ExceptionUtil.printAndRecord(TAG, e);

            } finally {
                hideProgressDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        if (!super.isLogin()) {
            JumpToActivity(MineActivity.this, LoginActivity.class, null);
        }
        this.setContentView(R.layout.activity_mine);
        setActionBarLayout(getResources().getString(R.string.title_mine_activity), ActionBarType.WITHBACK);
        GlobalApplication.getInstance().addActivity(this);
        this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
        initView();
        doRequestURL(Request.Method.GET, RequestURLFactory.sysRequestURL(RequestType.MINE_PROFILE,
                new String[]{MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_ID)}),
                MineActivity.class, handler, MSG_INDEX, PROGRESS_NO_CANCELABLE, "index");
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
    @Override
    public void initView() {
        txt_nick_name = (TextView) findViewById(R.id.txt_mine_nickname);
        user_logo = (NetworkImageView) findViewById(R.id.mine_user_photo);
        btn_mine_logoff = (TableRow) findViewById(R.id.btn_mine_logoff);
        img_jump_individual = (ImageButton) findViewById(R.id.btn_jump_individual_mine);
        btn_mine_my_favourite = (TableRow) findViewById(R.id.btn_mine_my_favourite);
        btn_mine_about_weidou = (TableRow) findViewById(R.id.btn_mine_about_weidou);
        layout_info = (LinearLayout) findViewById(R.id.layout_mine_info);
    }

    private void setCurrentContentView() {
        txt_nick_name.setText(body.getNickname());
//		加载用户头像
        String profile_image = body.getAvatar();
        user_logo.setBackgroundResource(R.drawable.loading_large);
        if ((profile_image != null) && (!profile_image.equals(""))) {
            user_logo.setDefaultImageResId(R.drawable.loading_large);
            user_logo.setErrorImageResId(R.drawable.loading_large);
            user_logo.setBackgroundColor(0);
            user_logo.setImageUrl(profile_image, imageLoader);
        }
//        user_logo.setOnClickListener(new changePhoto());
        btn_mine_logoff.setOnClickListener(new OnLogOffClick());
        img_jump_individual.setOnClickListener(new OnJumpIndividualInfoClick());
        btn_mine_my_favourite.setOnClickListener(new OnJumpToActivityClick(MineActivity.this,
                IndividualFavouriteActivity.class, null));
        btn_mine_about_weidou.setOnClickListener(new OnJumpToActivityClick(MineActivity.this,
                AboutActivity.class, null));
    }

    private class OnLogOffClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            MyPreferences.clearAllAppPerenceAttribut();
            Toast.makeText(MineActivity.this, "已登出", Toast.LENGTH_SHORT).show();
            JumpToActivity(MineActivity.this, LoginActivity.class, null);
            finish();
        }
    }

    private class OnJumpIndividualInfoClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Bundle data = new Bundle();
            data.putParcelable("profile_body", body);
            JumpToActivity(MineActivity.this,
                    IndividualInfoActivity.class, data);
        }
    }
}
