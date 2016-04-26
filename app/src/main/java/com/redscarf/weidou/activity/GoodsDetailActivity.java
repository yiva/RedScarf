package com.redscarf.weidou.activity;

import java.util.ArrayList;
import java.util.HashMap;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.pojo.DiscountBody;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.util.GlobalApplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private TextView label_sales_code;
    private ImageButton favourite;
    private ImageButton share;
    private Button copy_code;
    private Button buy;
    private Button brand_info;
    private LinearLayout layout_dead_time;

    private DiscountBody body;
    private String response;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            response = indexObj.getString("response");
            switch (msg.what) {
                case MSG_INDEX:
                    ArrayList<DiscountBody> arrRed = null;
                    try {
                        arrRed = RedScarfBodyAdapter.fromJSON(response, DiscountBody.class);
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
        GlobalApplication.getInstance().addActivity(this);

        doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURL(RequestType.DISCOUNT_POST,
                        new String[]{datas.getString("id")}), GoodsDetailActivity.class, handler,
                MSG_INDEX, 1);

    }

    @Override
    public void initView() {
        title = (TextView) findViewById(R.id.txt_goods_detail_title);
        content = (TextView) findViewById(R.id.txt_goods_detail_content);
        subtitle = (TextView) findViewById(R.id.txt_goods_detail_subtitle);
        exclusive = (TextView) findViewById(R.id.txt_exclusive_good_detail);
        expires = (TextView) findViewById(R.id.txt_expires_goods_detail);
        favourite = (ImageButton) findViewById(R.id.actionbar_good_detail_favorite);
        copy_code = (Button) findViewById(R.id.btn_good_detail_sale_code);
        buy = (Button) findViewById(R.id.btn_good_detail_buy_now);
        brand_info = (Button) findViewById(R.id.btn_good_detail_info_more);
        label_sales_code = (TextView) findViewById(R.id.txt_sales_code_goods_detail);
        layout_dead_time = (LinearLayout) findViewById(R.id.layout_dead_time_goods_detail);
        View divider = findViewById(R.id.view_dead_time_goods_detail);

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
        subtitle.setText(body.getSubtitle());
        expires.setText(body.getExpires());
        copy_code.setText("折扣码: "+body.getOthers());

        //无过期时间，隐藏expires
        if (StringUtils.isBlank(body.getExpires()) || StringUtils.contains(body.getExpires(),
                "0000-00-00")){
            layout_dead_time.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }

        //无折扣码
        if (StringUtils.isBlank(body.getOthers())){
            copy_code.setVisibility(View.GONE);
            label_sales_code.setVisibility(View.GONE);
        }

        //button listener

        favourite.setOnClickListener(new OnChangeFavourite());
        buy.setOnClickListener(new OnJumpToPageClick(GoodsDetailActivity.this, body.getTitle(),
                body.getWebsite()));
        brand_info.setOnClickListener(new OnJumpToBrandClick());
        copy_code.setOnClickListener(new OnCopySalesCodeClick());
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

    private class OnJumpToBrandClick implements OnClickListener{

        @Override
        public void onClick(View v) {
            try {
                JSONArray jas = new JSONArray(body.getBrand());
                String brand = jas.getString(0);
                Bundle data = new Bundle();
                data.putString("id", brand);
                Intent i_brand_detail = new Intent(GoodsDetailActivity.this, BrandDetailActivity.class);
                i_brand_detail.putExtras(data);
                startActivity(i_brand_detail);
            } catch (JSONException e) {
                Toast.makeText(GoodsDetailActivity.this,"网络故障！",Toast.LENGTH_SHORT).show();
                ExceptionUtil.printAndRecord(TAG, e);
            }

        }
    }

    /**
     * 复制折扣码
     */
    private class OnCopySalesCodeClick implements OnClickListener{

        @Override
        public void onClick(View v) {
            // Gets a handle to the clipboard service.
            ClipboardManager clipboard = (ClipboardManager)getSystemService(Context
                    .CLIPBOARD_SERVICE);
            // Creates a new text clip to put on the clipboard
            ClipData clip = ClipData.newPlainText("sales_code",body.getOthers());
            // Set the clipboard's primary clip.
            clipboard.setPrimaryClip(clip);

            Toast.makeText(GoodsDetailActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
        }
    }

}
