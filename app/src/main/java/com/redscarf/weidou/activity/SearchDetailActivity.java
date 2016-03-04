package com.redscarf.weidou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.redscarf.weidou.adapter.BrandsListAdapter;
import com.redscarf.weidou.adapter.BuyListAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.adapter.SearchDetailAdapter;
import com.redscarf.weidou.customwidget.ClearEditText;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.pojo.GoodsBody;
import com.redscarf.weidou.pojo.SearchDetailBody;
import com.redscarf.weidou.util.ExceptionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 搜索页面
 */
public class SearchDetailActivity extends BaseActivity {

    private final String TAG = SearchDetailActivity.class.getSimpleName();

    private ClearEditText search_content;
    private ArrayList<SearchDetailBody> arrRed;
    private ListView lv_index;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            String response = indexObj.getString("response");
            if (msg.what == MSG_INDEX) {
                try {
                    arrRed = RedScarfBodyAdapter
                            .fromJSON(response, SearchDetailBody.class);
                    if (arrRed.size() != 0){
                        lv_index.setAdapter(new SearchDetailAdapter(SearchDetailActivity.this,
                                arrRed));
                    }
                } catch (JSONException e) {
                    ExceptionUtil.printAndRecord(TAG, e);
                }
                hideProgressDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);
        initView();
    }

    private void initView() {
        search_content = (ClearEditText) findViewById(R.id.edit_search_detail);

        search_content.setOnEditorActionListener(new OnSearchSubmitListener());

        lv_index = (ListView) findViewById(R.id.list_search_detail);
//        lv_index.setOnItemClickListener(new onListIndexItemClick());
//        lv_index.setOnTouchListener(this);
        lv_index.setLongClickable(true);
    }


    private class OnSearchSubmitListener implements TextView.OnEditorActionListener{


        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                String content = String.valueOf(search_content.getText().toString().trim());
                if (!content.equals("")){
                    submitSearch(content);
                }
            }
            return false;
        }
    }

    private boolean submitSearch(String content){
        doRequestURL(Request.Method.GET, RequestURLFactory.getRequestListURL(RequestType
                .SEARCHLIST,new String[]{content}),SearchDetailActivity.class,handler,MSG_INDEX,
                PROGRESS_NO_CANCLE);
        return true;
    }
}
