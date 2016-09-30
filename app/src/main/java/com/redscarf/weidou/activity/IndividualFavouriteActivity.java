package com.redscarf.weidou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.redscarf.weidou.adapter.FavouriteAdapter;
import com.redscarf.weidou.adapter.MyFavouriteAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.pojo.MyFavouriteBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by yeahwang on 2016/3/28.
 */
public class IndividualFavouriteActivity extends BaseActivity{

    private ListView lv_favourite;
    private LinearLayout layout_info;
    private View view_404;

    private String response;
    private ArrayList<MyFavouriteBody> bodys;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INDEX:
                    Bundle indexObj = msg.getData();
                    response = indexObj.getString("response");
                    if (StringUtils.contains(response,"\"status\":\"error\"")){
                        Toast.makeText(IndividualFavouriteActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                    }else{
                        try {
                            bodys = (ArrayList<MyFavouriteBody>) RedScarfBodyAdapter.fromJSON(response, Class.forName
                                    ("com.redscarf.weidou.pojo.MyFavouriteBody"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (bodys.size() != 0) {
                            lv_favourite.setAdapter(new MyFavouriteAdapter(IndividualFavouriteActivity.this,
                                    bodys));
//                        lv_favourite.setAdapter(new FavouriteAdapter(IndividualFavouriteActivity.this,
//                                bodys));

                        }
                    }
                    hideProgressDialog();
                    break;
                case MSG_ERROR:
                    hideProgressDialog();
                    Bundle errObj = msg.getData();
                    String error = errObj.getString("error");
                    layout_info.setVisibility(View.VISIBLE);
                    view_404 = LayoutInflater.from(IndividualFavouriteActivity.this).inflate(R.layout.view_404, layout_info, true);
                    TextView text_404 = (TextView) view_404.findViewById(R.id.txt_404);
                    view_404.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_info.removeAllViews();
                            layout_info.setVisibility(View.GONE);
                            doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor(RequestType
                                            .MY_FAOURITE, ""), IndividualFavouriteActivity.class, handler, MSG_INDEX,
                                    PROGRESS_NO_CANCELABLE,"index");
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
        this.setContentView(R.layout.activity_individual_favourite);
        initView();
    }

    @Override
    public void initView() {
        setActionBarLayout(getResources().getString(R.string.title_individual_favourite), ActionBarType.WITHBACK);
        lv_favourite = (ListView) findViewById(R.id.list_my_favourite);
        lv_favourite.setOnItemClickListener(new OnMyFavouriteItemClick());
        lv_favourite.setOnItemLongClickListener(new OnMyFavouriteItemLongClick());
        layout_info = (LinearLayout) findViewById(R.id.layout_favourite_info);
        doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor(RequestType
                .MY_FAOURITE, ""), IndividualFavouriteActivity.class, handler, MSG_INDEX,
                PROGRESS_NO_CANCELABLE,"index");
    }

    //点击事件
    private class OnMyFavouriteItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String category = String.valueOf(bodys.get(position).getCategory());
            Bundle data = new Bundle();
            data.putString("id", bodys.get(position).getId());
            data.putString("title", bodys.get(position).getTitle());
            switch (category){
                //购物
                case "4":
                    JumpToActivity(IndividualFavouriteActivity.this, FoodDetailActivity.class,
                            data);
                    break;
                case "5":
                    JumpToActivity(IndividualFavouriteActivity.this, GoodsDetailActivity.class,
                            data);
                    break;
                case "283":
                    JumpToActivity(IndividualFavouriteActivity.this, BrandDetailActivity.class,
                            data);
                    break;

            }
        }
    }

    //长按事件
    private class OnMyFavouriteItemLongClick implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            return false;
        }
    }
}
