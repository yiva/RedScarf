package com.redscarf.weidou.activity.fragment;

import java.util.ArrayList;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.redscarf.weidou.activity.LoginActivity;
import com.redscarf.weidou.activity.MapActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.activity.WebActivity;
import com.redscarf.weidou.adapter.FoodDetailPhotoAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.customwidget.HorizontalListView;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.pojo.FoodDetailBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.util.LocationUtil;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FoodDetailFragment extends BaseFragment {

    private final String TAG = FoodDetailFragment.class.getSimpleName();

    private Bundle datas;
    protected ImageLoader imageLoader;

    private ImageButton favourite;
    private RelativeLayout layout_photo_big;
    private NetworkImageView img_photo_big;
    private LinearLayout layout;
    private LinearLayout layout_reservation_food_detail;
    private LinearLayout layout_michelin;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private TextView distance;

    private FoodDetailBody body;
    private ArrayList<String> photoAddr;
    private String response;

    OnShowMapListener mlistener;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            response = indexObj.getString("response");
            switch (msg.what) {
                case MSG_INDEX:

                    try {
                        ArrayList<FoodDetailBody> arrRed = RedScarfBodyAdapter
                                .fromJSON(response, FoodDetailBody.class);
                        body = arrRed.get(0);
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    }
                    mlistener.sendLcation(body);
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
                            Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                        Toast.makeText(getActivity(), "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MSG_IS_NOT_FAVOURITE:
                    try {
                        JSONObject jo = new JSONObject(response);
                        if ("true".equals(jo.getString("result")) || "true" == jo.getString
                                ("result")) {
                            body.setIs_favorate("0");
                            favourite.setBackgroundResource(R.drawable.ic_favourite_white);
                            Toast.makeText(getActivity(), "取消收藏", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                        Toast.makeText(getActivity(), "取消收藏失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_food_detail, container, false);

        this.imageLoader = new ImageLoader(VolleyUtil.getRequestQueue(), new BitmapCache());
        //get datas {key:post_id,title:category}
        datas = getActivity().getIntent().getExtras();
        if (StringUtils.isBlank(MyPreferences.getAppPerenceAttribute("latitude")) || StringUtils
                .isBlank(MyPreferences.getAppPerenceAttribute("longitude"))) {
            Location location = LocationUtil.getLocation(getActivity());
            if (null != location) {
                MyPreferences.setAppPerenceAttribute("latitude", location.getLatitude() + "");
                MyPreferences.setAppPerenceAttribute("longitude", location.getLongitude() + "");
                doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURL(RequestType.FOOD_POST,
                                new String[]{datas.getString("id") + "&lat=" + MyPreferences
                                        .getAppPerenceAttribute("latitude") + "&lng=" + MyPreferences
                                        .getAppPerenceAttribute("longitude")}),
                        FoodDetailFragment.class,
                        handler,
                        MSG_INDEX, PROGRESS_NO_CANCELABLE,"index");
            } else {
                doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURL(RequestType.FOOD_POST,
                                new String[]{datas.getString("id")}), FoodDetailFragment.class, handler,
                        MSG_INDEX, PROGRESS_NO_CANCELABLE,"index");
            }
        } else {
            doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURL(RequestType.FOOD_POST,
                            new String[]{datas.getString("id") + "&lat=" + MyPreferences
                                    .getAppPerenceAttribute("latitude") + "&lng=" + MyPreferences
                                    .getAppPerenceAttribute("longitude")}),
                    FoodDetailFragment.class,
                    handler,
                    MSG_INDEX, PROGRESS_NO_CANCELABLE,"index");
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mlistener = (OnShowMapListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("must implement "
                    + OnShowMapListener.class.getSimpleName());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

    }

    public interface OnShowMapListener {
        void sendLcation(FoodDetailBody food);
    }

    private boolean denoteFoodPhotos() {
        photoAddr = new ArrayList<>();
        try {
            JSONArray jsa = new JSONArray(body.getPost_thumbnail());
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jo = jsa.getJSONObject(i);
                String[] addr = String.valueOf(jo.getString("picture")).split(",");
                for (int j = 0; j < addr.length; j++) {
                    photoAddr.add(MyConstants.URL + "wp-content/uploads" + addr[j]);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     * 界面初始化
     */
    @Override
    public void initView() {
        TextView title_text = (TextView) getActivity().findViewById(R.id.actionbar_with_share_title);
        TextView phone = (TextView) rootView.findViewById(R.id.txt_food_detail_phone);
        TextView website = (TextView) rootView.findViewById(R.id.txt_food_detail_website);
        HorizontalListView detail_photos = (HorizontalListView) rootView.findViewById(R.id.hlist_food_detail_img);
        TextView address = (TextView) rootView.findViewById(R.id.txt_food_detail_address);
        TextView underground = (TextView) rootView.findViewById(R.id.txt_food_detail_underground);
        TextView cost = (TextView) rootView.findViewById(R.id.txt_food_detail_cost);
        TextView subtype = (TextView) rootView.findViewById(R.id.txt_food_detail_subtype);
        TextView content = (TextView) rootView.findViewById(R.id.txt_food_detail_content);
        TextView view_menu = (TextView) rootView.findViewById(R.id.txt_food_detail_view_menu);
        favourite = (ImageButton) getActivity().findViewById(R.id.actionbar_with_share_favorite);
        layout_photo_big = (RelativeLayout) getActivity().findViewById(R.id.layout_photo_big_food);
        img_photo_big = (NetworkImageView) getActivity().findViewById(R.id.img_photo_big_food);
        layout_reservation_food_detail = (LinearLayout) rootView.findViewById(R.id.layout_reservation_food_detail);
        layout_michelin = (LinearLayout) rootView.findViewById(R.id.layout_michelin_food_detail);
        star1 = (ImageView) rootView.findViewById(R.id.ico_michelin_food_detail_1);
        star2 = (ImageView) rootView.findViewById(R.id.ico_michelin_food_detail_2);
        star3 = (ImageView) rootView.findViewById(R.id.ico_michelin_food_detail_3);
        distance = (TextView) rootView.findViewById(R.id.txt_distance_food_detail);

        title_text.setText(body.getTitle());
        this.denoteFoodPhotos();
        detail_photos.setAdapter(new FoodDetailPhotoAdapter(getActivity(), photoAddr));
        phone.setText(body.getPhone());
        website.setText("访问网站");
        address.setText(body.getSubtitle().replace("/n", " "));
        underground.setText(body.getUnderground());
        cost.setText(body.getCost());
        subtype.setText(body.getSubtype());
        content.setText(body.getContent());
        view_menu.setText("查看菜单");
        if (StringUtils.isNotBlank(body.getDistance())) {
            distance.setText(body.getDistance() + "mi");
        }
        if (body.getIs_favorate().equals("1") || body.getIs_favorate() == "1") {
            favourite.setBackgroundResource(R.drawable.ic_favourite_red);
        }

        //Call Phone
        phone.setOnClickListener(new onCallPhone());

        if (StringUtils.isNumeric(body.getPost_michelin())
                && Integer.parseInt(body.getPost_michelin()) != 0) {
            layout_michelin.setVisibility(View.VISIBLE);
            switch (Integer.parseInt(body.getPost_michelin())) {
                case 3:
                    star3.setVisibility(View.VISIBLE);
                case 2:
                    star2.setVisibility(View.VISIBLE);
                case 1:
                    star1.setVisibility(View.VISIBLE);
                    break;
                default:
                    layout_michelin.setVisibility(View.GONE);
                    break;
            }
        }


        //Jump WebActivity
        website.setOnClickListener(new OnJumpToBrowerClick(body.getWebsite()));
        view_menu.setOnClickListener(new OnJumpToBrowerClick(body.getMenu()));
        favourite.setOnClickListener(new OnChangeFavourite());
        detail_photos.setOnItemClickListener(new OnDisplayBigImagesClick());
        layout_photo_big.setOnClickListener(new OnHidePhotoCanvasClick());
        if ("1".equals(body.getReservation())) {
            layout_reservation_food_detail.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 打电话事件
     */
    private class onCallPhone implements OnClickListener {

        @Override
        public void onClick(View v) {
            TextView phonenum = (TextView) rootView.findViewById(R.id.txt_food_detail_phone);
            String num = (String) phonenum.getText();
            if (null == num && "".equals(num.trim())) {
                Toast.makeText(getActivity(), "无电话号码", Toast.LENGTH_SHORT).show();
            } else {
                Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
                call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(call);
            }
        }
    }

    /**
     * 收藏功能
     */
    private class OnChangeFavourite implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isLogin()) {
                Intent in_login = new Intent(getActivity(), LoginActivity.class);
                startActivity(in_login);
            }
            switch (body.getIs_favorate()) {
                case "0"://make favourite
                    doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor(RequestType.MAKE_FAVOURITE,
                                    new String[]{body.getId()}), FoodDetailFragment.class, handler,
                            MSG_IS_FAVOURITE, PROGRESS_DISVISIBLE,"favourite");
                    break;
                case "1"://unmake favourite
                    doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor
                                    (RequestType.UNMAKE_FAVOURTIE, new String[]{body.getId()}),
                            FoodDetailFragment.class, handler, MSG_IS_NOT_FAVOURITE,
                            PROGRESS_DISVISIBLE,"unfavourite");
                    break;
                default:
                    break;
            }
        }
    }

    private class OnDisplayBigImagesClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            layout_photo_big.setVisibility(View.VISIBLE);
            String imageUrl = photoAddr.get(position);
            if (StringUtils.contains(imageUrl, "-150x150")) {
                imageUrl = imageUrl.replace("-150x150", "");
            }
            img_photo_big.setBackgroundResource(R.drawable.loading_large);
            if ((imageUrl != null) && (!imageUrl.equals(""))) {
                img_photo_big.setDefaultImageResId(R.drawable.loading_large);
                img_photo_big.setErrorImageResId(R.drawable.null_large);
                img_photo_big.setBackgroundColor(0);
                img_photo_big.setImageUrl(imageUrl, imageLoader);
            }
        }
    }

    private class OnHidePhotoCanvasClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            layout_photo_big.setVisibility(View.GONE);
        }
    }
}
