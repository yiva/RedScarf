package com.redscarf.weidou.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.redscarf.weidou.activity.WebActivity;
import com.redscarf.weidou.listener.BasePageLinstener;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.util.MyPreferences;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 基础类
 * 继承FragmentActivity
 *
 * @author yeahwa
 */
public abstract class BaseActivity extends FragmentActivity implements BasePageLinstener {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private ProgressDialog progressDialog;
    private StringRequest stringRequest;
    private TextView actionbar_title;
    protected View view_404;

    protected HashMap<String, String> url_map;
    protected FragmentManager basicFragment = getSupportFragmentManager();
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		url_map = new HashMap<String,String>();
        view_404 = getLayoutInflater().inflate(R.layout.view_404, null);
    }

    /**
     * 监听是否有分享信息
     */
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 设置ActionBar的布局
     * 布局Id
     */
    protected void setActionBarLayout(String title, ActionBarType type) {

        switch (type) {
            case NORMAL:
                ImageButton btn_back = (ImageButton) findViewById(R.id.actionbar_back);
                btn_back.setVisibility(View.GONE);
            case WITHBACK:
                actionbar_title = (TextView) findViewById(R.id.actionbar_title);
                break;
            case WITHSHARE:
                actionbar_title = (TextView) findViewById(R.id.actionbar_with_share_title);
                break;
            default:
                break;
        }
        actionbar_title.setText(title);
    }

    /*
     * actionbar返回按钮操作
     */
    public void onBackClick(View v) {
        finish();
    }

    protected class OnBackClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }
    }

    /**
     * 弹出框可以手动取消
     *
     * @param title
     * @param message
     */
    protected void showProgressDialog(String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(this, title,
                    message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 弹出框不能手动取消
     *
     * @param title
     * @param message
     */
    public void showProgressDialogNoCancelable(String title, String message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, title,
                    message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    /*
       * 隐藏提示加载
       */
    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    /**
     * @param method
     * @param url
     * @param clazz
     * @param handler
     * @param MSG
     * @param progressType 1:showProgressDialog
     */
    protected void doRequestURL(int method, String url, final Class clazz, final Handler handler,
                                final int MSG, int progressType, final String errContent) {
        if (progressType == PROGRESS_CANCELABLE) {
            showProgressDialog("", MyConstants.LOADING);
        } else if (progressType == PROGRESS_NO_CANCELABLE) {
            showProgressDialogNoCancelable("", MyConstants.LOADING);
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        stringRequest = new StringRequest(method, builder.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i(clazz.getSimpleName(), "success");
                Bundle data = new Bundle();
                data.putString("response", s);
                Message message = Message.obtain(handler, MSG);
                message.setData(data);
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ExceptionUtil.printAndRecord(clazz.getSimpleName(), new Exception("volley: " + volleyError));
                Bundle data = new Bundle();
                data.putString("error", errContent);
                Message message = Message.obtain(handler, MSG_ERROR);
                message.setData(data);
                handler.sendMessage(message);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(MyConstants.REQUEST_LOAD_TIME,
                MyConstants.MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(clazz.getSimpleName());
        VolleyUtil.getRequestQueue().add(stringRequest);
    }

    protected void doUploadFile(int method, String url, final Class clazz, final Handler handler,
                                final int MSG, int progressType) {
        if (progressType == PROGRESS_CANCELABLE) {
            showProgressDialog("", MyConstants.LOADING);
        } else if (progressType == PROGRESS_NO_CANCELABLE) {
            showProgressDialogNoCancelable("", MyConstants.LOADING);
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        stringRequest = new StringRequest(method, builder.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i(clazz.getSimpleName(), "success");
                Bundle data = new Bundle();
                data.putString("response", s);
                Message message = Message.obtain(handler, MSG);
                message.setData(data);
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ExceptionUtil.printAndRecord(clazz.getSimpleName(), new Exception("volley: " + volleyError));
                Message message = Message.obtain(handler, MSG_ERROR);
                handler.sendMessage(message);
                hideProgressDialog();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(clazz.getSimpleName());
        VolleyUtil.getRequestQueue().add(stringRequest);
    }

    protected class OnJumpToBrowerClick implements View.OnClickListener {
        private String url;

        public OnJumpToBrowerClick(String u) {
            this.url = u;
        }

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    public abstract void initView();

    /**
     * @param mContext
     * @param clazz
     * @param datas
     */
    protected void JumpToActivity(Context mContext, Class clazz, Bundle datas) {
        Intent intent = new Intent(mContext, clazz);
        if (datas != null) {
            intent.putExtras(datas);
        }
        startActivity(intent);
    }

    /**
     * 跳转activity事件
     */
    protected class OnJumpToActivityClick implements View.OnClickListener {
        private Context mContext;
        private Class clazz;
        private Bundle datas;

        /**
         * @param c  current activity
         * @param cl target activity
         * @param d  data of this intent，if don't have any data, just set it "null"
         */
        public OnJumpToActivityClick(Context c, Class cl, Bundle d) {
            this.mContext = c;
            this.clazz = cl;
            this.datas = d;
        }

        @Override
        public void onClick(View v) {
            JumpToActivity(this.mContext, this.clazz, this.datas);
        }
    }

    /**
     * index_container界面执行退出，否则返回到上一个fragment
     */
    public void ExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            GlobalApplication.getInstance().exit();
        }
    }

    /**
     * 判断是否有用户登录
     *
     * @return
     */
    protected boolean isLogin() {
        return StringUtils.isNotBlank(MyPreferences.getAppPerenceAttribute(MyConstants
                .PREF_USER_ID)) ? true : false;
    }

    protected class OnShareContent implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
}
