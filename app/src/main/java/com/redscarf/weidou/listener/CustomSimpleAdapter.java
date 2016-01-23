package com.redscarf.weidou.listener;

import java.util.ArrayList;

import com.redscarf.weidou.pojo.RedScarfBody;

import android.widget.SimpleAdapter;

public interface CustomSimpleAdapter {
	
	public SimpleAdapter gatherSimpleAdapter(ArrayList<RedScarfBody> bodys);
	
	public SimpleAdapter gatherSimpleAdapter();

}
