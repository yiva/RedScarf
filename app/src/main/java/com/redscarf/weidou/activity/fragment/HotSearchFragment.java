package com.redscarf.weidou.activity.fragment;

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

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.adapter.SearchAdapter;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.pojo.SearchBody;
import com.redscarf.weidou.util.MyConstants;

import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by yeahwang on 2016/2/29.
 */
public class HotSearchFragment extends BaseFragment{

    private ListView lv_search;
    private String response;
    private ArrayList<SearchBody> bodys;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_INDEX) {
                Bundle indexObj = msg.getData();
                response = indexObj.getString("response");
                try {
                    bodys = (ArrayList<SearchBody>) RedScarfBodyAdapter.fromJSON(response, Class.forName("com.redscarf.weidou.pojo.SearchBody"));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (bodys.size() != 0) {
                    lv_search.setAdapter(new SearchAdapter(getActivity(), bodys));

                }
                hideProgressDialog();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_hot, container, false);
        initView();
        return rootView;
    }

    @Override
    public void initView() {
        lv_search = (ListView) rootView.findViewById(R.id.list_search_hot);
        lv_search.setOnItemClickListener(new OnHotSearchItemClick());
        showProgressDialogNoCancelable("", MyConstants.LOADING);
        doRequestURL(RequestURLFactory.getRequestListURL(RequestType.HOTSEARCHLIST, new String[]{"searches"}), SearchFragment.class, handler, MSG_INDEX);
    }

    private class OnHotSearchItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(), "Hot Search", Toast.LENGTH_SHORT).show();
        }
    }
}
