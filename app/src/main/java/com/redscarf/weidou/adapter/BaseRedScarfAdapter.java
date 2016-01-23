package com.redscarf.weidou.adapter;

import java.util.Iterator;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.VolleyUtil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseRedScarfAdapter<T> extends BaseAdapter{
	
	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<T> mRedScarfBodies;
	protected ImageLoader imageLoader;
	protected RequestQueue queue;
	
	public BaseRedScarfAdapter(Context context,
			List<T> listData) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mRedScarfBodies = listData;
		this.queue = VolleyUtil.getRequestQueue();
		this.imageLoader = new ImageLoader(queue, new BitmapCache());
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mRedScarfBodies.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mRedScarfBodies.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	protected void formatRedScarfBody(RedScarfBody item) {
		try {
//			item.setCategories(denoteJSONArray(
//					new JSONArray(item.getCategories()), "title"));
			item.setTags(denoteJSONArray(new JSONArray(item.getTags()), "title"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
//		item.setContent(replaceContent(item.getContent()));
	}

	//index数据格式化
	protected void formatIndexRedScarfBody(RedScarfBody item) {
		//去掉<p></p>
		item.setContent(replaceContent(item.getContent()));
		try {
			StringBuffer sb = new StringBuffer();
			JSONArray jarr = new JSONArray(item.getTags());
			for (int i = 0; i < jarr.length(); i++) {
				JSONObject jo = jarr.getJSONObject(i);
				sb.append(jo.getString("title") + ",");
			}
			sb.setLength(sb.length() - 1);
			item.setTags(sb.toString());

		} catch (JSONException e) {
			Log.e("IndexApapter", e.getMessage());
		} catch (Exception e) {
			item.setTags("");
		}

	}

	private String denoteJSONArray(JSONArray json, String key){
		if (!json.isNull(0)) {
			try {
				JSONObject jo = json.getJSONObject(0);
				for (Iterator iter = jo.keys(); iter.hasNext();) {
					String s = (String) iter.next();
					if (key == s || s.equals(key)) {
						return jo.getString(key);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String replaceContent(String str) {
		return str.replaceAll(MyConstants.REPLACE_STRINGS, "");

	}

}
