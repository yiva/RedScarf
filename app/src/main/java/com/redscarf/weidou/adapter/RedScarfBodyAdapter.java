package com.redscarf.weidou.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.JSONHelper;

import android.util.Log;
import android.widget.Toast;

public class RedScarfBodyAdapter {

	public static ArrayList<RedScarfBody> fromJSON(String json){
		ArrayList<RedScarfBody> items = new ArrayList<RedScarfBody>();
		String jsonStr = "";
		try {
			JSONObject jsonObj = new JSONObject(json);
			//单条是为post，多条时为posts
			if (jsonObj.isNull("posts")) {
				jsonStr = jsonObj.getString("post");
				JSONObject iObj = new JSONObject(jsonStr);
				RedScarfBody item = JSONHelper.parseObject(iObj, RedScarfBody.class);
				items.add(item);
			} else {
				jsonStr = jsonObj.getString("posts");
				items = (ArrayList<RedScarfBody>) JSONHelper.parseCollection(jsonStr, ArrayList.class, RedScarfBody.class);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ERROR", e.getMessage());
			return null;
		}
		return items;
	}

	public static <T> ArrayList<T> fromJSON(String json,Class<T> cl) throws JSONException{
		ArrayList<T> items = new ArrayList<T>();
		String jsonStr = "";
		JSONObject jsonObj = new JSONObject(json);
		//单条是为post，多条时为posts
		if (jsonObj.isNull("posts")) {
			jsonStr = jsonObj.getString("post");
			JSONObject iObj = new JSONObject(jsonStr);
			T item = (T) JSONHelper.parseObject(iObj, cl);
			items.add(item);
		} else {
			jsonStr = jsonObj.getString("posts");
			items = (ArrayList<T>) JSONHelper.parseCollection(jsonStr, ArrayList.class, cl);
		}
		return items;
	}

	public static <T> T parseObj(String jsonStr,Class<T> cl) throws JSONException {
		T instance = null;
		instance = JSONHelper.parseObject(jsonStr, cl);
		return instance;
	}


	private static String denoteJSONArray(JSONArray json, String key){
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

//	private static String denoteJSONObject(JSONObject json, String key){
//		if (!json.isNull(0)) {
//			try {
//				JSONObject jo = json.getJSONObject(0);
//				for (Iterator iter = jo.keys(); iter.hasNext();) {
//					String s = (String) iter.next();
//					if (key == s || s.equals(key)) {
//						return jo.getString(key);
//					}
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return "";
//	}

}
