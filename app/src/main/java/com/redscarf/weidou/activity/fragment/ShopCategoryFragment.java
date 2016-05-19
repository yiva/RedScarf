package com.redscarf.weidou.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.ShopGridAdapter;
import com.redscarf.weidou.pojo.GridBody;
import com.redscarf.weidou.util.ActionBarType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeahwang on 2015/12/26.
 */
public class ShopCategoryFragment extends BaseFragment {

    private GridView grid_shop;
    private List<GridBody> datas;

    private OnChangeShopListFragmentListener mlistener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_shop_category, container, false);


        initView();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mlistener = (OnChangeShopListFragmentListener) context;
        }catch(ClassCastException ex) {
            throw new ClassCastException(context.toString()
                    + "must implement OnChangeShopListFragmentListener");
        }

    }

    @Override
    public void initView() {
        setActionBarLayout(getResources().getString(R.string.title_shopping), ActionBarType.NORMAL);
        grid_shop = (GridView) rootView.findViewById(R.id.grid_shop);
        grid_shop.setAdapter(new ShopGridAdapter(getActivity(), datas = this.makeShopHeaderGridArrays()));
        grid_shop.setOnItemClickListener(new OnShopItemClick());
    }

    private class OnShopItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mlistener.OnShopCategoryClickItem(datas.get(position).getPostId(),datas.get(position)
                    .getTitle());
        }
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

    public interface OnChangeShopListFragmentListener{
        //类别ID
        void OnShopCategoryClickItem(int postid,String title);
    }


}
