package com.redscarf.weidou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.redscarf.weidou.adapter.CookieAdapter;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.pojo.CookieBody;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.JSONHelper;
import com.redscarf.weidou.util.weibo.AccessTokenKeeper;
import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.network.VolleyUtil;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
    private Button btn_register;
    private TextView txt_nonce;
    private LinearLayout btn_login_by_weibo;

    private CookieBody cookie_body;
    private Boolean cookie_valid;

    private AuthInfo mAuthInfo;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;
    /**
     * 用户信息接口
     */
    private UsersAPI mUsersAPI;

    private String response;

    private final int MSG_GENERATE = 11; // msg.what generate cookie
    private final int MSG_VALID = 12; //msg.what valid cookie
    private final int MSG_LOGIN_WEIBO = 13;//msg.what login with weibo

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            switch (msg.what) {
                case MSG_GENERATE:
                    cookie_body = indexObj.getParcelable("result");
                    loginForward(cookie_body);
                    break;
                case MSG_VALID:
                    cookie_valid = indexObj.getBoolean("valid");
                    loginValid(cookie_valid);
                    break;
                case MSG_LOGIN_WEIBO:
                    response = indexObj.getString("response");
                    try {
                        JSONObject iObj = new JSONObject(response);
                        cookie_body = JSONHelper.parseObject(iObj, CookieBody.class);
                    } catch (JSONException e) {
                        ExceptionUtil.printAndRecord(TAG, e);
                    }
                    loginForward(cookie_body);
                    break;
            }
            hideProgressDialog();

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
        GlobalApplication.getInstance().addActivity(this);
        this.initView();
    }

    @Override
    public void initView() {
        e_username = (EditText) findViewById(R.id.edit_login_email);
        e_pwd = (EditText) findViewById(R.id.edit_login_pwd);
        btn_login = (Button) findViewById(R.id.btn_login_submit);
        txt_nonce = (TextView) findViewById(R.id.txt_login_nonce);
        btn_register = (Button) findViewById(R.id.btn_login_register);
        btn_login_by_weibo = (LinearLayout) findViewById(R.id.btn_login_by_weibo);

        btn_login.setOnClickListener(new onLogin());//login with email
        txt_nonce.setOnClickListener(new onNonceLogin());//login with guest
        btn_register.setOnClickListener(new OnRegisterClick());//register
        btn_login_by_weibo.setOnClickListener(new OnLoginByWeibo());

        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, MyConstants.APP_KEY, MyConstants.REDIRECT_URL, MyConstants
                .SCOPE);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
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
        } else {//已经登录过，验证cookie
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
        } else {
            Toast.makeText(LoginActivity.this, body.getError(), Toast.LENGTH_SHORT).show();
            MyPreferences.setAppPerenceAttribute(MyConstants.PREF_USER_COOKIE, "");
            MyPreferences.setAppPerenceAttribute(MyConstants.PREF_USER_COOKIE_NAME, "");
        }
    }

    private class OnLoginByWeibo implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mSsoHandler.authorize(new AuthListener());
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
     * @param valid 根据已存在cookie判断登录
     */
    private void loginValid(Boolean valid) {
        if (valid) {
            Intent i_login = new Intent(LoginActivity.this, BasicViewActivity.class);
            startActivity(i_login);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void loginNonce() {
        Intent i_login_nonce = new Intent(LoginActivity.this, BasicViewActivity.class);
        startActivity(i_login_nonce);
    }

    private class onLogin implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            login();
        }
    }

    private class onNonceLogin implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loginNonce();
        }
    }

    private class OnRegisterClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i_register = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i_register);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitApp();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String phoneNum = mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                // 获取用户信息接口
                mUsersAPI = new UsersAPI(LoginActivity.this, MyConstants.APP_KEY, mAccessToken);
                long uid = Long.parseLong(mAccessToken.getUid());
                mUsersAPI.show(uid, mListener);
                Toast.makeText(LoginActivity.this,
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
//        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
//        mTokenText.setText(message);
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                LogUtil.i(TAG, response);
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    String email = "wb" + user.idstr + "@weidou.co.uk";
                    String gender = "";
                    if ("f".equals(user.gender)) {
                        gender = "女";
                    } else if ("m".equals(user.gender)) {
                        gender = "男";
                    }
                    doRequestURL(Request.Method.GET, RequestURLFactory.sysRequestURL(RequestType
                                    .LOGIN_WEIBO, new String[]{user.idstr, email, email, user
                                    .screen_name, user.screen_name,
                                    gender, user.location}),
                            LoginActivity.class,
                            handler,
                            MSG_LOGIN_WEIBO, PROGRESS_DISVISIBLE);
                } else {
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(LoginActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
}
