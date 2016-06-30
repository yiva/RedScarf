package com.redscarf.weidou.activity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.redscarf.weidou.activity.BrandDetailActivity;
import com.redscarf.weidou.activity.GoodsDetailActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.activity.popupwindow.ShopCategaryPopup;
import com.redscarf.weidou.adapter.BrandDetailAdapter;
import com.redscarf.weidou.adapter.BrandsListAdapter;
import com.redscarf.weidou.adapter.BuyListAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.adapter.ShopGridAdapter;
import com.redscarf.weidou.customwidget.HorizontalListView;
import com.redscarf.weidou.customwidget.pullableview.PullToRefreshLayout;
import com.redscarf.weidou.customwidget.pullableview.PullableListView;
import com.redscarf.weidou.pojo.GoodsBody;
import com.redscarf.weidou.pojo.GridBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.DisplayUtil;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 购物fragment
 *
 * @author yeahwa
 */
public class BuyFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener
        //        implements OnTouchListener
{
    private final String TAG = BuyFragment.class.getSimpleName();
    private static int CURRENT_PAGE = 1;

    private View is_click_head_btn;
    private PullableListView lv_shop;
    private HorizontalListView lv_brands;
    private ListView lv_brand_detail;
    private LinearLayout layout_popup_brand_detail;
    private PopupWindow popup_brand_detail;
    private PopupWindow popup_selector;
    private ImageButton btn_hide_brand_detail;
    private ImageButton selector;
    private GridView grid_shop;
    private Button dismiss;
    private View actionbar_buy;

    private List<GridBody> datas;

    List<TextView> headtabs = new ArrayList<TextView>();
    private String response;
    private final int MSG_INDEX = 1; //msg.what index
    private ArrayList<GoodsBody> bodys;
    private HashMap<String,ArrayList<String>> map_brands;
    private ArrayList<String> listbrands_title;
    private static Integer category_id = 5;
    private static Integer flag = 1;
    private static String title = "全部购物";

    private BuyListAdapter buyListAdapter;
    private BrandsListAdapter brandsListAdapter;

    BackShopCategoryListener mbackClickListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INDEX:
                    Bundle indexObj = msg.getData();
                    response = indexObj.getString("response");
                    try {
                        JSONObject jo = new JSONObject(response);
                        listbrands_title = parseBrands(jo.getString("brands"));
                        bodys = (ArrayList<GoodsBody>) RedScarfBodyAdapter.fromJSON(response, Class.forName("com.redscarf.weidou.pojo.GoodsBody"));
                    } catch (ClassNotFoundException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    }
                    if (listbrands_title.size() != 0) {
                        brandsListAdapter = new BrandsListAdapter(getActivity(), listbrands_title);
                        lv_brands.setAdapter(brandsListAdapter);
                    }
                    if (bodys.size() != 0) {
                        buyListAdapter = new BuyListAdapter(getActivity(), bodys, category_id);
                        lv_shop.setAdapter(buyListAdapter);
                    }
                    hideProgressDialog();
                    break;
                case MSG_NEXT_PAGE:
                    Bundle nextObj = msg.getData();
                    response = nextObj.getString("response");
                    try {
                        JSONObject jo = new JSONObject(response);
                        ArrayList<GoodsBody> items = (ArrayList<GoodsBody>) RedScarfBodyAdapter.fromJSON(response, Class.forName("com.redscarf.weidou.pojo.GoodsBody"));
                        if (items.size() != 0) {
                            bodys.addAll(items);
                            buyListAdapter.notifyDataSetChanged();
//                            lv_shop.setAdapter(new BuyListAdapter(getActivity(), bodys, category_id));
                        }
                    } catch (ClassNotFoundException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    } catch (JSONException e) {
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

        rootView = inflater.inflate(R.layout.fragment_buy, container, false);
        initView();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            setActionBarLayout(title, ActionBarType.WITHBACK);

            if (flag.equals(1)) {
                showProgressDialogNoCancelable("", MyConstants.LOADING);
                doRequestURL(RequestURLFactory.getRequestListURL(RequestType.BUYLIST, new String[]{category_id.toString(), "1"}), BuyFragment.class, handler, MSG_INDEX);
            }
        } catch (Exception ex) {
            ExceptionUtil.printAndRecord(TAG,ex);
            setActionBarLayout(title, ActionBarType.WITHBACK);
            showProgressDialogNoCancelable("", MyConstants.LOADING);
            doRequestURL(RequestURLFactory.getRequestListURL(RequestType.BUYLIST, new String[]{category_id.toString(), "1"}), BuyFragment.class, handler, MSG_INDEX);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            try {
//                flag = getArguments().getInt("flag");
//                category_id = getArguments().getInt("category_id");
//                title = getArguments().getString("title");
                setActionBarLayout(title, ActionBarType.WITHBACK);
                if (flag.equals(1)) {
                    showProgressDialogNoCancelable("", MyConstants.LOADING);
                    doRequestURL(RequestURLFactory.getRequestListURL(RequestType.BUYLIST, new String[]{category_id.toString(), CURRENT_PAGE+""}), BuyFragment.class, handler, MSG_INDEX);
                }
            } catch (Exception ex) {
                ExceptionUtil.printAndRecord(TAG,ex);
                setActionBarLayout(title, ActionBarType.WITHBACK);
                showProgressDialogNoCancelable("", MyConstants.LOADING);
                doRequestURL(RequestURLFactory.getRequestListURL(RequestType.BUYLIST, new String[]{category_id.toString(), CURRENT_PAGE+""}), BuyFragment.class, handler, MSG_INDEX);
            }
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mbackClickListener = (BackShopCategoryListener) context;
//        } catch (ClassCastException ex) {
//            throw new ClassCastException(context.toString()
//                    + "must implement BackShopCategoryFragment");
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
        if (popup_brand_detail != null) {
            popup_brand_detail.dismiss();
        }
        if (popup_selector != null) {
            popup_selector.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {
        ImageButton back = (ImageButton) rootView.findViewById(R.id.actionbar_back);
        back.setVisibility(View.GONE);
//        back.setOnClickListener(new OnbackClick());

        //品牌类别选择
        selector = (ImageButton) rootView.findViewById(R.id.actionbar_selector);
        selector.setVisibility(View.VISIBLE);
        selector.setOnClickListener(new OnSelectDiscountCategaryClick());

        // 定义列表
        lv_shop = (PullableListView) rootView.findViewById(R.id.list_shop);
        lv_shop.setOnItemClickListener(new onListBuyItemClick());
        lv_shop.setLongClickable(true);
        ((PullToRefreshLayout)rootView.findViewById(R.id.refresh_view)).setOnRefreshListener(this);

        lv_brands = (HorizontalListView) rootView.findViewById(R.id.hlist_brand);
        lv_brands.setOnItemClickListener(new onListBrandItemClick());
        actionbar_buy = rootView.findViewById(R.id.actionbar_buy);
        popup_selector = new PopupWindow();
    }

    /**
     * 下拉
     * @param pullToRefreshLayout
     */
    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
        // 下拉刷新操作
        new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                CURRENT_PAGE = 1;
                // 千万别忘了告诉控件刷新完毕了哦！
                doRequestURL(RequestURLFactory.getRequestListURL(RequestType.BUYLIST, new String[]{category_id.toString(), CURRENT_PAGE+""}), BuyFragment.class, handler, MSG_INDEX);
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 5000);
    }

    /**
     * 上拉
     * @param pullToRefreshLayout
     */
    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
        new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                doRequestURL(Request.Method.GET,RequestURLFactory.getRequestListURL(RequestType.BUYLIST, new String[]{category_id.toString(), ++CURRENT_PAGE+""}), BuyFragment.class, handler, MSG_NEXT_PAGE,PROGRESS_DISVISIBLE);
                // 千万别忘了告诉控件加载完毕了哦！
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 5000);
    }

	/**
     * 点击跳转
	 */
    private class onListBuyItemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            GoodsBody bodyItem = bodys.get(position);

            Bundle data = new Bundle();
            data.putString("id", bodyItem.getId());
            data.putString("title", bodyItem.getTitle());

            Intent in_shop_detail = new Intent(getActivity(), GoodsDetailActivity.class);
            in_shop_detail.putExtras(data);
            startActivity(in_shop_detail);
        }

    }

    /**
     * 品牌标签列表点击事件
     */
    private class onListBrandItemClick implements OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String key = listbrands_title.get(position);
            ArrayList<String> list_res = map_brands.get(key);
            showPopupWindow(view,list_res);
        }
    }


    public void setMarginTop(int page) {
        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParam.setMargins(0, page, 0, 0);
        lv_shop.setLayoutParams(layoutParam);
        lv_shop.invalidate();
    }


    /**
     * selector list datas
     *
     * @return selectors
     */
    public ArrayList<String> getDatas() {
        ArrayList<String> selectors = new ArrayList<String>();
        selectors.add("时尚配饰");
        selectors.add("护肤美妆");
        selectors.add("保健塑身");
        selectors.add("食品酒水");
        selectors.add("家居电子");
        selectors.add("餐厅票务");
        selectors.add("酒店交通");
        return selectors;
    }

    /**
     * 设置不可点击
     *
     * @param btn 点击按钮
     */
    private void changeFocusButton(View btn) {
        is_click_head_btn.setClickable(true);
        is_click_head_btn = btn;
        is_click_head_btn.setClickable(false);
    }

    //返回ShopCategoryFragment
    private class OnbackClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            mbackClickListener.backToShopCategory();
        }
    }

    public interface BackShopCategoryListener {
        void backToShopCategory();
    }

    /**
     * 解析品牌标签
     * @param brands
     * @return
     * @throws JSONException
     */
    private ArrayList<String> parseBrands(String brands) throws JSONException {
        JSONArray jas = new JSONArray(brands);
        ArrayList<String> listbrands = new ArrayList<>();
        map_brands = new HashMap<>();
        for (int i = 0; i < jas.length(); ++i) {
            JSONArray sub_jas = jas.getJSONArray(i);
            String key = new JSONObject(sub_jas.get(0).toString()).getString("title").substring(0, 1);
            listbrands.add(key);
            ArrayList<String> list_detail = new ArrayList<>();
            for (int j = 0; j < sub_jas.length(); ++j) {
                JSONObject jo = sub_jas.getJSONObject(j);
                list_detail.add(jo.getString("id") + "#" + jo.getString("title"));
            }
            map_brands.put(key, list_detail);
        }
        return listbrands;
    }

    /**
     * 点击品牌字母弹出框
     * @param view
     * @param brands
     */
    private void showPopupWindow(View view, ArrayList<String> brands) {
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.popup_brand_detail, null);
        lv_brand_detail = (ListView) contentView.findViewById(R.id.list_brand_detail);
        layout_popup_brand_detail = (LinearLayout) contentView.findViewById(R.id.layout_popup_brand_detail);
        lv_brand_detail.setAdapter(new BrandDetailAdapter(getActivity(), brands));
        ViewTreeObserver vto = lv_brand_detail.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                layout_popup_brand_detail.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int shop_height = lv_shop.getHeight();
                int lv_height = layout_popup_brand_detail.getHeight();
                if (shop_height >= lv_height){
                    return ;
                }
                ViewGroup.LayoutParams layoutParams = lv_brand_detail.getLayoutParams();
                layoutParams.width = GlobalApplication.getScreenWidth();
                layoutParams.height = shop_height - DisplayUtil.dip2px(getActivity(),50);
                lv_brand_detail.setLayoutParams(layoutParams);
            }
        });
        ViewGroup.LayoutParams params = lv_brand_detail.getLayoutParams();

        lv_brand_detail.setOnItemClickListener(new OnBrandDetailItemClick(brands));
        popup_brand_detail = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popup_brand_detail.setFocusable(true);
        popup_brand_detail.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且不会影响背景
        popup_brand_detail.setBackgroundDrawable(new BitmapDrawable());
        popup_brand_detail.showAsDropDown(view, 0, 8);
        btn_hide_brand_detail = (ImageButton) contentView.findViewById(R.id.btn_hide_up_brand);
        btn_hide_brand_detail.setOnClickListener(new OnBrandDetailHideClick());
    }

    private void showShopCategaryPopupWindow(){
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_shop_category, null);
        dismiss = (Button) contentView.findViewById(R.id.btn_shop_category_dismiss);
        grid_shop = (GridView) contentView.findViewById(R.id.grid_shop);
        grid_shop.setAdapter(new ShopGridAdapter(getActivity(), datas = this.makeShopHeaderGridArrays()));
        grid_shop.setOnItemClickListener(new OnShopItemClick());
        int h = GlobalApplication.getScreenHeight(getActivity());
        int w = GlobalApplication.getScreenWidth(getActivity());
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
        popup_selector.showAtLocation(actionbar_buy, Gravity.CENTER,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dismiss.setOnClickListener(new OnShopCategoryDismissClick());
    }

    /**
     * 初始化顶部控件数据
     *
     * @return
     */
    public List<GridBody> makeShopHeaderGridArrays() {
        Integer[] colors = {R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple,
                R.color.weidou_purple
        };
        Integer[] photo = {R.drawable.basket,
                R.drawable.purse,
                R.drawable.lipstick,
                R.drawable.gym,
                R.drawable.wine_glass,
                R.drawable.tv,
                R.drawable.ticket,
                R.drawable.car
        };
        String[] title = {"全部购物", "时尚配饰", "护肤美妆",
                "保健塑身", "食品酒水", "家居电子",
                "餐厅票务", "酒店交通"};
        Integer[] postIds = {5, 481, 480, 478, 483, 479, 484, 482};
        List<GridBody> headerBody = new ArrayList<>();
        for (int i = 0; i < postIds.length; ++i) {
            headerBody.add(new GridBody(colors[i], title[i], photo[i],postIds[i]));
        }
        return headerBody;
    }

    /**
     * 品牌列表Item点击事件
     */
    private class OnBrandDetailItemClick implements OnItemClickListener{

        private ArrayList<String> brandDetail;
        public OnBrandDetailItemClick(ArrayList<String> details){
            this.brandDetail = details;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String brand_id = brandDetail.get(position).split("#")[0];
            Bundle data = new Bundle();
            data.putString("id", brand_id);
            Intent i_brand_detail = new Intent(getActivity(), BrandDetailActivity.class);
            i_brand_detail.putExtras(data);
            startActivity(i_brand_detail);
        }
    }

    private class OnBrandDetailHideClick implements OnClickListener{

        @Override
        public void onClick(View v) {
            popup_brand_detail.dismiss();
        }
    }

    /**
     * 品牌类别点击事件
     */
    private class OnSelectDiscountCategaryClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!popup_selector.isShowing()) {
                // 以下拉方式显示popupwindow
                showShopCategaryPopupWindow();
            } else {
                popup_selector.dismiss();
            }
        }
    }


    @Subscribe
    public void onEventMainThread(GoodsBody event) {

        String msg = "onEventMainThread收到了消息：" + event.getTitle();
        Log.d("harvic", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private class OnShopItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            GridBody body = datas.get(position);
            CURRENT_PAGE = 1;
            category_id = body.getPostId();
            title = body.getTitle();
            setActionBarLayout(title, ActionBarType.WITHBACK);
            showProgressDialogNoCancelable("", MyConstants.LOADING);
            doRequestURL(RequestURLFactory.getRequestListURL(RequestType.BUYLIST, new String[]{category_id.toString(), CURRENT_PAGE+""}), BuyFragment.class, handler, MSG_INDEX);
            popup_selector.dismiss();
        }
    }

    private class OnShopCategoryDismissClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            popup_selector.dismiss();
        }
    }

}
