package com.redscarf.weidou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.redscarf.weidou.adapter.MyFavouriteAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.pojo.MyFavouriteBody;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by yeahwang on 2016/3/28.
 */
public class IndividualFavouriteActivity extends BaseActivity{

    private ListView lv_favourite;

    private String response;
    private ArrayList<MyFavouriteBody> bodys;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_INDEX) {
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

                    }
                }
                hideProgressDialog();
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
        lv_favourite = (ListView) findViewById(R.id.list_my_favourite);
        lv_favourite.setOnItemClickListener(new OnMyFavouriteItemClick());
        doRequestURL(Request.Method.GET, RequestURLFactory.getRequestURLWithAuthor(RequestType
                .MY_FAOURITE, ""), IndividualInfoActivity.class, handler, MSG_INDEX, PROGRESS_NO_CANCLE);
    }

    private class OnMyFavouriteItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String category = String.valueOf(bodys.get(position).getCategory());
            switch (category){
                //购物
                case "1":
                case "283":
                    JumpToActivity(IndividualFavouriteActivity.this, BrandDetailActivity.class, null);
                    break;

            }
        }
    }
}