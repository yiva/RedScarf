package com.redscarf.weidou.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.FoodHeaderGridAdapter;
import com.redscarf.weidou.pojo.GridBody;
import com.redscarf.weidou.util.ActionBarType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeahwang on 2015/12/26.
 */
public class FoodCategoryFragment extends BaseFragment {

    private GridView grid_food;
    private List<GridBody> datas;


    private OnChangeFoodListFragmentListener mchangelistener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_food_category, container, false);

        initView();
        return rootView;
    }


    @Override
    public void initView() {
    }

    public interface OnChangeFoodListFragmentListener {
        void foodCategoryClick(int postid,String title);
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
                "东南亚餐厅", "西餐厅","印度餐厅", "中东餐厅",
                "下午茶", "咖啡甜品店", "外卖快餐店"};
        Integer[] postIds = {4, 533, 534, 535, 536, 537, 709, 538, 539, 540};
        List<GridBody> headerBody = new ArrayList<>();
        for (int i = 0; i < postIds.length; ++i) {
            headerBody.add(new GridBody(colors[i], title[i], photo[i], postIds[i]));
        }
        return headerBody;
    }

    private class OnFoodGridItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mchangelistener.foodCategoryClick(datas.get(position).getPostId(),datas.get(position)
                    .getTitle());
        }
    }
}
