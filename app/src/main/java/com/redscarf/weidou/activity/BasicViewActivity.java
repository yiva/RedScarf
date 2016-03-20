package com.redscarf.weidou.activity;

import java.util.HashMap;
import java.util.List;

import com.redscarf.weidou.activity.fragment.BuyFragment;
import com.redscarf.weidou.activity.fragment.FoodCategoryFragment;
import com.redscarf.weidou.activity.fragment.FoodFragment;
import com.redscarf.weidou.activity.fragment.SearchFragment;
import com.redscarf.weidou.activity.fragment.ShopCategoryFragment;
import com.redscarf.weidou.util.GlobalApplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 主界面
 *
 * @author yeahwang
 */
public class BasicViewActivity extends BaseActivity implements OnTouchListener,
        ShopCategoryFragment.OnChangeShopListFragmentListener,
        BuyFragment.BackShopCategoryListener,
        FoodCategoryFragment.OnChangeFoodListFragmentListener,
        FoodFragment.BackFoodCategoryListener{

    //	private static final String INDEX_CONTAINER = "IndexFragment";
    private static final String FOOD_CONTAINER = "FoodCategoryFragment";
    private static final String SHOP_CONTAINER = "ShopCategoryFragment";
    private static final String SEARCH_COMTAINER = "SearchFragment";
    private static final String SHOP_LIST_CONTAINER = "BuyFragment";
    private static final String FOOD_LIST_CONTAINER = "FoodFragment";

    private static String SHOP_EXCAHNGE_TAG = SHOP_CONTAINER;
    private static String FOOD_EXCAHNGE_TAG = FOOD_CONTAINER;

    private PopupWindow sharePopupWindow;
    private RadioGroup bottom_tab;
    private Button btn_mine;

    HashMap<String, Fragment> mapFragment = new HashMap<String, Fragment>();
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        this.setContentView(R.layout.activity_basic);
        GlobalApplication.getInstance().addActivity(this);
        initView();
    }

    @Override
    public void initView(){
        btn_mine = (Button) findViewById(R.id.btn_bottom_mine);
        btn_mine.setOnClickListener(new OnJumpMineActivityClick());
        bottom_tab = (RadioGroup) findViewById(R.id.bottomtabs);

        transaction = basicFragment.beginTransaction();

        mapFragment.put(SEARCH_COMTAINER, new SearchFragment());
        mapFragment.put(FOOD_CONTAINER, new FoodCategoryFragment());
        mapFragment.put(SHOP_CONTAINER, new ShopCategoryFragment());
        mapFragment.put(SHOP_LIST_CONTAINER, new BuyFragment());
        mapFragment.put(FOOD_LIST_CONTAINER, new FoodFragment());

        transaction.add(R.id.basicfragment, mapFragment.get(SHOP_EXCAHNGE_TAG), SHOP_EXCAHNGE_TAG);
        transaction.commit();
        onSelectFragment();

//		showSharePopupWindow();
    }


    /*
     * 底部分享弹窗
     */
    private void showSharePopupWindow() {
        View v = getLayoutInflater().inflate(R.layout.popup_index_share, null);
        sharePopupWindow = new PopupWindow(v,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        sharePopupWindow.setOutsideTouchable(true);

        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePopupWindow.dismiss();

            }
        });

        v.findViewById(R.id.btn_share_cancel_index).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        sharePopupWindow.dismiss();
                    }
                });
    }


    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return super.onGenericMotionEvent(event);
    }


    private void hideFragments(){
        List<Fragment> frs = basicFragment.getFragments();
        transaction = basicFragment.beginTransaction();
        for (Fragment fr : frs) {
            transaction.hide(fr);
        }
        transaction.commit();
    }



//	@Override
//	public void onSearch(View v) {
//		Toast.makeText(null, "search", Toast.LENGTH_SHORT).show();
//		Log.d("search btn", "not do it!");
//	}

    public void onAdvHide(View v) {
        switch (v.getId()) {
            case R.id.btn_adv_esc:
                View view = findViewById(R.id.adv_frame);
                view.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public void onToMineActivity(View v) {
        switch (v.getId()) {
            case R.id.btn_bottom_mine:
                Intent in_mine = new Intent(this, MineActivity.class);
                startActivity(in_mine);
                break;
        }
    }


    /*
     *
     * (non-Javadoc)
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitApp();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    /*
     * 底部四个按钮的点击操作
     */
    public void onSelectFragment() {
        bottom_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.btn_bottom_search:
                        hideFragments();
                        transaction = basicFragment.beginTransaction();
                        if (mapFragment.get(SEARCH_COMTAINER).isAdded()) {
                            transaction.show(mapFragment.get(SEARCH_COMTAINER)).commit();
                        }else{
                            transaction.add(R.id.basicfragment, mapFragment.get(SEARCH_COMTAINER),SEARCH_COMTAINER).commit();
                        }
                        break;
                    case R.id.btn_bottom_food:
                        hideFragments();
                        transaction = basicFragment.beginTransaction();
                        if (mapFragment.get(FOOD_EXCAHNGE_TAG).isAdded()) {
                            transaction.show(mapFragment.get(FOOD_EXCAHNGE_TAG)).commit();
                        }else{
                            transaction.add(R.id.basicfragment, mapFragment.get(FOOD_EXCAHNGE_TAG),FOOD_EXCAHNGE_TAG).commit();
                        }
                        break;
                    case R.id.btn_bottom_buy:
                        hideFragments();
                        transaction = basicFragment.beginTransaction();
                        if (mapFragment.get(SHOP_EXCAHNGE_TAG).isAdded()){
                            transaction.show(mapFragment.get(SHOP_EXCAHNGE_TAG)).commit();
                        }else{
                            transaction.add(R.id.basicfragment, mapFragment.get(SHOP_EXCAHNGE_TAG),SHOP_EXCAHNGE_TAG).commit();
                        }
                        break;
                    default:
                        break;
                }

            }
        });


    }

    @Override
    public void OnShopCategoryClickItem(int postid) {
        SHOP_EXCAHNGE_TAG = SHOP_LIST_CONTAINER;
        this.hideFragments();
        FragmentTransaction changeTransaction = basicFragment.beginTransaction();
        BuyFragment buy = (BuyFragment) mapFragment.get(SHOP_EXCAHNGE_TAG);
        if (buy.isAdded()) {
            Bundle args = buy.getArguments();
            args.putInt("flag", 0);
            if (postid != args.getInt("category_id")){
                args.putInt("flag", 1);
                args.putInt("category_id", postid);
            }
            changeTransaction.show(buy);
            buy.onResume();
        }else {
            Bundle args = new Bundle();
            args.putInt("flag", 1);
            args.putInt("category_id", postid);
            buy.setArguments(args);
            changeTransaction.add(R.id.basicfragment, buy, SHOP_EXCAHNGE_TAG);
        }
        changeTransaction.commit();
    }

    @Override
    public void backToShopCategory() {
        BuyFragment buy = (BuyFragment) basicFragment.findFragmentByTag(SHOP_LIST_CONTAINER);
        ShopCategoryFragment shopcategory = (ShopCategoryFragment) basicFragment.findFragmentByTag(SHOP_CONTAINER);
        SHOP_EXCAHNGE_TAG = SHOP_CONTAINER;
        this.hideFragments();
        FragmentTransaction backTransaction = basicFragment.beginTransaction();
        backTransaction.show(mapFragment.get(SHOP_EXCAHNGE_TAG));
        backTransaction.commit();


    }

    @Override
    public void foodCategoryClick(int postid) {
        FOOD_EXCAHNGE_TAG = FOOD_LIST_CONTAINER;
        this.hideFragments();
        FragmentTransaction changeTransaction = basicFragment.beginTransaction();
        FoodFragment item = (FoodFragment) mapFragment.get(FOOD_EXCAHNGE_TAG);
        if (item.isAdded()) {
            Bundle args = item.getArguments();
            args.putInt("flag", 0);
            if (postid != args.getInt("category_id")){
                args.putInt("flag", 1);
                args.putInt("category_id", postid);
            }
            changeTransaction.show(item);
            item.onResume();
        }else {
            Bundle args = new Bundle();
            args.putInt("flag", 1);
            args.putInt("category_id", postid);
            item.setArguments(args);
            changeTransaction.add(R.id.basicfragment, item, FOOD_EXCAHNGE_TAG);
        }
        changeTransaction.commit();
    }

    @Override
    public void backToFoodCategory() {
        FoodFragment buy = (FoodFragment) basicFragment.findFragmentByTag(FOOD_LIST_CONTAINER);
        FoodCategoryFragment foodcategory = (FoodCategoryFragment) basicFragment.findFragmentByTag(FOOD_CONTAINER);
        FOOD_EXCAHNGE_TAG = FOOD_CONTAINER;
        this.hideFragments();
        FragmentTransaction backTransaction = basicFragment.beginTransaction();
        backTransaction.show(mapFragment.get(FOOD_EXCAHNGE_TAG));
        backTransaction.commit();
    }

    private class OnJumpMineActivityClick implements OnClickListener{

        @Override
        public void onClick(View v) {
            Intent in_mine = new Intent(BasicViewActivity.this, MineActivity.class);
            startActivity(in_mine);
        }
    }
}
