package com.redscarf.weidou.adapter;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.util.JSONHelper;

public class MemberBodyAdapter {

	public static ArrayList<RedScarfBody> fromJSON(JSONObject jsonObj){
		ArrayList<RedScarfBody> items = new ArrayList<RedScarfBody>();
		String jsonStr = "";
		try {
			//单条是为post，多条时为posts
			if (jsonObj.isNull("profilefields")) {
				jsonStr = jsonObj.getString("profilefields");
				JSONObject iObj = new JSONObject(jsonStr);
				RedScarfBody item = JSONHelper.parseObject(iObj, RedScarfBody.class);
				items.add(item);
			}else{
				jsonStr = jsonObj.getString("posts");
				items = (ArrayList<RedScarfBody>) JSONHelper.parseCollection(jsonStr, ArrayList.class, RedScarfBody.class);
			}
			/*
			 * categories:[{"id":4,"parent":0,"slug":"%e7%be%8e%e9%a3%9f","title":"美食","description":"","post_count":22}]
			 */
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("ERROR", e.getMessage());
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("ERROR", e.getMessage());
		}
		return items;
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

}
