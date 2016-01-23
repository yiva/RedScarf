package com.redscarf.weidou.adapter;

import android.util.Log;

import com.redscarf.weidou.pojo.CookieBody;
import com.redscarf.weidou.util.JSONHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *  Cookie Json
 * Created by yeahwang on 2015/10/10.
 */
public class CookieAdapter {

    private static final String TAG = CookieAdapter.class.getSimpleName();

    public static CookieBody fromJSON(JSONObject response) {
        CookieBody body = new CookieBody();
        try {
            body = JSONHelper.parseObject(response, CookieBody.class);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return body;
    }

}
