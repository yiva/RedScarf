package com.redscarf.weidou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.adapter.AttachmentAdapter;
import com.redscarf.weidou.adapter.BaseRedScarfAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.customwidget.ScrollListView;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.pojo.AttachmentBody;
import com.redscarf.weidou.pojo.BrandDetailBody;
import com.redscarf.weidou.pojo.GoodsBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.DisplayUtil;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.JSONHelper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
    private ListView listview_attachment;
    private LinearLayout layout_info;
    private View view_404;

    private Bundle datas;
    //msg.what goods

    private String response;
    private BrandDetailBody brand_body;
    protected ImageLoader imageLoader;
    private AttachmentAdapter attachmentAdapter;

    private ArrayList<AttachmentBody> attachmentBodies;

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
                    setCurrentContentView();
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
                case MSG_ERROR:
                    hideProgressDialog();
                    Bundle errObj = msg.getData();
                    String error = errObj.getString("error");
                    layout_info.setVisibility(View.VISIBLE);
                    view_404 = LayoutInflater.from(BrandDetailActivity.this).inflate(R.layout.view_404, layout_info, true);
                    TextView text_404 = (TextView) view_404.findViewById(R.id.txt_404);
                    view_404.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_info.removeAllViews();
                            layout_info.setVisibility(View.GONE);
                            doRequestURL(Request.Method.GET,RequestURLFactory.getRequestURL(RequestType.BRAND_POST,
                                    new String[]{datas.getString("id")}), BrandDetailActivity.class, handler,
                                    MSG_INDEX,PROGRESS_NO_CANCELABLE,"index");
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_brand_detail);
        datas = this.getIntent().getExtras();
        this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
        GlobalApplication.getInstance().addActivity(this);
        initView();
        doRequestURL(Request.Method.GET,RequestURLFactory.getRequestURL(RequestType.BRAND_POST,
                new String[]{datas.getString("id")}), BrandDetailActivity.class, handler,
                MSG_INDEX,PROGRESS_NO_CANCELABLE,"index");
    }

    @Override
    public void initView() {
        //register
        description = (TextView) findViewById(R.id.txt_brand_detail_description);
        photo = (NetworkImageView) findViewById(R.id.img_brand_detail_photo);
        website = (Button) findViewById(R.id.btn_brand_detail_website);
        service = (Button) findViewById(R.id.btn_brand_detail_service);
        postinfo = (Button) findViewById(R.id.btn_brand_detail_post_info);
        favourite = (ImageButton) findViewById(R.id.actionbar_with_share_favorite);
        share = (ImageButton) findViewById(R.id.actionbar_with_share_share);
        listview_attachment = (ListView) findViewById(R.id.list_attachments_brand_detail);
        label = (TextView) findViewById(R.id.txt_label_brand_detail);
        layout_info = (LinearLayout) findViewById(R.id.layout_brand_detail);

    }

    private void setCurrentContentView() {
        setActionBarLayout(brand_body.getTitle(), ActionBarType.WITHSHARE);
        label.setText(brand_body.getTitle());
        description.setText(String.valueOf(StringUtils.substringBetween(brand_body.getSubtype(), "\"")));
        description.setVisibility(View.VISIBLE);

        //load image
        String imageUrl = null;
        try {
            imageUrl = brand_body.getPost_medium();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        photo.setBackgroundResource(R.drawable.loading_large);
        if ((imageUrl != null) && (!imageUrl.equals(""))) {
            photo.setDefaultImageResId(R.drawable.loading_large);
            photo.setErrorImageResId(R.drawable.loading_large);
            photo.setBackgroundColor(0);
            photo.setImageUrl(imageUrl, imageLoader);
        }

        //favourite button
        if (brand_body.getIs_favorate().equals("1") || brand_body.getIs_favorate() == "1") {
            favourite.setBackgroundResource(R.drawable.ic_favourite_red);
        }

        //set listener
        website.setOnClickListener(new OnJumpToBrowerClick(brand_body.getWebsite()));
        service.setOnClickListener(new OnJumpToBrowerClick(brand_body.getCustomer_service()));
        postinfo.setOnClickListener(new OnJumpToBrowerClick(brand_body.getDeliver_info()));
        share.setOnClickListener(new OnSharePage());
        favourite.setOnClickListener(new OnChangeFavourite());
        try {
            attachmentBodies = this.formatJsonToAttachments();
            if (null != attachmentBodies && 0 != attachmentBodies.size()) {
                attachmentAdapter = new AttachmentAdapter(BrandDetailActivity.this, attachmentBodies);
                listview_attachment.setAdapter(attachmentAdapter);
                //暂时这样搞，没法
                ScrollListView.getTotalHeightofListView(listview_attachment,
                        DisplayUtil.setHeightByRatio(GlobalApplication.getScreenWidth(), 3, 2));
                listview_attachment.setOnItemClickListener(new OnAttachmentItemClick());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //不让listview作为首项显示在屏幕中
        ScrollView sv = (ScrollView) findViewById(R.id.scroll_brand_detail);
        sv.smoothScrollTo(0, 0);
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
                            MSG_IS_FAVOURITE, PROGRESS_DISVISIBLE,"favourite");
                    break;
                case "1"://unmake favourite
                    doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor
                                    (RequestType.UNMAKE_FAVOURTIE, new String[]{brand_body.getId()}),
                            BrandDetailActivity.class, handler, MSG_IS_NOT_FAVOURITE,
                            PROGRESS_DISVISIBLE,"unfavourite");
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

    /**
     * 格式化品牌下的折扣信息
     *
     * @return
     */
    private ArrayList<AttachmentBody> formatJsonToAttachments() throws JSONException {
        String attachments = brand_body.getAttachments();
        return (ArrayList<AttachmentBody>) JSONHelper.parseCollection(attachments, ArrayList
                .class, AttachmentBody.class);
    }

    private class OnAttachmentItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AttachmentBody bodyItem = attachmentBodies.get(position);

            Bundle data = new Bundle();
            data.putString("id", bodyItem.getId());

            Intent in_shop_detail = new Intent(BrandDetailActivity.this, GoodsDetailActivity.class);
            in_shop_detail.putExtras(data);
            startActivity(in_shop_detail);
        }
    }
}
