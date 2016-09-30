package com.redscarf.weidou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.redscarf.weidou.adapter.BuyListAdapter;
import com.redscarf.weidou.adapter.HotMoreAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.pojo.BrandDetailBody;
import com.redscarf.weidou.pojo.GoodsBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.ExceptionUtil;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * 发现的更多产品界面
 * Created by yeahwang on 2016/8/22.
 */
public class HotMoreActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final String TAG = HotMoreActivity.class.getSimpleName();

    private ListView lv_hot_more;
    private LinearLayout layout_info;
    private View view_404;

    private Bundle datas = new Bundle();
    private static int CURRENT_PAGE = 1;
    private String response;
    private ArrayList<GoodsBody> bodys;

    private BuyListAdapter hotMoretAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            response = indexObj.getString("response");
            switch (msg.what) {
                case MSG_INDEX:
                    try {
                        bodys = (ArrayList<GoodsBody>) RedScarfBodyAdapter.fromJSON(response,
                                Class.forName("com.redscarf.weidou.pojo.GoodsBody"));
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (bodys.size() != 0) {
                        hotMoretAdapter = new BuyListAdapter(HotMoreActivity.this, bodys);
                        lv_hot_more.setAdapter(hotMoretAdapter);
                    }
                    hideProgressDialog();
                    break;
                case MSG_ERROR:
                    hideProgressDialog();
                    Bundle errObj = msg.getData();
                    String error = errObj.getString("error");
                    layout_info.setVisibility(View.VISIBLE);
                    view_404 = LayoutInflater.from(HotMoreActivity.this).inflate(R.layout.view_404, layout_info, true);
                    TextView text_404 = (TextView) view_404.findViewById(R.id.txt_404);
                    view_404.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_info.removeAllViews();
                            layout_info.setVisibility(View.GONE);
                            setActionBarLayout(datas.getString("title"), ActionBarType.WITHBACK);
                            doRequestURL(Request.Method.GET, RequestURLFactory.getRequestListURL(RequestType.HOT_MORE_LIST,
                                    new String[]{datas.getString("id")}), HotMoreActivity.class, handler,
                                    MSG_INDEX, PROGRESS_CANCELABLE, "index");
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
        this.setContentView(R.layout.activity_hot_more);
        initView();
    }

    @Override
    public void initView() {
        datas = getIntent().getExtras();

        lv_hot_more = (ListView) findViewById(R.id.list_hot_more);
        lv_hot_more.setOnItemClickListener(HotMoreActivity.this);
        layout_info = (LinearLayout) findViewById(R.id.layout_hot_more_info);

        setActionBarLayout(datas.getString("title"), ActionBarType.WITHBACK);
        doRequestURL(Request.Method.GET, RequestURLFactory.getRequestListURL(RequestType.HOT_MORE_LIST,
                        new String[]{datas.getString("id")}), HotMoreActivity.class, handler,
                MSG_INDEX, PROGRESS_CANCELABLE, "index");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GoodsBody bodyItem = bodys.get(position);//使用了headview position需加1

        Bundle data = new Bundle();
        data.putString("id", bodyItem.getId());
        data.putString("title", bodyItem.getTitle());

        Intent in_shop_detail = new Intent(HotMoreActivity.this, GoodsDetailActivity.class);
        in_shop_detail.putExtras(data);
        startActivity(in_shop_detail);
    }
}
