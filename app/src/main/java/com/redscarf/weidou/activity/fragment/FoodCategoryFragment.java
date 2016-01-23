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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeahwang on 2015/12/26.
 */
public class FoodCategoryFragment extends BaseFragment {

    private View rootView;
    private GridView grid_food;
    private List<GridBody> datas;


    private OnChangeFoodListFragmentListener mchangelistener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_food_category, container, false);

        grid_food = (GridView) rootView.findViewById(R.id.grid_food);
        grid_food.setAdapter(new FoodHeaderGridAdapter(getActivity(), datas = this.makeFoodHeaderGridArrays()));
        grid_food.setOnItemClickListener(new OnFoodGridItemClick());
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mchangelistener = (OnChangeFoodListFragmentListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException("must implement "
                    + OnChangeFoodListFragmentListener.class.getSimpleName());
        }
    }

    public interface OnChangeFoodListFragmentListener {
        void foodCategoryClick(int postid);
    }

    /**
     * 初始化顶部控件数据
     *
     * @return
     */
    public List<GridBody> makeFoodHeaderGridArrays() {
        Integer[] colors = {R.color.allcanting,
                R.color.chinesecanting,
                R.color.rihan,
                R.color.southeast,
                R.color.west,
                R.color.middleeast,
                R.color.noontea,
                R.color.cafe,
                R.color.fastfood};
        Integer[] photo = {R.drawable.all_canting,
                R.drawable.china_canting,
                R.drawable.sushi,
                R.drawable.noodle,
                R.drawable.hotfood,
                R.drawable.chicken_leg,
                R.drawable.tea,
                R.drawable.cake,
                R.drawable.forma};
        String[] title = {"全部餐厅", "中餐厅", "日韩餐厅",
                "东南亚餐厅", "西餐厅", "中东印巴餐厅",
                "下午茶", "咖啡甜品店", "外卖快餐店"};
        Integer[] postIds = {4, 533, 534, 535, 536, 537, 538, 539, 540};
        List<GridBody> headerBody = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            headerBody.add(new GridBody(colors[i], title[i], photo[i], postIds[i]));
        }
        return headerBody;
    }

    private class OnFoodGridItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mchangelistener.foodCategoryClick(datas.get(position).getPostId());
        }
    }
}
