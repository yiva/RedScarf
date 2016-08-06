package com.redscarf.weidou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.redscarf.weidou.adapter.BrandsListAdapter;
import com.redscarf.weidou.adapter.BuyListAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.adapter.SearchDetailAdapter;
import com.redscarf.weidou.adapter.SearchDiscountAdapter;
import com.redscarf.weidou.adapter.SearchFoodAdapter;
import com.redscarf.weidou.customwidget.ClearEditText;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.pojo.BrandBody;
import com.redscarf.weidou.pojo.DiscountBody;
import com.redscarf.weidou.pojo.FoodBody;
import com.redscarf.weidou.pojo.GoodsBody;
import com.redscarf.weidou.pojo.SearchDetailBody;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.JSONHelper;
import com.redscarf.weidou.util.MyConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * 搜索页面
 */
public class SearchDetailActivity extends BaseActivity
        implements View.OnTouchListener {

    private final String TAG = SearchDetailActivity.class.getSimpleName();

    private ClearEditText search_content;
    private ArrayList<SearchDetailBody> arrRed;
    private ArrayList<DiscountBody> arrDiscount;
    private ArrayList<FoodBody> arrFood;
    private ArrayList<BrandBody> arrBrand;
    private ListView lv_index;

    private SearchDiscountAdapter searchDiscountAdapter;
    private SearchFoodAdapter searchFoodAdapter;

    private Bundle datas;
    private float lastY = 0f;
    private float currentY = 0f;
    private String category;
    private static int CURRENT_PAGE = 1;
    private String search_category = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            String response = indexObj.getString("response");
            if (msg.what == MSG_INDEX) {
                try {
                    JSONObject o = new JSONObject(response);
                    if (!o.isNull("posts")) {
                        JSONObject jo = o.getJSONObject("posts");
                        JSONArray names = jo.names();
                        for (int i = 0; i < names.length(); ++i) {
                            String name = names.getString(i);
                            JSONArray ja = jo.getJSONArray(name);
                            if (0 != ja.length()) {
                                search_category = name;
                                switch (name) {
                                    case "brand":
                                        arrBrand = (ArrayList<BrandBody>) JSONHelper
                                                .parseCollection(ja, ArrayList
                                                        .class, BrandBody.class);
                                        break;
                                    case "discount":
                                        arrDiscount = (ArrayList<DiscountBody>) JSONHelper
                                                .parseCollection(ja, ArrayList
                                                        .class, DiscountBody.class);
                                        searchDiscountAdapter = new SearchDiscountAdapter
                                                (SearchDetailActivity.this, arrDiscount);
                                        lv_index.setAdapter(searchDiscountAdapter);
                                        break;
                                    case "food":
                                        arrFood = (ArrayList<FoodBody>) JSONHelper
                                                .parseCollection(ja, ArrayList
                                                        .class, FoodBody.class);
                                        searchFoodAdapter = new SearchFoodAdapter
                                                (SearchDetailActivity.this, arrFood);
                                        lv_index.setAdapter(searchFoodAdapter);
                                        break;
                                    case "others":
                                        break;
                                    default:
                                        search_category = "";
                                        break;
                                }
                                break;
                            }
                        }
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        datas = this.getIntent().getExtras();
        GlobalApplication.getInstance().addActivity(this);
        initView();
    }

    @Override
    public void initView() {
        search_content = (ClearEditText) findViewById(R.id.edit_search_detail);

        search_content.setOnEditorActionListener(new OnSearchSubmitListener());
        lv_index = (ListView) findViewById(R.id.list_search_detail);
        lv_index.setOnItemClickListener(new OnSearchDetailItemClick());
        lv_index.setOnTouchListener(this);
        lv_index.setLongClickable(true);
        try {
            if (datas != null && datas.containsKey("content")) {
                category = datas.getString("content");
                switch (category) {
                    case "5":
                        search_content.setHint("购物");
                        break;
                    case "4":
                        search_content.setHint("美食");
                        break;
                    default:
                        break;
                }
//                search_content.setText(datas.getString("content"));
//                search_content.setInputType(InputType.TYPE_NULL);
//                doRequestURL(Request.Method.GET, RequestURLFactory.getRequestListURL(RequestType
//                                .SEARCHLIST, new String[]{datas.getString("content")}),
//                        SearchDetailActivity.class, handler, MSG_INDEX,
//                        PROGRESS_NO_CANCLE);
            }
        } catch (Exception ex) {
            ExceptionUtil.printAndRecord(TAG, ex);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //下面两个表示滑动的方向，大于0表示向下滑动，小于0表示向上滑动，等于0表示未滑动
        int lastDirection = 0;
        int currentDirection = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                currentY = event.getY();
                currentDirection = 0;
                lastDirection = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                //只有在listView.getFirstVisiblePosition()>0的时候才判断是否进行显隐动画。因为listView.getFirstVisiblePosition()==0时，
                //ToolBar——也就是头部元素必须是可见的，如果这时候隐藏了起来，那么占位置用了headerview就被用户发现了
                //但是当用户将列表向下拉露出列表的headerview的时候，应该要让头尾元素再次出现才对——这个判断写在了后面onScrollListener里面……
                float tmpCurrentY = event.getY();
                if (Math.abs(tmpCurrentY - lastY) > MyConstants.FLING_MIN_DISTANCE) {//滑动距离大于touchslop时才进行判断
                    currentY = tmpCurrentY;
                    currentDirection = (int) (currentY - lastY);
                    if (lastDirection != currentDirection) {
                        //关闭软键盘
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(search_content.getWindowToken(), 0);
                    }
                    lastY = currentY;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //手指抬起的时候要把currentDirection设置为0，这样下次不管向哪拉，都与当前的不同（其实在ACTION_DOWN里写了之后这里就用不着了……）
                currentDirection = 0;
                lastDirection = 0;
                break;
        }
        return false;
    }


    private class OnSearchSubmitListener implements TextView.OnEditorActionListener {


        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String content = String.valueOf(search_content.getText().toString().trim());
                if (!content.equals("")) {
                    submitSearch(content);
                }
            }
            return false;
        }
    }

    private boolean submitSearch(String content) {
        CURRENT_PAGE = 1;
        doRequestURL(Request.Method.GET, RequestURLFactory.getRequestListURL(RequestType
                        .SEARCHLIST, new String[]{content, category, CURRENT_PAGE + ""}),
                SearchDetailActivity
                        .class,
                handler, MSG_INDEX,
                PROGRESS_NO_CANCELABLE,"search");
        return true;
    }

    private class OnSearchDetailItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle datas = new Bundle();

            switch (search_category) {
                //餐厅
                case "food":
                    datas.putString("id", arrFood.get(position).getId());
                    Intent i_food = new Intent(SearchDetailActivity.this, FoodDetailActivity.class);
                    i_food.putExtras(datas);
                    startActivity(i_food);
                    break;
                //购物
                case "discount":
                    datas.putString("id", arrDiscount.get(position).getId());
                    Intent i_discount = new Intent(SearchDetailActivity.this, GoodsDetailActivity
                            .class);
                    i_discount.putExtras(datas);
                    startActivity(i_discount);
                    break;
                //品牌
                case "brand":
                    datas.putString("id", arrBrand.get(position).getId());
                    Intent i_brand = new Intent(SearchDetailActivity.this, BrandDetailActivity.class);
                    i_brand.putExtras(datas);
                    startActivity(i_brand);
                    break;
            }
        }
    }

}
