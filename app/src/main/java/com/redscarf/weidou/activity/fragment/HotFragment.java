package com.redscarf.weidou.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.HotAdapter;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.pojo.HotBody;
import com.redscarf.weidou.pojo.HotListBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.JSONHelper;
import com.redscarf.weidou.util.MyConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 发现
 * Created by yeahwang on 2016/8/8.
 */
public class HotFragment extends BaseFragment {

    private final String TAG = HotFragment.class.getSimpleName();

    private RecyclerView recyclerViewHot;
    private View actionbar_hot;
    private View view_404;
    private LinearLayout layout_info;

    private HotAdapter hotAdapter;

    private ArrayList<HotBody> list_hot_item;
    private ArrayList<HotListBody> list_hot = new ArrayList<>();
    private String response;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INDEX:
                    Bundle indexObj = msg.getData();
                    response = indexObj.getString("response");
                    parseHotItems();
                    ArrayList<HotBody> list = new ArrayList<>();
                    HotBody b = new HotBody();
                    b.setTitle("Hello");
                    list.add(b);
                    hotAdapter = new HotAdapter(getActivity(), list_hot);
                    recyclerViewHot.setAdapter(hotAdapter);
                    hotAdapter.setOnRecyclerViewListener(new OnHotItemClick());
                    hideProgressDialog();
                    layout_info.setVisibility(View.GONE);
                    break;
                case MSG_ERROR:
                    hideProgressDialog();
                    Bundle errObj = msg.getData();
                    String error = errObj.getString("error");
                    layout_info.setVisibility(View.VISIBLE);
                    view_404 = LayoutInflater.from(getActivity()).inflate(R.layout.view_404, layout_info, true);
                    TextView text_404 = (TextView) view_404.findViewById(R.id.txt_404);
                    view_404.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doRequestURL(Request.Method.GET, RequestURLFactory.getRequestListURL(RequestType.HOTLIST,
                                    new String[]{""}), HotFragment.class, handler, MSG_INDEX, PROGRESS_NO_CANCELABLE,
                                    "index");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_hot, container,
                false);

        initView();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doRequestURL(Request.Method.GET, RequestURLFactory.getRequestListURL(RequestType.HOTLIST,
                        new String[]{""}), HotFragment.class, handler, MSG_INDEX, PROGRESS_NO_CANCELABLE,
                "index");
    }

    @Override
    public void initView() {
        setActionBarLayout("热门", ActionBarType.NORMAL);
        recyclerViewHot = (RecyclerView) rootView.findViewById(R.id.list_hot);
        recyclerViewHot.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHot.setLayoutManager(layoutManager);
    }

    private boolean parseHotItems() {
        try {
            JSONObject jo = new JSONObject(response);
            String posts = jo.getString("posts");
            JSONObject o = jo.getJSONObject("posts");
            JSONArray ja_hot_category = new JSONArray(MyConstants.STR_HOT_CATEGORY);
            for (int i = 0; i < ja_hot_category.length(); ++i) {
                JSONObject jo_hot_category = ja_hot_category.getJSONObject(i);
                String key = jo_hot_category.getString("key");
                JSONArray array = o.getJSONArray(key);
                ArrayList<HotBody> bodys = (ArrayList<HotBody>) JSONHelper.parseCollection
                        (array, ArrayList.class, HotBody.class);
                HotListBody body = new HotListBody();
                body.setId(key);
                body.setKey(jo_hot_category.getString("value"));
                body.setHotItems(bodys);
                list_hot.add(body);
            }
        } catch (JSONException e) {
            ExceptionUtil.printAndRecord(TAG, e);
            return false;
        }
        return true;
    }

    private class OnHotItemClick implements HotAdapter.OnRecyclerViewListener {

        @Override
        public void onItemClick(int position) {
            Toast.makeText(getActivity(), list_hot.get(position).getKey(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClick(int position) {
            return false;
        }
    }
}
