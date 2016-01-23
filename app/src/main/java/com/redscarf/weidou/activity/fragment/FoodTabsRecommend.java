package com.redscarf.weidou.activity.fragment;

import java.util.ArrayList;
import java.util.List;




import com.redscarf.weidou.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FoodTabsRecommend extends Fragment {

	private View rootView;
	private ListView listFood;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rootView =  inflater.inflate(R.layout.fragment_food_tabs_recommend, container,false);

		listFood = (ListView) rootView.findViewById(R.id.list_food_recommend);

		for (int i = 0; i <10; i++) {
			listFood.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getData()));
		}


		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		rootView = this.getView();
//		rootView.findViewById(R.id.listview_food_recommend).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(getActivity(),FoodDetailActivity.class);
//				startActivity(intent);
//			}
//		});



	}


	private List<String> getData(){

		List<String> data = new ArrayList<String>();
		data.add("测试数据1");
		data.add("测试数据2");
		data.add("测试数据3");
		data.add("测试数据4");

		return data;
	}

}
