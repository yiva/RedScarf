package com.redscarf.weidou.activity.fragment;


import java.util.ArrayList;

import org.json.JSONException;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.activity.WhoseShareActivity;
import com.redscarf.weidou.adapter.IndexListAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 首页界面
 *
 * @author yeahwa
 */
@SuppressLint("ResourceAsColor")
public class IndexFragment extends BaseFragment implements OnTouchListener {

    private final String TAG = IndexFragment.class.getSimpleName();

    private ListView lv_index;
    private ListView lv_tag;
    private View rootView;


    private ImageButton search;

    private TextView is_click_head_btn;
    private TextView hot_index;
    private TextView attention;
    private TextView hot_food;
    private TextView hot_buy;

    private final int MSG_INDEX = 1; // msg.what index
    private final int MSG_TAG = 2;  //msg.what Tag Post

    private ArrayList<RedScarfBody> bodys;
    private String response;
    private Bundle datas;

    private float lastY = 0f;
    private float currentY = 0f;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            response = indexObj.getString("response");
            if (msg.what == MSG_INDEX) {
                try {
                    bodys = RedScarfBodyAdapter.fromJSON(response, RedScarfBody.class);
                } catch (JSONException e) {
                    ExceptionUtil.printAndRecord(TAG, e);
                }
                if (bodys.size() != 0) {
                    lv_index.setAdapter(new IndexListAdapter(getActivity(), bodys));
                }
                hideProgressDialog();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_index, container, false);
        lv_index = (ListView) rootView.findViewById(R.id.list_index);
        lv_index.setOnItemClickListener(new onListIndexItemClick());
        lv_index.setOnTouchListener(this);
        lv_index.setLongClickable(true);
        View header = inflater.inflate(R.layout.header_index, null);
        lv_index.addHeaderView(header, null, false);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // doPostURL();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        datas = getActivity().getIntent().getExtras();//接收tag数据
        //类别ID{183：分享|4：美食|6：购物}
        //请求url参数
        if (null == datas) {
            doRequestURL(RequestURLFactory.getRequestListURL(RequestType.INDEXLIST, new String[]{"1"}), IndexFragment.class, handler, MSG_INDEX);
        } else if ("tag_post".equals(datas.getString("flag"))) {
            doRequestURL(RequestURLFactory.getRequestListURL(RequestType.TAGLIST, new String[]{Uri.encode(datas.getString("tag_slug"), "utf-8")}), IndexFragment.class, handler, MSG_INDEX);
            datas.clear();

        } else if ("author_post".equals(datas.getString("flag"))) {
            doRequestURL(RequestURLFactory.getRequestListURL(RequestType.AUTHORLIST, new String[]{datas.getString("author_id")}), IndexFragment.class, handler, MSG_INDEX);
            datas.clear();

        }
        this.customClickListener();
    }

    @Override
    public void initView() {
        search = (ImageButton) rootView.findViewById(R.id.btnIndexSearch);

        attention = (TextView) rootView.findViewById(R.id.txt_hot_title_attention);
        hot_index = (TextView) rootView.findViewById(R.id.txt_hot_title_best);
        hot_food = (TextView) rootView.findViewById(R.id.txt_hot_title_food);
        hot_buy = (TextView) rootView.findViewById(R.id.txt_hot_title_shop);
//
        hot_index.setOnClickListener(new onSelectCategary());
        hot_food.setOnClickListener(new onSelectCategary());
        hot_buy.setOnClickListener(new onSelectCategary());
        attention.setOnClickListener(new onSelectCategary());

        //设置处于焦点tab
        hot_index.setTextColor(getResources().getColor(R.color.black));
        hot_index.setClickable(false);
        is_click_head_btn = hot_index;
    }

    private void customClickListener() {
        getActivity().findViewById(R.id.btnIndexSearch).setOnClickListener(
                new onSearchClickListener());
//		getActivity().findViewById(R.id.txt_attention_title_index)
//				.setOnClickListener(new onAttentionClickListener());
    }

    /*
     * 点击跳转
     */
    private class onListIndexItemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            RedScarfBody bodyItem = bodys.get(position - 1);
            enterSubActivity(bodyItem);
        }

    }

//	/**
//	 * tag跳转
//	 */
//	private class onListTagItemClick implements OnItemClickListener{
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////			Arrays.asList(getItem(position).getTags().split(","));
//		}
//	}

    /**
     * 跳转到分享二级页面
     */
    private void enterSubActivity(RedScarfBody item) {
        Intent i_sub = new Intent(getActivity(), WhoseShareActivity.class);
        Bundle data = new Bundle();
        data.putParcelable("body", item);
        i_sub.putExtras(data);
        startActivity(i_sub);
    }

//	/**
//	 * 各按钮事件
//	 */
//	private class mIndexSimpleAdapter extends SimpleAdapter {
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View index_view = super.getView(position, convertView, parent);
//			// 用户logo
//			ImageView img_index_user_logo = (ImageView) index_view
//					.findViewById(R.id.img_index_user_logo);
//			img_index_user_logo.setOnClickListener(new onIndexBtnClick());
//			// 用户名称
//			TextView txt_user_name = (TextView) index_view
//					.findViewById(R.id.txt_username_index);
//			txt_user_name.setOnClickListener(new onIndexBtnClick());
//			// 加关注
//			TextView txt_attention_index = (TextView) index_view
//					.findViewById(R.id.txt_attention_index);
//			txt_attention_index.setOnClickListener(new onIndexBtnClick());
//
//			return index_view;
//		}
//
//		public mIndexSimpleAdapter(Context context,
//								   List<? extends Map<String, ?>> data, int resource,
//								   String[] from, int[] to) {
//			super(context, data, resource, from, to);
//			// TODO Auto-generated constructor stub
//		}
//
//	}

    /*
     * 事件实现
     */
//    private class onIndexBtnClick implements OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                // 点击头像
//                case R.id.img_index_user_logo:
//                    Toast.makeText(getActivity(), "Hello world", Toast.LENGTH_SHORT)
//                            .show();
//                    break;
//                // 点击用户名
//                case R.id.txt_username_index:
//                    View view = getActivity().findViewById(R.id.adv_frame);
//                    view.setVisibility(View.GONE);
//                    IndividualShareFragment individualShare = new IndividualShareFragment();
//
//                    FragmentTransaction transaction = getFragmentManager()
//                            .beginTransaction();
//
//                    transaction.replace(R.id.basicfragment, individualShare,
//                            "IndividualShare");
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    break;
//                // 关注
//                case R.id.txt_attention_index:
//                    TextView txt_attention = (TextView) rootView
//                            .findViewById(R.id.txt_attention_index);
//                    if (txt_attention.getText() == ""
//                            || txt_attention.getText() == null) {
//                        txt_attention.setBackgroundResource(0);
//                        txt_attention.setText("已关注");
//                    } else {
//                        txt_attention.setText(null);
//                        txt_attention.setBackgroundResource(R.drawable.attention_red);
//                    }
//                default:
//                    break;
//            }
//        }
//
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 下面两个表示滑动的方向，大于0表示向下滑动，小于0表示向上滑动，等于0表示未滑动
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
                // 只有在listView.getFirstVisiblePosition()>0的时候才判断是否进行显隐动画。因为listView.getFirstVisiblePosition()==0时，
                // ToolBar——也就是头部元素必须是可见的，如果这时候隐藏了起来，那么占位置用了headerview就被用户发现了
                // 但是当用户将列表向下拉露出列表的headerview的时候，应该要让头尾元素再次出现才对——这个判断写在了后面onScrollListener里面……
                float tmpCurrentY = event.getY();
                if (Math.abs(tmpCurrentY - lastY) > MyConstants.FLING_MIN_DISTANCE) {// 滑动距离大于touchslop时才进行判断
                    currentY = tmpCurrentY;
                    currentDirection = (int) (currentY - lastY);
                    if (lastDirection != currentDirection) {
                        // 如果与上次方向不同，则执行显/隐动画
                        if (currentDirection < 0) {
                            getActivity().findViewById(R.id.actionbar_index)
                                    .setVisibility(View.GONE);
                        } else {
                            getActivity().findViewById(R.id.actionbar_index)
                                    .setVisibility(View.VISIBLE);
                        }
                    }
                    lastY = currentY;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 手指抬起的时候要把currentDirection设置为0，这样下次不管向哪拉，都与当前的不同（其实在ACTION_DOWN里写了之后这里就用不着了……）
                currentDirection = 0;
                lastDirection = 0;
                break;
        }
        return false;
    }

    public void setMarginTop(int page) {
        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParam.setMargins(0, page, 0, 0);
        lv_index.setLayoutParams(layoutParam);
        lv_index.invalidate();
    }

    private class onSearchClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Search Button", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private class onHotClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Hot Button", Toast.LENGTH_SHORT)
                    .show();
//			attention.setTextColor(getActivity().getResources().getColor(R.color.white));
//			hot.setTextColor(R.color.black);
        }

    }

    private class onAttentionClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Attention Button",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class onSelectCategary implements OnClickListener {

        @Override
        public void onClick(View v) {
            changeBottomButton();
            switch (v.getId()) {
                case R.id.txt_hot_title_best:
                    changeFocusButton(hot_index);
                    doRequestURL(RequestURLFactory.getRequestListURL(RequestType.INDEXLIST, new String[]{"1"}), IndexFragment.class, handler, MSG_INDEX);
                    break;
                case R.id.txt_hot_title_food:
                    changeFocusButton(hot_food);
                    doRequestURL(RequestURLFactory.getRequestListURL(RequestType.HOTFOODLIST, new String[]{"1"}), IndexFragment.class, handler, MSG_INDEX);
                    break;
                case R.id.txt_hot_title_shop:
                    changeFocusButton(hot_buy);
                    doRequestURL(RequestURLFactory.getRequestListURL(RequestType.HOTSBUYLIST, new String[]{"1"}), IndexFragment.class, handler, MSG_INDEX);
                    break;
                case R.id.txt_hot_title_attention:
                    changeFocusButton(attention);
                    break;
                default:
                    break;
            }
        }
    }

    private void changeBottomButton() {
        hot_index.setTextColor(getResources().getColor(R.color.white));
        hot_food.setTextColor(getResources().getColor(R.color.white));
        hot_buy.setTextColor(getResources().getColor(R.color.white));
        attention.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * 设置不可点击
     *
     * @param btn 点击按钮
     */
    private void changeFocusButton(TextView btn) {
        is_click_head_btn.setClickable(true);
        is_click_head_btn.setTextColor(getResources().getColor(R.color.white));
        is_click_head_btn = btn;
        is_click_head_btn.setClickable(false);
        is_click_head_btn.setTextColor(getResources().getColor(R.color.black));
    }

}
