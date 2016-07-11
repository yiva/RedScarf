package com.redscarf.weidou.activity.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Request;
import com.redscarf.weidou.activity.FoodDetailActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.FoodListAdapter;
import com.redscarf.weidou.adapter.FoodMoreAdapter;
import com.redscarf.weidou.adapter.FoodSelectListAdapter;
import com.redscarf.weidou.adapter.FoodSeriesAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.adapter.ShopGridAdapter;
import com.redscarf.weidou.customwidget.HorizontalListView;
import com.redscarf.weidou.customwidget.pullableview.PullToRefreshLayout;
import com.redscarf.weidou.customwidget.pullableview.PullableListView;
import com.redscarf.weidou.listener.PullRefreshListener;
import com.redscarf.weidou.pojo.FoodBody;
import com.redscarf.weidou.pojo.FoodSeriesBody;
import com.redscarf.weidou.pojo.FoodTopicBody;
import com.redscarf.weidou.pojo.FoodUrlAttribute;
import com.redscarf.weidou.pojo.GridBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.LocationUtil;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class FoodFragment extends BaseFragment implements OnTouchListener, PullToRefreshLayout.OnRefreshListener {


    private final String TAG = FoodFragment.class.getSimpleName();

    private PullableListView lv_food;
    private ImageButton selector;
    private Button reset;
    private Button submit;
    private Button update_time;
    private Button price;
    private Button distance;
    private View actionbar_food;
    private PopupWindow popup_selector;
    private HorizontalListView lv_food_select;
    private HorizontalListView lv_select_food_series;
    private HorizontalListView lv_select_food_more;
    private Button[] costs = new Button[4];

    private String response;
    private float lastY = 0f;
    private float currentY = 0f;

    private static Integer category_id = 4;
    private static Integer flag = 1;
    private static String title = "全部餐厅";
    private static int CURRENT_PAGE = 1;
    private FoodUrlAttribute foodUrlAttribute;

    private ArrayList<FoodBody> bodys;
    private List<GridBody> datas;
    private ArrayList<String> list_food_select;
    private Location location;

    private ArrayList<FoodSeriesBody> list_food_series;
    private ArrayList<FoodTopicBody> list_food_more;

    private FoodListAdapter foodListAdapter;
    private FoodSelectListAdapter foodSelectListAdapter;
    private FoodSeriesAdapter foodSeriesAdapter;
    private FoodMoreAdapter foodMoreAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NEXT_PAGE:
                    Bundle foodObj = msg.getData();
                    response = foodObj.getString("response");
                    try {
                        ArrayList<FoodBody> items = (ArrayList<FoodBody>) RedScarfBodyAdapter.fromJSON(response, Class.forName("com.redscarf.weidou.pojo.FoodBody"));
                        if (bodys.size() != 0) {
                            bodys.addAll(items);
                            foodListAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    }
                    break;
                case MSG_INDEX:
                    Bundle indexObj = msg.getData();
                    response = indexObj.getString("response");
                    try {
                        bodys = (ArrayList<FoodBody>) RedScarfBodyAdapter.fromJSON(response, Class.forName("com.redscarf.weidou.pojo.FoodBody"));
                        JSONObject jo = new JSONObject(response);
                        list_food_select = parseFoodSelect(jo.getString("titles"));
                    } catch (Exception e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    }
                    if (bodys.size() != 0) {
                        foodListAdapter = new FoodListAdapter(getActivity(), bodys);
                        lv_food.setAdapter(foodListAdapter);
                    }
                    if (list_food_select.size() != 0) {
                        foodSelectListAdapter = new FoodSelectListAdapter(getActivity(), list_food_select);
                        lv_food_select.setAdapter(foodSelectListAdapter);
                    }
                    hideProgressDialog();
                    break;
                case MSG_FOOD_FILTER:
                    Bundle foodSeriesObj = msg.getData();
                    response = foodSeriesObj.getString("response");
                    try {
                        list_food_series = (ArrayList<FoodSeriesBody>) RedScarfBodyAdapter
                                .fromJSONWithAttr(response, "categories", Class.forName("com" +
                                        ".redscarf.weidou.pojo.FoodSeriesBody"));
                        list_food_more = (ArrayList<FoodTopicBody>) RedScarfBodyAdapter
                                .fromJSONWithAttr(response, "topic_categories", Class.forName("com" +
                                        ".redscarf.weidou.pojo.FoodTopicBody"));
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    } catch (ClassNotFoundException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
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

        rootView = inflater.inflate(R.layout.fragment_food, container,
                false);

        initView();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mfoodBackListener = (BackFoodCategoryListener) context;
//        } catch (ClassCastException ex) {
//            throw new ClassCastException(context.toString()
//                    + "must implement BackShopCategoryFragment");
//        }
//    }


    @Override
    public void onStart() {
        super.onStart();
        Location tmp_location = null;
        if (null != (tmp_location = LocationUtil.getLocation(getActivity()))) {
            location = tmp_location;
        }
        if (location != null) {
            foodUrlAttribute.setLatitude(location.getLatitude()+"");
            foodUrlAttribute.setLongitude(location.getLongitude()+"");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "resume");
        setActionBarLayout(title, ActionBarType.WITHBACK);
        showProgressDialogNoCancelable("", MyConstants.LOADING);
        doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST, new
                String[]{foodUrlAttribute.toString(),
                CURRENT_PAGE + ""}), FoodFragment.class, handler, MSG_INDEX);
        doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOOD_FILTER_LIST, ""), FoodFragment.class, handler, MSG_FOOD_FILTER);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (popup_selector != null) {
            popup_selector.dismiss();
        }
    }

    @Override
    public void initView() {
        location = LocationUtil.getLocation(getActivity());
        foodUrlAttribute = new FoodUrlAttribute();
        if (location != null) {
            foodUrlAttribute.setLatitude(location.getLatitude()+"");
            foodUrlAttribute.setLongitude(location.getLongitude()+"");
        }
        ImageButton back = (ImageButton) rootView.findViewById(R.id.actionbar_back);
        back.setVisibility(View.GONE);

        //品牌类别选择
        selector = (ImageButton) rootView.findViewById(R.id.actionbar_selector);
        selector.setVisibility(View.VISIBLE);
        selector.setOnClickListener(new OnSelectFoodCategaryClick());
//        back.setOnClickListener(new OnBackClick());
        lv_food = (PullableListView) rootView.findViewById(R.id.list_food);
        lv_food.setOnItemClickListener(new onListFoodItemClick());
        lv_food.setOnTouchListener(this);
        lv_food.setLongClickable(true);
        ((PullToRefreshLayout) rootView.findViewById(R.id.food_refresh_view)).setOnRefreshListener(this);

        lv_food_select = (HorizontalListView) rootView.findViewById(R.id.list_food_select);
        lv_food_select.setOnItemClickListener(new OnListFoodSelectItemClick());

        actionbar_food = rootView.findViewById(R.id.actionbar_food);
        popup_selector = new PopupWindow();
    }

    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
        // 下拉刷新操作
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                CURRENT_PAGE = 1;
                // 千万别忘了告诉控件刷新完毕了哦！
                doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST, new String[]{category_id.toString(), CURRENT_PAGE + ""}), FoodFragment.class, handler, MSG_INDEX);
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 5000);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                doRequestURL(Request.Method.GET, RequestURLFactory.getRequestListURL(RequestType.FOODLIST, new String[]{category_id.toString(), ++CURRENT_PAGE + ""}), FoodFragment.class, handler, MSG_NEXT_PAGE, PROGRESS_DISVISIBLE);
                // 千万别忘了告诉控件加载完毕了哦！
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 5000);
    }

//    private class OnBackClick implements OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            mfoodBackListener.backToFoodCategory();
//        }
//    }


    /**
     * 点击跳转
     */
    private class onListFoodItemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
//            FoodBody bodyItem = bodys.get(position - 1);
            FoodBody bodyItem = bodys.get(position);
            Bundle data = new Bundle();
            data.putString("id", bodyItem.getId());
            data.putString("title", bodyItem.getTitle());
            Intent in_food_detail = new Intent(getActivity(), FoodDetailActivity.class);
            in_food_detail.putExtras(data);
            startActivity(in_food_detail);
        }

    }

    private class OnListFoodSelectItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String key = list_food_select.get(position);
            if ("*".equals(key)) {
                foodUrlAttribute.setFisrt_key("");
            } else {
                foodUrlAttribute.setFisrt_key(key);
            }
            CURRENT_PAGE = 1;
            doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST_WITH_FILTER, new
                            String[]{foodUrlAttribute.toString(), CURRENT_PAGE + ""}), FoodFragment.class, handler,
                    MSG_INDEX);
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
//                        //如果与上次方向不同，则执行显/隐动画
//                        if (currentDirection < 0) {
//                            getActivity().findViewById(R.id.actionbar_food).setVisibility(View.GONE);
//                        } else {
//                            getActivity().findViewById(R.id.actionbar_food).setVisibility(View.VISIBLE);
//                        }
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

    public void setMarginTop(int page) {
        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParam.setMargins(0, page, 0, 0);
        lv_food.setLayoutParams(layoutParam);
        lv_food.invalidate();
    }

//    public interface BackFoodCategoryListener {
//        void backToFoodCategory();
//    }

    private void showFoodCategaryPopupWindow() {
        foodUrlAttribute.clear();
        if (location != null) {
            foodUrlAttribute.setLatitude(location.getLatitude()+"");
            foodUrlAttribute.setLongitude(location.getLongitude()+"");
        }
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_food_category, null);
        //重置按钮
        reset = (Button) contentView.findViewById(R.id.btn_food_category_reset);
        //确定按钮
        submit = (Button) contentView.findViewById(R.id.btn_food_category_finish);
        update_time = (Button) contentView.findViewById(R.id.food_select_update_time);
        price = (Button) contentView.findViewById(R.id.food_select_price);
        distance = (Button) contentView.findViewById(R.id.food_select_distance);

        costs[0] = (Button) contentView.findViewById(R.id.food_price_1);
        costs[1] = (Button) contentView.findViewById(R.id.food_price_2);
        costs[2] = (Button) contentView.findViewById(R.id.food_price_3);
        costs[3] = (Button) contentView.findViewById(R.id.food_price_4);

        //菜系
        lv_select_food_series = (HorizontalListView) contentView.findViewById(R.id.list_select_food_series);
        if (list_food_series.size() != 0) {
            foodSeriesAdapter = new FoodSeriesAdapter(getActivity(), list_food_series);
            lv_select_food_series.setAdapter(foodSeriesAdapter);
        }

        //更多
        lv_select_food_more = (HorizontalListView) contentView.findViewById(R.id.list_select_food_more);
        if (list_food_more.size() != 0) {
            foodMoreAdapter = new FoodMoreAdapter(getActivity(), list_food_more);
            lv_select_food_more.setAdapter(foodMoreAdapter);
        }

        // 设置SelectPicPopupWindow的View
        popup_selector.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        popup_selector.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        popup_selector.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        popup_selector.setFocusable(true);
        popup_selector.setOutsideTouchable(true);
        // 刷新状态
        popup_selector.update();
//      // 这个是为了点击“返回Back”也能使其消失，并且不会影响背景
        popup_selector.setBackgroundDrawable(new BitmapDrawable());
//        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        popup_selector.setAnimationStyle(R.style.AnimationPreview);
        popup_selector.showAtLocation(actionbar_food, Gravity.CENTER, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        reset.setOnClickListener(new OnFoodCategoryResetClick());
        submit.setOnClickListener(new OnFoodCategorySubmitClick());
        update_time.setOnClickListener(new OnFoodSortFilterClick());
        price.setOnClickListener(new OnFoodSortFilterClick());
        distance.setOnClickListener(new OnFoodSortFilterClick());
        lv_select_food_series.setOnItemClickListener(new OnFoodSeriesItemClick());
        lv_select_food_more.setOnItemClickListener(new OnFoodMoreItemClick());
        for (Button btn : costs) {
            btn.setOnClickListener(new OnFoodCostclick());
        }


    }


    /**
     * 菜系(主分类)
     */
    private class OnFoodSeriesItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FoodSeriesBody body = list_food_series.get(position);
            foodUrlAttribute.setMain_category(body.getId());
            foodUrlAttribute.setMain_category_flag(1);

            foodSeriesAdapter.setSelectedPosition(position);

            foodSeriesAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 更多（主题topic）
     */
    private class OnFoodMoreItemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FoodTopicBody body = list_food_more.get(position);
            foodUrlAttribute.setTopic(body.getId());
            foodUrlAttribute.setTopic_flag(1);

            foodMoreAdapter.setSelectedPosition(position);
            foodMoreAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 重置
     */
    private class OnFoodCategoryResetClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            foodUrlAttribute.clear();
            popup_selector.dismiss();
        }
    }

    /**
     * 提交
     */
    private class OnFoodCategorySubmitClick implements OnClickListener {
        @Override
        public void onClick(View v) {

            popup_selector.dismiss();
            doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST_WITH_FILTER, new
                            String[]{foodUrlAttribute.toString(), CURRENT_PAGE + ""}), FoodFragment.class, handler,
                    MSG_INDEX);
        }
    }

    /**
     * 按钮切换状态
     *
     * @param v
     * @param flag
     * @param field
     * @return
     * @throws IllegalAccessException
     */
    private int switchButtonStatus(Button v, int flag, Field field) throws IllegalAccessException {
        String content = v.getText().toString().trim();
        field.setAccessible(true);
        if (0 != (++flag) % 3) {
            v.setSelected(true);
            if (1 == flag) {
                field.set(foodUrlAttribute, "DESC");
                v.setText(content + " +");
            } else if (2 == flag) {
                field.set(foodUrlAttribute, "ASC");
                v.setText(StringUtils.substringBefore(content, " ") + " -");
            }
        } else {
            flag = 0;
            field.set(foodUrlAttribute, "");
            v.setSelected(false);
            v.setText(StringUtils.substringBefore(content, " "));
        }
        return flag;
    }

    private class OnFoodSortFilterClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.food_select_update_time:
                        int update_flag = foodUrlAttribute.getUpdate_time_flag();

                        foodUrlAttribute.setUpdate_time_flag(switchButtonStatus(update_time,
                                update_flag, FoodUrlAttribute.class.getDeclaredField("update_time")));
                        break;
                    case R.id.food_select_price:
                        int cost_flag = foodUrlAttribute.getCost_flag();
                        foodUrlAttribute.setCost_flag(switchButtonStatus(price, cost_flag,
                                FoodUrlAttribute.class.getDeclaredField("price")));
                        break;
                    case R.id.food_select_distance:
                        int distance_flag = foodUrlAttribute.getDistance_flag();
                        foodUrlAttribute.setDistance_flag(switchButtonStatus(distance,
                                distance_flag, FoodUrlAttribute.class.getDeclaredField("distance")));
                        break;
                    default:
                        break;

                }
            } catch (NoSuchFieldException e) {
                ExceptionUtil.printAndRecord(TAG, e);
            } catch (IllegalAccessException e) {
                ExceptionUtil.printAndRecord(TAG, e);
            }
        }
    }

    /**
     * cost选项
     */
    private class OnFoodCostclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.food_price_1:
                    changeCostState(costs[0]);
                    break;
                case R.id.food_price_2:
                    changeCostState(costs[1]);
                    break;
                case R.id.food_price_3:
                    changeCostState(costs[2]);
                    break;
                case R.id.food_price_4:
                    changeCostState(costs[3]);
                    break;
            }
        }
    }

    private void changeCostState(Button cost) {
        foodUrlAttribute.setCost_flag(1);
        foodUrlAttribute.setCost(cost.getText().toString());
        for (Button btn : costs) {
            btn.setSelected(false);
        }
        cost.setSelected(true);
    }

    /**
     * 品牌类别点击事件
     */
    private class OnSelectFoodCategaryClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!popup_selector.isShowing()) {
                // 以下拉方式显示popupwindow
                showFoodCategaryPopupWindow();
            } else {
                popup_selector.dismiss();
            }
        }
    }

    /**
     * 解析美食标签
     *
     * @param foods
     * @return
     * @throws JSONException
     */
    private ArrayList<String> parseFoodSelect(String foods) throws JSONException {
        JSONArray jas = new JSONArray(foods);
        ArrayList<String> listfoods = new ArrayList<>();
        for (int i = 0; i < jas.length(); ++i) {
            String key = jas.getString(i);
            listfoods.add(key);
        }
        return listfoods;
    }

}
