package com.redscarf.weidou.activity.fragment;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.adapter.ViewPageFragmentAdapter;
import com.redscarf.weidou.customwidget.PagerSlidingTabStrip;

/**
 * 带有导航条的基类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年11月6日 下午4:59:50
 * 
 */
public abstract class BaseViewPagerFragment extends BaseFragment {

	protected PagerSlidingTabStrip mTabStrip;
	protected ViewPager mViewPager;
	protected ViewPageFragmentAdapter mTabsAdapter;

	// protected EmptyLayout mErrorLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_search, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mTabStrip = (PagerSlidingTabStrip) view
				.findViewById(R.id.pager_tabstrip);

		mViewPager = (ViewPager) view.findViewById(R.id.pager);


		mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
				mTabStrip, mViewPager);
		setScreenPageLimit();
		onSetupTabAdapter(mTabsAdapter);
	}
    
	/**
	 * 需要子类进行重写，设置viewpager的缓存页数
	 */
	protected void setScreenPageLimit() {
	}


	protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
}