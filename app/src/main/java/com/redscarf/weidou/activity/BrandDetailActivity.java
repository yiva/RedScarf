package com.redscarf.weidou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.pojo.BrandDetailBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yeahwang on 2016/2/17.
 */
public class BrandDetailActivity extends BaseActivity {

    private static final String TAG = BrandDetailActivity.class.getSimpleName();

    private TextView title;
    private TextView label;
    private TextView description;
    private NetworkImageView photo;
    private Button website;
    private Button service;
    private Button postinfo;
    private ImageButton share;
    private ImageButton favourite;

    private Bundle datas;
     //msg.what goods

    private String response;
    private BrandDetailBody brand_body;
    protected ImageLoader imageLoader;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            response = indexObj.getString("response");
            switch (msg.what) {
                case MSG_INDEX:
                    ArrayList<BrandDetailBody> arrRed = new ArrayList<>();
                    try {
                        arrRed = RedScarfBodyAdapter.fromJSON(response, BrandDetailBody.class);
                        brand_body = arrRed.get(0);
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    }
                    initView();
                    hideProgressDialog();
                    break;
                case MSG_IS_FAVOURITE:
                    try {
                        JSONObject jo = new JSONObject(response);
                        if ("true".equals(jo.getString("result")) || "true" == jo.getString
                                ("result")) {
                            brand_body.setIs_favorate("1");
                            favourite.setBackgroundResource(R.drawable.ic_favourite_red);
                            Toast.makeText(BrandDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                        Toast.makeText(BrandDetailActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MSG_IS_NOT_FAVOURITE:
                    try {
                        JSONObject jo = new JSONObject(response);
                        if ("true".equals(jo.getString("result")) || "true" == jo.getString
                                ("result")) {
                            brand_body.setIs_favorate("0");
                            favourite.setBackgroundResource(R.drawable.ic_favourite_white);
                            Toast.makeText(BrandDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                        Toast.makeText(BrandDetailActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_brand_detail);
        datas = this.getIntent().getExtras();
        this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
        GlobalApplication.getInstance().addActivity(this);
        doRequestURL(RequestURLFactory.getRequestURL(RequestType.BRAND_POST,
                new String[]{datas.getString("id")}), BrandDetailActivity.class, handler, MSG_INDEX);
    }

    @Override
    public void initView() {
        setActionBarLayout(brand_body.getTitle(), ActionBarType.WITHSHARE);
        //register
        description = (TextView) findViewById(R.id.txt_brand_detail_description);
        photo = (NetworkImageView) findViewById(R.id.img_brand_detail_photo);
        website = (Button) findViewById(R.id.btn_brand_detail_website);
        service = (Button) findViewById(R.id.btn_brand_detail_service);
        postinfo = (Button) findViewById(R.id.btn_brand_detail_post_info);
        favourite = (ImageButton) findViewById(R.id.actionbar_with_share_favorite);
        share = (ImageButton) findViewById(R.id.actionbar_with_share_share);

        description.setText(String.valueOf(StringUtils.substringBetween(brand_body.getSubtype(), "\"")));
        description.setVisibility(View.VISIBLE);

        //load image
        String imageUrl = brand_body.getPost_medium();
        photo.setBackgroundResource(R.drawable.loading_large);
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            photo.setDefaultImageResId(R.drawable.loading_large);
            photo.setErrorImageResId(R.drawable.null_large);
            photo.setBackgroundColor(0);
            photo.setImageUrl(imageUrl, imageLoader);
        }

        //favourite button
        if (brand_body.getIs_favorate().equals("1") || brand_body.getIs_favorate() == "1") {
            favourite.setBackgroundResource(R.drawable.ic_favourite_red);
        }

        //set listener
        website.setOnClickListener(new OnJumpToPageClick(BrandDetailActivity.this, brand_body.getTitle(), brand_body.getWebsite()));
        service.setOnClickListener(new OnJumpToPageClick(BrandDetailActivity.this, brand_body.getTitle(), brand_body.getCustomer_service()));
        postinfo.setOnClickListener(new OnJumpToPageClick(BrandDetailActivity.this, brand_body.getTitle(),brand_body.getDeliver_info()));
        share.setOnClickListener(new OnSharePage());
        favourite.setOnClickListener(new OnChangeFavourite());
    }

    /**
     * 收藏功能
     */
    private class OnChangeFavourite implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isLogin()) {
                JumpToActivity(BrandDetailActivity.this, LoginActivity.class, null);
            }
            switch (brand_body.getIs_favorate()) {
                case "0"://make favourite
                    doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor(RequestType.MAKE_FAVOURITE,
                                    new String[]{brand_body.getId()}), BrandDetailActivity.class, handler,
                            MSG_IS_FAVOURITE, 0);
                    break;
                case "1"://unmake favourite
                    doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor
                                    (RequestType.UNMAKE_FAVOURTIE, new String[]{brand_body.getId()}),
                            BrandDetailActivity.class, handler, MSG_IS_NOT_FAVOURITE, 0);
                    break;
                default:
                    break;
            }
        }
    }

    private class OnSharePage implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

}
