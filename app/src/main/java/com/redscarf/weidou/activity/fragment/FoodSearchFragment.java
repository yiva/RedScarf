package com.redscarf.weidou.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.redscarf.weidou.activity.FoodDetailActivity;
import com.redscarf.weidou.activity.GoodsDetailActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.FoodSearchAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.adapter.SearchAdapter;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.pojo.HotFoodBody;
import com.redscarf.weidou.pojo.SearchBody;
import com.redscarf.weidou.util.MyConstants;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by yeahwang on 2016/2/29.
 */
public class FoodSearchFragment extends BaseFragment{

    private ListView lv_search;
    private String response;
    private ArrayList<HotFoodBody> bodys;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_INDEX) {
                Bundle indexObj = msg.getData();
                response = indexObj.getString("response");
                try {
                    bodys = (ArrayList<HotFoodBody>) RedScarfBodyAdapter.fromJSON(response, Class.forName("com.redscarf.weidou.pojo.HotFoodBody"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (bodys.size() != 0) {
                    lv_search.setAdapter(new FoodSearchAdapter(getActivity(), bodys));

                }
                hideProgressDialog();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_food, container, false);
        initView();
        return rootView;
    }

    @Override
    public void initView() {
        lv_search = (ListView) rootView.findViewById(R.id.list_search_food);
        lv_search.setOnItemClickListener(new OnFoodSearchItemClick());
        showProgressDialogNoCancelable("", MyConstants.LOADING);
        doRequestURL(RequestURLFactory.getRequestListURL(RequestType.HOTSEARCHLIST, new String[]{"foodposts"}), SearchFragment.class, handler, MSG_INDEX);
    }

    private class OnFoodSearchItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle datas = new Bundle();
            datas.putString("id",bodys.get(position).getId());
            datas.putString("title",bodys.get(position).getTitle());
            Intent i_food = new Intent(getActivity(),FoodDetailActivity
                    .class);
            i_food.putExtras(datas);
            startActivity(i_food);
        }
    }
}
