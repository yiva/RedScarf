package com.redscarf.weidou.activity.fragment;

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
import com.redscarf.weidou.pojo.GridBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
    private View actionbar_food;
    private PopupWindow popup_selector;
    private HorizontalListView lv_food_select;
    private HorizontalListView lv_select_food_series;
    private HorizontalListView lv_select_food_more;

    private String response;
    private float lastY = 0f;
    private float currentY = 0f;

    private static Integer category_id = 4;
    private static Integer flag = 1;
    private static String title = "全部餐厅";
    private static int CURRENT_PAGE = 1;
    private static String url_attr = "";

    private ArrayList<FoodBody> bodys;
    private List<GridBody> datas;
    private ArrayList<String> list_food_select;

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
                        list_food_series = (ArrayList<FoodSeriesBody>)RedScarfBodyAdapter
                                .fromJSONWithAttr(response,"categories",Class.forName("com" +
                                        ".redscarf.weidou.pojo.FoodSeriesBody"));
                        list_food_more = (ArrayList<FoodTopicBody>)RedScarfBodyAdapter
                                .fromJSONWithAttr(response,"topic_categories", Class.forName("com" +
                                        ".redscarf.weidou.pojo.FoodTopicBody"));
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG,e);
                    } catch (ClassNotFoundException e) {
                        ExceptionUtil.printAndRecord(TAG,e);
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
    public void onResume() {
        super.onResume();
        Log.d(TAG, "resume");
        setActionBarLayout(title, ActionBarType.WITHBACK);
        showProgressDialogNoCancelable("", MyConstants.LOADING);
        doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST, new String[]{category_id.toString(), CURRENT_PAGE + ""}), FoodFragment.class, handler, MSG_INDEX);
        doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOOD_FILTER_LIST, ""), FoodFragment.class, handler, MSG_FOOD_FILTER);
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            try {
//                setActionBarLayout(title, ActionBarType.WITHBACK);
//                if (flag.equals(1)) {
//                    showProgressDialogNoCancelable("", MyConstants.LOADING);
//                    doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST, new String[]{category_id.toString(), CURRENT_PAGE + ""}), FoodFragment.class, handler, MSG_INDEX);
//                }
//            } catch (Exception ex) {
//                ExceptionUtil.printAndRecord(TAG, ex);
//                setActionBarLayout(title, ActionBarType.WITHBACK);
//                showProgressDialogNoCancelable("", MyConstants.LOADING);
//                doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST, new String[]{category_id.toString(), "1"}), FoodFragment.class, handler, MSG_INDEX);
//            }
//        }
//    }

    @Override
    public void initView() {
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
            Toast.makeText(getActivity(), list_food_select.get(position), Toast.LENGTH_SHORT).show();
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
        url_attr = "";
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_food_category, null);
        //重置按钮
        reset = (Button) contentView.findViewById(R.id.btn_food_category_reset);
        //确定按钮
        submit = (Button) contentView.findViewById(R.id.btn_food_category_finish);

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

    }

    /**
     * 初始化顶部控件数据
     *
     * @return
     */
    public List<GridBody> makeFoodHeaderGridArrays() {
        Integer[] colors = {R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple};
        Integer[] photo = {R.drawable.all_canting,
                R.drawable.china_canting,
                R.drawable.sushi,
                R.drawable.noodle,
                R.drawable.hotfood,
                R.drawable.india,
                R.drawable.chicken_leg,
                R.drawable.tea,
                R.drawable.cake,
                R.drawable.forma};
        String[] title = {"全部餐厅", "中餐厅", "日韩餐厅",
                "东南亚餐厅", "西餐厅", "印度餐厅", "中东餐厅",
                "下午茶", "咖啡甜品店", "外卖快餐店"};
        Integer[] postIds = {4, 533, 534, 535, 536, 537, 709, 538, 539, 540};
        List<GridBody> headerBody = new ArrayList<>();
        for (int i = 0; i < postIds.length; ++i) {
            headerBody.add(new GridBody(colors[i], title[i], photo[i], postIds[i]));
        }
        return headerBody;
    }

    /**
     * 菜系
     */
    private class OnFoodSeriesItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FoodSeriesBody body = list_food_series.get(position);
            CURRENT_PAGE = 1;
            category_id = Integer.parseInt(body.getId());
            title = body.getTitle();
            setActionBarLayout(title, ActionBarType.WITHBACK);
            showProgressDialogNoCancelable("", MyConstants.LOADING);
            doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST, new String[]{category_id.toString(), CURRENT_PAGE + ""}), FoodFragment.class, handler, MSG_INDEX);
            popup_selector.dismiss();
        }
    }

    /**
     * 更多
     */
    private class OnFoodMoreItemClick implements OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    private class OnFoodCategoryResetClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            popup_selector.dismiss();
        }
    }

    private class OnFoodCategorySubmitClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            popup_selector.dismiss();
        }
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
