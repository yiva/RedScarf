package com.redscarf.weidou.activity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.listener.CustomSimpleAdapter;
import com.redscarf.weidou.pojo.RedScarfBody;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 *
 */
public class IndividualShareFragment extends Fragment implements
		CustomSimpleAdapter {

	private ListView lv_individual;

	private View rootView;

	private ImageView userlogo; // 头像
	private ImageView gender; // 性别

	private TextView nickname; // 昵称
	private TextView count_attention;// 关注数
	private TextView count_fans;// 粉丝数
	private TextView count_share;// 分享数
	private TextView location;// 位置
	private TextView userinfo;// 用户介绍

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_individual_share,
				container, false);

		this.initUserInfo();

		// 定义列表
		lv_individual = (ListView) rootView.findViewById(R.id.list_individual);
		lv_individual.setAdapter(gatherSimpleAdapter());

		// lv_individual.setOnItemClickListener(new onListIndexItemClick());

		return rootView;
	}

	/**
	 * 设置用户个人信息
	 */
	private void initUserInfo() {
		userlogo = (ImageView) rootView.findViewById(R.id.img_individual_logo);
		nickname = (TextView) rootView
				.findViewById(R.id.txt_nickname_individual);
		gender = (ImageView) rootView.findViewById(R.id.ico_gender_individual);
		count_attention = (TextView) rootView
				.findViewById(R.id.txt_count_attention_individual);
		count_fans = (TextView) rootView
				.findViewById(R.id.txt_count_fans_individual);
		count_share = (TextView) rootView
				.findViewById(R.id.txt_count_share_individual);
		location = (TextView) rootView
				.findViewById(R.id.txt_location_individual);
		userinfo = (TextView) rootView.findViewById(R.id.txt_info_individual);

		userlogo.setBackgroundResource(R.drawable.logo);
		nickname.setText("英国红领巾");
		gender.setBackgroundResource(R.drawable.ico_female);
		count_attention.setText("35");
		count_fans.setText("357");
		count_share.setText("300");
		location.setText("英国，伦敦");
		userinfo.setText(getActivity().getResources().getString(
				R.string.demotext_food_detail_review_content));
	}

	@Override
	public SimpleAdapter gatherSimpleAdapter() {

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 6; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("img_individual_product",R.drawable.index_img);
			map.put("content_individual", getActivity().getResources().getString(
					R.string.demotext_shop_detail_content));
			map.put("location_content_individual", "Chai Wu");
			map.put("discuss_individual", "评论12");
			map.put("zan_individual", "赞35");
			map.put("favourite_individual", "收藏");
			listItem.add(map);
		}

		String[] individual_label = new String[] { "img_individual_product",
				"content_individual", "location_content_individual",
				"discuss_individual", "zan_individual", "favourite_individual", };
		int[] individual_contents = new int[] { R.id.img_individual_product,
				R.id.content_individual, R.id.location_content_individual,
				R.id.discuss_individual, R.id.zan_individual,
				R.id.favourite_individual };
		SimpleAdapter individualSimpleAdapter = new SimpleAdapter(
				getActivity(), listItem, R.layout.listview_individual, individual_label,
				individual_contents);
		return individualSimpleAdapter;
	}

	/*
	 * 点击跳转
	 */
//	private class onListIndexItemClick implements OnItemClickListener {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			Intent in_food_detail = new Intent(getActivity(),
//					FoodDetailActivity.class);
//			startActivity(in_food_detail);
//		}
//
//	}

	/*
	 * 各按钮事件
	 */
//	private class mIndividualSimpleAdapter extends SimpleAdapter {
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
////			View index_view = super.getView(position, convertView, parent);
////
////			ImageView img_index_user_logo = (ImageView) index_view
////					.findViewById(R.id.img_index_user_logo);
////			img_index_user_logo.setOnClickListener(new onIndividualBtnClick());
////
////			TextView txt_attention_index = (TextView) index_view
////					.findViewById(R.id.txt_attention_index);
////			txt_attention_index.setOnClickListener(new onIndividualBtnClick());
//
//			return index_view;
//		}
//
//		public mIndividualSimpleAdapter(Context context,
//				List<? extends Map<String, ?>> data, int resource,
//				String[] from, int[] to) {
//			super(context, data, resource, from, to);
//			// TODO Auto-generated constructor stub
//		}
//
//	}

	/*
	 * 事件实现
	 */
	private class onIndividualBtnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
//			switch (v.getId()) {
//			// 点击头像
//			case R.id.img_index_user_logo:
//				Toast.makeText(getActivity(), "Hello world", Toast.LENGTH_SHORT)
//						.show();
//				break;
//			// 关注
//			case R.id.txt_attention_index:
//				TextView txt_attention = (TextView) rootView
//						.findViewById(R.id.txt_attention_index);
//				if (txt_attention.getText() == ""
//						|| txt_attention.getText() == null) {
//					txt_attention.setBackground(null);
//					txt_attention.setText("已关注");
//				} else {
//					txt_attention.setText(null);
//					txt_attention.setBackgroundDrawable(getActivity()
//							.getResources().getDrawable(
//									R.drawable.attention_red));
//				}
//			default:
//				break;
//			}
		}

	}

	@Override
	public SimpleAdapter gatherSimpleAdapter(ArrayList<RedScarfBody> bodys) {
		// TODO Auto-generated method stub
		return null;
	}

}
