package com.redscarf.weidou.adapter;

import java.util.Iterator;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.BitmapCache;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.VolleyUtil;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
	protected int selectedPosition = -1;
	
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


	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition(){
		return selectedPosition;
	}

	protected void formatRedScarfBody(RedScarfBody item) {
		try {
			item.setTags(denoteJSONArray(new JSONArray(item.getTags()), "title"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
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

	/**
	 * 设置图片大小比为3：2
	 * @param v 需设置图片
	 */
	protected void setImageViewMeasure(final View v){
		//按比例设置图片大小
		ViewTreeObserver vto = v.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onGlobalLayout() {
				v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				v.getHeight();
				int width = v.getWidth();
				int height = width * 2 / 3;
				ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
				layoutParams.width = width;
				layoutParams.height = height;
				v.setLayoutParams(layoutParams);
			}
		});
	}

	/**
	 * 设置图片大小比为3：2
	 * @param v 需设置图片
	 */
	protected void setImageViewMeasure(final View v,final Integer width){
		//按比例设置图片大小
		ViewTreeObserver vto = v.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onGlobalLayout() {
				v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				v.getHeight();
				int height = width * 2 / 3;
				ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
				layoutParams.width = width;
				layoutParams.height = height;
				v.setLayoutParams(layoutParams);
			}
		});
	}

}
