package com.redscarf.weidou.activity;

import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.pojo.BrandBody;
import com.redscarf.weidou.pojo.BrandDetailBody;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsDetailActivity extends BaseActivity {

    private final String TAG = GoodsDetailActivity.class.getSimpleName();

    private Bundle datas;
    protected ImageLoader imageLoader;
    private TextView actionbar_title;
    private TextView title;
    private TextView content;
    private TextView subtitle;
    private TextView exclusive;
    private TextView expires;
    private ImageButton favourite;
    private ImageButton share;

    private BrandBody body;
    private String response;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            response = indexObj.getString("response");
            switch (msg.what) {
                case MSG_INDEX:
                    ArrayList<BrandBody> arrRed = null;
                    try {
                        arrRed = RedScarfBodyAdapter.fromJSON(response, BrandBody.class);
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    }
                    body = arrRed.get(0);
                    initView();
                    hideProgressDialog();
                    break;
                case MSG_IS_FAVOURITE:
                    try {
                        JSONObject jo = new JSONObject(response);
                        if ("true".equals(jo.getString("result")) || "true" == jo.getString
                                ("result")) {
                            body.setIs_favorate("1");
                            favourite.setBackgroundResource(R.drawable.ic_favourite_red);
                            Toast.makeText(GoodsDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                        Toast.makeText(GoodsDetailActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MSG_IS_NOT_FAVOURITE:
                    try {
                        JSONObject jo = new JSONObject(response);
                        if ("true".equals(jo.getString("result")) || "true" == jo.getString
                                ("result")) {
                            body.setIs_favorate("0");
                            favourite.setBackgroundResource(R.drawable.ic_favourite_white);
                            Toast.makeText(GoodsDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                        Toast.makeText(GoodsDetailActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_goods_detail);

        this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
        //get datas
        datas = this.getIntent().getExtras();

        actionbar_title = (TextView) findViewById(R.id.title_good_detail);

        actionbar_title.setText(String.valueOf(datas.getString("title")));

        //请求参数
        url_map = new HashMap<String, String>();
        url_map.put("json", "get_brandpost");
        url_map.put("post_id", datas.getString("key"));
        doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURL(RequestType.BRAND_POST,
                new String[]{datas.getString("key")}), GoodsDetailActivity.class, handler,
                MSG_INDEX, 1);

        goodsListener();
    }

    private void goodsListener() {
        Button buy_submit = (Button) findViewById(R.id.btn_buy_submit_goods_detail);

        buy_submit.setOnClickListener(new onBuySubmitClick());
    }

    private void initView() {
        title = (TextView) findViewById(R.id.txt_goods_detail_title);
        content = (TextView) findViewById(R.id.txt_goods_detail_business_content);
        subtitle = (TextView) findViewById(R.id.txt_goods_detail_subtitle);
        exclusive = (TextView) findViewById(R.id.txt_exclusive_good_detail);
        expires = (TextView) findViewById(R.id.txt_expires_goods_detail);
        favourite = (ImageButton) findViewById(R.id.actionbar_food_detail_favorite);

        title.setText(body.getTitle());
        if (body.getIs_favorate().equals("1") || body.getIs_favorate() == "1") {
            favourite.setBackgroundResource(R.drawable.ic_favourite_red);
        }
        NetworkImageView goodsImage = (NetworkImageView) findViewById(R.id.goods_detail_image);
//		BaseRedScarfAdapter.formatRedScarfBody(body);
        String imageUrl = body.getPost_medium();
        goodsImage.setBackgroundResource(R.drawable.loading_large);
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            goodsImage.setDefaultImageResId(R.drawable.loading_large);
            goodsImage.setErrorImageResId(R.drawable.null_large);
            goodsImage.setBackgroundColor(0);
            goodsImage.setImageUrl(imageUrl, imageLoader);
        }
        content.setText(Html.fromHtml(body.getContent()));
        subtitle.setText(body.getPhone());
        //favourite button

        favourite.setOnClickListener(new OnChangeFavourite());
    }

    private class onBuySubmitClick implements OnClickListener {

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

    /**
     * 收藏功能
     */
    private class OnChangeFavourite implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (body.getIs_favorate()) {
                case "0"://make favourite
                    doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor(RequestType.MAKE_FAVOURITE,
                                    new String[]{body.getId()}), BrandDetailActivity.class, handler,
                            MSG_IS_FAVOURITE, 0);
                    break;
                case "1"://unmake favourite
                    doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor
                                    (RequestType.UNMAKE_FAVOURTIE, new String[]{body.getId()}),
                            BrandDetailActivity.class, handler, MSG_IS_NOT_FAVOURITE, 0);
                    break;
                default:
                    break;
            }
        }
    }

}
