package com.redscarf.weidou.activity.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.ShopGridAdapter;
import com.redscarf.weidou.pojo.GridBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.GlobalApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by XZR on 2016/6/25.
 */
public class ShopCategaryPopup extends PopupWindow {

    private View conentView;
    private GridView grid_shop;

    private List<GridBody> datas;
    private Activity context;
    public Handler mHandler;

    public ShopCategaryPopup(final Activity mContext) {
        this.context = mContext;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.fragment_shop_category, null);
        this.initView();
        int h = GlobalApplication.getScreenHeight(context);
        int w = GlobalApplication.getScreenWidth(context);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(h);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
//      // 这个是为了点击“返回Back”也能使其消失，并且不会影响背景
        this.setBackgroundDrawable(new BitmapDrawable());
//        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent,0, 8);
        } else {
            this.dismiss();
        }
    }

    public void initView() {
//        setActionBarLayout(getResources().getString(R.string.title_shopping), ActionBarType.NORMAL);
        grid_shop = (GridView) conentView.findViewById(R.id.grid_shop);
        grid_shop.setAdapter(new ShopGridAdapter(context, datas = this.makeShopHeaderGridArrays()));
        grid_shop.setOnItemClickListener(new OnShopItemClick());
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

    private class OnShopItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            GridBody body = datas.get(position);
            EventBus.getDefault().post(body);
            ShopCategaryPopup.this.dismiss();
        }
    }

}
