package com.redscarf.weidou.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.redscarf.weidou.activity.GoodsDetailActivity;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.BuyListAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.pojo.GoodsBody;
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
import android.widget.ImageButton;
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
public class BuyFragment extends BaseFragment implements OnTouchListener {

	private final String TAG = BuyFragment.class.getSimpleName();

	private ImageButton discount_search;
	private TextView shop_selector;
	private View rootView;
	private PopupWindow selecter;
	private TextView txt_discount;
	private TextView txt_brand;
	private View is_click_head_btn;
	private ListView lv_shop;
	private ListView lv_selector;

	List<TextView> headtabs = new ArrayList<TextView>();
	private String response;
	private final int MSG_INDEX = 1; //msg.what index
	private ArrayList<GoodsBody> bodys;
	private float lastY = 0f;
	private float currentY = 0f;

	BackShopCategoryListener mbackClickListener;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_INDEX) {
				Bundle indexObj = msg.getData();
				response = indexObj.getString("response");
				try {
					bodys = (ArrayList<GoodsBody>) RedScarfBodyAdapter.fromJSON(response,Class.forName("com.redscarf.weidou.pojo.GoodsBody"));
				} catch (ClassNotFoundException e) {
					ExceptionUtil.printAndRecord(TAG, e);
				} catch (JSONException e) {
					ExceptionUtil.printAndRecord(TAG, e);
				}
				if (bodys.size() != 0) {
					lv_shop.setAdapter(new BuyListAdapter(getActivity(), bodys));
				}
				hideProgressDialog();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_buy, container, false);




		//选项卡
//		rootView.findViewById(R.id.index_discount).setOnClickListener(new onHeadTabListener());
//		rootView.findViewById(R.id.index_beauty).setOnClickListener(new onHeadTabListener());

		// 定义列表
		lv_shop = (ListView) rootView.findViewById(R.id.list_shop);

		lv_shop.setOnItemClickListener(new onListBuyItemClick());
		lv_shop.setOnTouchListener(this);
		lv_shop.setLongClickable(true);
		View header = inflater.inflate(R.layout.header_buy, null);
		lv_shop.addHeaderView(header, null, false);
		registerButton();


		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		Integer flag = getArguments().getInt("flag");
		Integer category_id = getArguments().getInt("category_id");
		if (flag.equals(1)) {
			showProgressDialogNoCancelable("", MyConstants.LOADING);
			doRequestURL(RequestURLFactory.getRequestListURL(RequestType.BUYLIST, new String[]{category_id.toString(), "1"}), BuyFragment.class, handler, MSG_INDEX);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try{
			mbackClickListener = (BackShopCategoryListener) context;
		}catch (ClassCastException ex){
			throw new ClassCastException(context.toString()
					+ "must implement BackShopCategoryFragment");
		}
	}

	private void registerButton() {
		ImageButton back = (ImageButton) rootView.findViewById(R.id.btn_shop_list_back);
		back.setOnClickListener(new OnbackClick());
//		discount_search = (ImageButton) getActivity().findViewById(R.id.btn_disount_search);
//		shop_selector = (TextView) rootView.findViewById(R.id.btnDiscountSelector);
//		//弹出框
//		View selectorView = getActivity().getLayoutInflater().inflate(R.layout.pop_selector, null);
//		lv_selector = (ListView) selectorView.findViewById(R.id.list_pop_selector);
//		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_selector, getDatas());
//		lv_selector.setAdapter(arrayAdapter);
//		shop_selector.setOnClickListener(new onMenuClickListener());

//		selecter = new PopupWindow(selectorView, measureContentWidth(arrayAdapter), ViewGroup.LayoutParams.WRAP_CONTENT, true);
//		selecter.setBackgroundDrawable(new BitmapDrawable());
//		getActivity().findViewById(R.id.btnDiscountSelector).setOnClickListener(new onMenuClickListener());
//		getActivity().findViewById(R.id.btn_disount_search).setOnClickListener(new onSearchClickListener());
//		txt_discount = (TextView) rootView.findViewById(R.id.index_discount);
//		txt_brand = (TextView) rootView.findViewById(R.id.index_beauty);
//		is_click_head_btn = txt_discount;
//		is_click_head_btn.setClickable(false);
	}


	/*
	 * 头部选项卡切换事件
	 */
	private class onHeadTabListener implements OnClickListener {

		@Override
		public void onClick(View v) {
//			setAllHeadTab();
			for (TextView headtab : headtabs) {
				headtab.setBackgroundColor(0);
			}
			getActivity().findViewById(v.getId()).setBackgroundColor(getResources().getColor(R.color.customappred));
//			switch (v.getId()) {
////				case R.id.index_discount://Discount
////					changeFocusButton(txt_discount);
////						url_map.put("id", "5");
////						doRequestURL(MyConstants.URL, url_map, BuyFragment.class, handler, MSG_INDEX);
////				break;
////				case R.id.index_beauty://Brand
////					changeFocusButton(txt_brand);
////						url_map.put("id", "283");
////						doRequestURL(MyConstants.URL, url_map, BuyFragment.class, handler, MSG_INDEX);
////
////				break;
//			}
		}

	}

	/**
	 * 获取选项卡控件
	 */
//	public void setAllHeadTab() {
//		headtabs.add((TextView) getActivity().findViewById(R.id.index_discount));
//		headtabs.add((TextView) getActivity().findViewById(R.id.index_beauty));
//	}


	/*
	 * 点击跳转
	 */
	private class onListBuyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			GoodsBody bodyItem = bodys.get(position);
			Intent in_shop_detail = new Intent(getActivity(), GoodsDetailActivity.class);

			ArrayList<String> items = BuyListAdapter.denoteCategorys(bodyItem.getCategories());
			Bundle data = new Bundle();
			data.putString("key", bodyItem.getId());
			data.putString("title", items.get(0));
			in_shop_detail.putExtras(data);

			startActivity(in_shop_detail);
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
						//如果与上次方向不同，则执行显/隐动画
						if (currentDirection < 0) {
							getActivity().findViewById(R.id.actionbar_buy).setVisibility(View.GONE);
						} else {
							getActivity().findViewById(R.id.actionbar_buy).setVisibility(View.VISIBLE);
						}
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
		RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		layoutParam.setMargins(0, page, 0, 0);
		lv_shop.setLayoutParams(layoutParam);
		lv_shop.invalidate();
	}

	private class onSearchClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), "Search Button", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 左上角菜单事件
	 */
	private class onMenuClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (!selecter.isShowing()) {
				selecter.showAsDropDown(getActivity().findViewById(R.id.actionbar_buy), 0, 0);
				selecter.setOutsideTouchable(true);
			} else {
				selecter.dismiss();
			}
		}
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
	 * @param btn 点击按钮
	 */
	private void changeFocusButton(View btn) {
		is_click_head_btn.setClickable(true);
		is_click_head_btn = btn;
		is_click_head_btn.setClickable(false);
	}

	//返回ShopCategoryFragment
	private class OnbackClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			mbackClickListener.backToShopCategory();
		}
	}

	public interface BackShopCategoryListener{
		void backToShopCategory();
	}

}
