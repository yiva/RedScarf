package com.redscarf.weidou.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.redscarf.weidou.activity.BasicViewActivity;
import com.redscarf.weidou.activity.FoodDetailActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.activity.SearchDetailActivity;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.adapter.SearchAdapter;
import com.redscarf.weidou.adapter.ViewPageFragmentAdapter;
import com.redscarf.weidou.pojo.SearchBody;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by yeahwang on 2015/12/23.
 */
public class SearchFragment extends BaseViewPagerFragment {

    private final String TAG = SearchFragment.class.getSimpleName();

    private View rootView;
    private ListView lv_search;
    private ImageButton btn_search;

    private String response;
    private final int MSG_INDEX = 1; //msg.what index
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

//        lv_search = (ListView) rootView.findViewById(R.id.list_search);

//        showProgressDialogNoCancelable("", MyConstants.LOADING);
//        doRequestURL(RequestURLFactory.getRequestListURL(RequestType.SEARCHLIST, new String[]{"searches"}), SearchFragment.class, handler, MSG_INDEX);
        initView();

        return rootView;
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = { "热门购物", "热门餐厅", "热门搜索" };
        adapter.addTab(title[0], "shop", ShopSearchFragment.class, null);
        adapter.addTab(title[1], "food", FoodSearchFragment.class, null);
        adapter.addTab(title[2], "hot", HotSearchFragment.class, null);
    }

    @Override
    public void initView() {
        btn_search = (ImageButton) rootView.findViewById(R.id.btnSearch);
        btn_search.setOnClickListener(new OnJumpSearchDetailLinstener());
    }

    private class onSearchItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(), bodys.get(position).getTerms(), Toast.LENGTH_SHORT).show();
        }
    }

    private class OnJumpSearchDetailLinstener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent i_search_info = new Intent(getActivity(),SearchDetailActivity.class);
            startActivity(i_search_info);
        }
    }
}
