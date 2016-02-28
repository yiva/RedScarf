package com.redscarf.weidou.activity.fragment;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.redscarf.weidou.activity.MapActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.activity.SendReviewActivity;
import com.redscarf.weidou.activity.UserReviewActivity;
import com.redscarf.weidou.activity.WebActivity;
import com.redscarf.weidou.adapter.FoodDetailPhotoAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.customwidget.HorizontalListView;
import com.redscarf.weidou.pojo.FoodDetailBody;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.util.MyConstants;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FoodDetailFragment extends BaseFragment {

    private final String TAG = FoodDetailFragment.class.getSimpleName();

    private Bundle datas;
    private final int MSG_INDEX = 2; //msg.what foods

    private FoodDetailBody body;
    private View rootView;
    private ArrayList<String> photoAddr;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_INDEX) {
                Bundle indexObj = msg.getData();
                try {
                    ArrayList<FoodDetailBody> arrRed = RedScarfBodyAdapter
                            .fromJSON(indexObj.getString("response"), FoodDetailBody.class);
                    body = arrRed.get(0);
                } catch (JSONException e) {
                    ExceptionUtil.printAndRecord(TAG, e);
                }
                initView();
                hideProgressDialog();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_food_detail, container, false);

        //get datas {key:post_id,title:category}
        datas = getActivity().getIntent().getExtras();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        //请求url参数
        doRequestURL(RequestURLFactory.getRequestURL(RequestType.FOOD_POST,
                new String[]{datas.getString("key")}), FoodDetailFragment.class, handler, MSG_INDEX);
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

        TextView title_text = (TextView) rootView.findViewById(R.id.txt_food_detail_title);
        TextView phone = (TextView) rootView.findViewById(R.id.txt_food_detail_phone);
        TextView website = (TextView) rootView.findViewById(R.id.txt_food_detail_website);
        ImageView img_call_phone = (ImageView) rootView.findViewById(R.id.btn_food_detail_phone_next);
        HorizontalListView detail_photos = (HorizontalListView) rootView.findViewById(R.id.hlist_food_detail_img);


        this.denoteFoodPhotos();
        detail_photos.setAdapter(new FoodDetailPhotoAdapter(getActivity(), photoAddr));
        title_text.setText(body.getTitle());
        phone.setText(body.getPhone());
        website.setText(body.getWebsite());
        //Call Phone
        img_call_phone.setOnClickListener(new onCallPhone());
        //Jump UserReviewActivity
//		rootView.findViewById(R.id.btn_food_detail_review_more).setOnClickListener(new onReviewMoreLinstener());

        ////Jump MapActivity
        rootView.findViewById(R.id.imbtn_maps).setOnClickListener(new onImgMapsLinstener());

        //Jump WebActivity
        rootView.findViewById(R.id.btn_food_detail_website_next).setOnClickListener(new OnJumpWeb());

//        getActivity().findViewById(R.id.btn_bottom_food_detail_review).setOnClickListener(new onSendReview());
    }


    /*
     * Jump UserReviewActivity
     */
    private class onReviewMoreLinstener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), UserReviewActivity.class);
            startActivity(intent);
        }

    }

    /*
     * Jump MapActivity
     */
    private class onImgMapsLinstener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Bundle map_data = new Bundle();
//			map_data.putString("location", body.getLocation());
            Intent mapIntent = new Intent(getActivity(), MapActivity.class);
            mapIntent.putExtras(map_data);
            startActivity(mapIntent);
        }

    }

    /**
     * 访问页面
     */
    private class OnJumpWeb implements OnClickListener {

        @Override
        public void onClick(View v) {
            Bundle datas = new Bundle();
            datas.putString("url", body.getWebsite());
            Intent i_web = new Intent(getActivity(), WebActivity.class);
            i_web.putExtras(datas);
            startActivity(i_web);
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
                Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
                startActivity(call);
            }
        }
    }

    private class onSendReview implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i_review = new Intent(getActivity(), SendReviewActivity.class);
            Bundle data = new Bundle();
//			data.putInt("key", body.getId());
            i_review.putExtras(data);
            startActivity(i_review);
        }
    }

}
