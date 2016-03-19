package com.redscarf.weidou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.redscarf.weidou.adapter.CookieAdapter;
import com.redscarf.weidou.pojo.CookieBody;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录注册Activity
 * Created by yeahwang on 2015/10/9.
 */
public class LoginActivity extends BaseActivity {

    private final String TAG = LoginActivity.class.getSimpleName();

    private JsonObjectRequest jsonObjRequest;

    private EditText e_username;
    private EditText e_pwd;
    private Button btn_login;
    private Button btn_nonce;

    private CookieBody cookie_body;
    private Boolean cookie_valid;

    private final int MSG_GENERATE = 11; // msg.what generate cookie
    private final int MSG_VALID = 12; //msg.what valid cookie

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            switch (msg.what){
                case MSG_GENERATE:
                    cookie_body = indexObj.getParcelable("result");
                    loginForward(cookie_body);
                    break;
                case MSG_VALID:
                    cookie_valid = indexObj.getBoolean("valid");
                    loginValid(cookie_valid);
                    break;
            }
            hideProgressDialog();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
        this.registerButton();
//        mVolleyQueue = Volley.newRequestQueue(LoginActivity.this);
    }

    private void registerButton() {
        e_username = (EditText) findViewById(R.id.edit_login_email);
        e_pwd = (EditText) findViewById(R.id.edit_login_pwd);
        btn_login = (Button) findViewById(R.id.btn_login_submit);
//        btn_login_weibo = (Button) findViewById(R.id.btn_login_weibo);
//        btn_login_weixin = (Button) findViewById(R.id.btn_login_weixin);
//        btn_login_qq = (Button) findViewById(R.id.btn_login_qq);
        btn_nonce = (Button) findViewById(R.id.btn_login_nonce);

        btn_login.setOnClickListener(new onLogin());//login with email
        btn_nonce.setOnClickListener(new onNonceLogin());//login with guest
    }

    private void login() {
        showProgressDialog("", MyConstants.LOADING);
        String username = e_username.getText().toString().trim();
        String pwd = e_pwd.getText().toString().trim();
        String user_cookie = MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_COOKIE);
        if ("".equals(user_cookie) || null == user_cookie) {//之前未登录过，生成cookie

            jsonObjRequest = new JsonObjectRequest(Request.Method.GET,
                    RequestURLFactory.sysRequestURL(RequestType.LOGIN_FIRST,
                            new String[]{username, pwd}), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i(TAG, "success");
                    CookieBody arrRed = CookieAdapter.fromJSON(response);
                    Bundle data = new Bundle();
                    data.putParcelable("result", arrRed);
                    Message message = Message.obtain(handler, MSG_GENERATE);
                    message.setData(data);
                    handler.sendMessage(message);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "error", error);
                }
            });
        }else {//已经登录过，验证cookie
            jsonObjRequest = new JsonObjectRequest(Request.Method.GET,
                    RequestURLFactory.sysRequestURL(RequestType.LOGIN_AGAIN,
                            new String[]{MyPreferences.getAppPerenceAttribute(MyConstants.PREF_USER_COOKIE)}),
                   new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i(TAG, "success");
                    boolean res = false;
                    try {
                        res = response.getBoolean("valid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Bundle data = new Bundle();
                    data.putBoolean("valid", res);
                    Message message = Message.obtain(handler, MSG_VALID);
                    message.setData(data);
                    handler.sendMessage(message);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "error", error);
                }
            });
        }
        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjRequest.setTag(TAG);
        VolleyUtil.getRequestQueue().add(jsonObjRequest);
    }

    /**
     *
     * @param body 生成cookie进行登录
     */
    private void loginForward(CookieBody body) {
        if ("ok".equals(body.getStatus())) {
            MyPreferences.setAppPerenceAttribute(MyConstants.PREF_USER_COOKIE, body.getCookie());
            MyPreferences.setAppPerenceAttribute(MyConstants.PREF_USER_COOKIE_NAME, body.getCookie_name());
            MyPreferences.setAppPerenceAttribute(MyConstants.PREF_USER_ID, makeID(body.getUser()));
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent i_login = new Intent(LoginActivity.this, BasicViewActivity.class);
            startActivity(i_login);
            finish();
        }else {
            Toast.makeText(LoginActivity.this, body.getError(), Toast.LENGTH_SHORT).show();
            MyPreferences.setAppPerenceAttribute(MyConstants.PREF_USER_COOKIE, "");
            MyPreferences.setAppPerenceAttribute(MyConstants.PREF_USER_COOKIE_NAME, "");
        }
    }

    private String makeID(String userinfo) {
        String id = "";
        try {
            JSONObject jo = new JSONObject(userinfo);
            id = jo.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }


    /**
     *
     * @param valid 根据已存在cookie判断登录
     */
    private void loginValid(Boolean valid) {
        if (valid) {
            Intent i_login = new Intent(LoginActivity.this, BasicViewActivity.class);
            startActivity(i_login);
            finish();
        }else{
            Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void loginNonce() {
        Intent i_login_nonce = new Intent(LoginActivity.this, BasicViewActivity.class);
        startActivity(i_login_nonce);
    }

    private class onLogin implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            login();
        }
    }

    private class onNonceLogin implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            loginNonce();
        }
    }

}
