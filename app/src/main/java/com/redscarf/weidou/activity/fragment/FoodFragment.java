package com.redscarf.weidou.activity.fragment;

import java.util.ArrayList;

import com.redscarf.weidou.activity.FoodDetailActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.FoodListAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.pojo.FoodBody;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FoodFragment extends BaseFragment implements OnTouchListener {


    private final String TAG = FoodFragment.class.getSimpleName();

    private ListView lv_food;

    private final int MSG_INDEX = 1; //msg.what index
    private ArrayList<FoodBody> bodys;
    private String response;
    private float lastY = 0f;
    private float currentY = 0f;

    private BackFoodCategoryListener mfoodBackListener;
    private static Integer category_id;
    private static Integer flag;
    private static String title;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_INDEX) {
                Bundle indexObj = msg.getData();
                response = indexObj.getString("response");
                try {
                    bodys = (ArrayList<FoodBody>) RedScarfBodyAdapter.fromJSON(response, Class.forName("com.redscarf.weidou.pojo.FoodBody"));
                } catch (Exception e) {
                    ExceptionUtil.printAndRecord(TAG, e);
                }
                if (bodys.size() != 0) {
                    lv_food.setAdapter(new FoodListAdapter(getActivity(), bodys));
                }
                hideProgressDialog();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mfoodBackListener = (BackFoodCategoryListener) context;
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString()
                    + "must implement BackShopCategoryFragment");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            flag = getArguments().getInt("flag");
            category_id = getArguments().getInt("category_id");
            title = getArguments().getString("title");
            setActionBarLayout(title, ActionBarType.WITHBACK);

            if (flag.equals(1)) {
                getArguments().putInt("flag",0);
                showProgressDialogNoCancelable("", MyConstants.LOADING);
                doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST, new String[]{category_id.toString(),"1"}), FoodFragment.class, handler, MSG_INDEX);
            }
        } catch (Exception ex) {
            ExceptionUtil.printAndRecord(TAG, ex);
            setActionBarLayout(title, ActionBarType.WITHBACK);
            showProgressDialogNoCancelable("", MyConstants.LOADING);
            doRequestURL(RequestURLFactory.getRequestListURL(RequestType.FOODLIST, new String[]{category_id.toString(), "1"}), FoodFragment.class, handler, MSG_INDEX);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            try {
                flag = getArguments().getInt("flag");
                category_id = getArguments().getInt("category_id");
                title = getArguments().getString("title");
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
    }

    @Override
    public void initView() {
        ImageButton back = (ImageButton) rootView.findViewById(R.id.actionbar_back);
        back.setOnClickListener(new OnBackClick());
        lv_food = (ListView) rootView.findViewById(R.id.list_food);
        lv_food.setOnItemClickListener(new onListFoodItemClick());
        lv_food.setOnTouchListener(this);
        lv_food.setLongClickable(true);
    }

    private class OnBackClick implements OnClickListener{

        @Override
        public void onClick(View v) {
            mfoodBackListener.backToFoodCategory();
        }
    }


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

    /**
     * selector list datas
     *
     * @return selectors
     */
    public ArrayList<String> getDatas() {
        ArrayList<String> selectors = new ArrayList<String>();
        selectors.add("委媛推荐");
        selectors.add("离我最近");
        selectors.add("最热查询");
        selectors.add("最高评价");
        return selectors;
    }

    public interface BackFoodCategoryListener{
        void backToFoodCategory();
    }

}
