package com.redscarf.weidou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.pojo.BrandBody;
import com.redscarf.weidou.pojo.BrandDetailBody;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.ExceptionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

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

    private Bundle datas;
    private final int MSG_INDEX = 1; //msg.what goods
    private String response;
    private BrandDetailBody brand_body;
    protected ImageLoader imageLoader;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_INDEX) {
                Bundle indexObj = msg.getData();
                response = indexObj.getString("response");
                ArrayList<BrandDetailBody> arrRed = new ArrayList<>();
                try {
                    arrRed = RedScarfBodyAdapter.fromJSON(response, BrandDetailBody.class);
                    brand_body = arrRed.get(0);
                } catch (JSONException e) {
                    ExceptionUtil.printAndRecord(TAG, e);
                }
                initView(brand_body);
                hideProgressDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_brand_detail);
        datas = this.getIntent().getExtras();
        this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
        doRequestURL(RequestURLFactory.getRequestURL(RequestType.BRAND_POST,
                new String[]{datas.getString("id")}), BrandDetailActivity.class, handler, MSG_INDEX);
    }

    private void initView(BrandDetailBody body) {
        //register
        title = (TextView) findViewById(R.id.txt_brand_detail_title);
        label = (TextView) findViewById(R.id.txt_brand_detail_label);
        description = (TextView) findViewById(R.id.txt_brand_detail_description);
        photo = (NetworkImageView) findViewById(R.id.img_brand_detail_photo);
        website = (Button) findViewById(R.id.btn_brand_detail_website);
        service = (Button) findViewById(R.id.btn_brand_detail_service);
        postinfo = (Button) findViewById(R.id.btn_brand_detail_post_info);

        //put data
        title.setText(body.getTitle());
        label.setText(body.getTitle());
        description.setText(body.getSubtype().substring(1, body.getSubtype().length() - 1));

        //load image
        String imageUrl = body.getPost_medium();
        photo.setBackgroundResource(R.drawable.loading_large);
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            photo.setDefaultImageResId(R.drawable.loading_large);
            photo.setErrorImageResId(R.drawable.null_large);
            photo.setBackgroundColor(0);
            photo.setImageUrl(imageUrl, imageLoader);
        }

        //set listener
        website.setOnClickListener(new OnJumpToPageClick(BrandDetailActivity.this, body.getTitle(), body.getWebsite()));
        service.setOnClickListener(new OnJumpToPageClick(BrandDetailActivity.this, body.getTitle(),body.getCustomer_service()));
        postinfo.setOnClickListener(new OnJumpToPageClick(BrandDetailActivity.this, body.getTitle(),body.getDeliver_info()));
    }
}
