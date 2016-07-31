package com.redscarf.weidou.activity.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.activity.WebActivity;
import com.redscarf.weidou.listener.BasePageLinstener;
import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.network.VolleyUtil;
import com.redscarf.weidou.util.MyPreferences;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * fragment父类
 */
public abstract class BaseFragment extends Fragment implements BasePageLinstener {

    protected final int PROGRESS_DISVISIBLE = 0;
    protected final int PROGRESS_NO_CANCLE = 1;
    protected final int PROGRESS_CANCLE = 2;

    private ProgressDialog progressDialog;
    private TextView actionbar_title;

    private StringRequest stringRequest;

    protected HashMap<String, String> url_map;

    protected View rootView;

    public static final int STATE_NONE = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_LOADMORE = 2;
    public static final int STATE_NOMORE = 3;
    public static final int STATE_PRESSNONE = 4;// 正在下拉但还没有到刷新的状态
    public static int mState = STATE_NONE;

    /**
     * 提示加载
     */
    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(getActivity(), title,
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
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    /**
     * 设置ActionBar的布局
     * 布局Id
     */
    protected void setActionBarLayout(String title, ActionBarType type) {

        switch (type) {
            case NORMAL:
                ImageButton btn_back = (ImageButton) rootView.findViewById(R.id
                        .actionbar_back);
                btn_back.setVisibility(View.GONE);
            case WITHBACK:
                actionbar_title = (TextView) rootView.findViewById(R.id.actionbar_title);
                break;
            case WITHSHARE:
                actionbar_title = (TextView) rootView.findViewById(R.id.actionbar_with_share_title);
                break;
            default:
                break;
        }
        actionbar_title.setText(title);
    }

    protected void setMarginTop(int paramInt) {
        RelativeLayout localRelativeLayout = (RelativeLayout) getActivity().findViewById(paramInt);
        RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
        localLayoutParams.setMargins(0, paramInt, 0, 0);
        localRelativeLayout.setLayoutParams(localLayoutParams);
        localRelativeLayout.invalidate();
    }


    public void showProgressDialogNoCancelable(String title, String message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(getActivity(), title,
                    message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }

        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * listView of PopupWindow wrap content
     *
     * @param listAdapter
     * @return
     */
    protected int measureContentWidth(ListAdapter listAdapter) {
        ViewGroup mMeasureParent = null;
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;

        final ListAdapter adapter = listAdapter;
        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }

            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(getActivity());
            }

            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);

            final int itemWidth = itemView.getMeasuredWidth();

            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }

        return maxWidth;
    }

    /**
     * @param url     http头
     * @param map     请求参数
     * @param clazz   调用Class
     * @param handler 消息对象
     * @param MSG     消息标识
     */
    public void doRequestURL(String url, HashMap<String, String> map, final Class clazz, final Handler handler, final int MSG) {
        showProgressDialog("", MyConstants.LOADING);
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
        }
        stringRequest = new StringRequest(StringRequest.Method.GET, builder.toString(), new Response.Listener<String>() {
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
                Log.e(clazz.getSimpleName(), "error", volleyError);
                hideProgressDialog();
            }
        });
        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(MyConstants.REQUEST_LOAD_TIME,
                MyConstants.MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(clazz.getSimpleName());
        VolleyUtil.getRequestQueue().add(stringRequest);
    }

    public void doRequestURL(String url, final Class clazz, final Handler handler, final int MSG) {
        showProgressDialog("", MyConstants.LOADING);
        stringRequest = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
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
                Log.e(clazz.getSimpleName(), "error", volleyError);
            }
        });
        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(MyConstants.REQUEST_LOAD_TIME,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(clazz.getSimpleName());
        VolleyUtil.getRequestQueue().add(stringRequest);
    }

    /**
     * @param method
     * @param url
     * @param clazz
     * @param handler
     * @param MSG
     * @param progressType 1:showProgressDialog 2:showProgressDialogNoCancelable
     */
    protected void doRequestURL(int method, String url, final Class clazz, final Handler handler,
                                final int MSG, final int progressType) {
        if (progressType == PROGRESS_CANCLE) {
            showProgressDialog("", MyConstants.LOADING);
        } else if (progressType == PROGRESS_NO_CANCLE) {
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
                Log.e(clazz.getSimpleName(), "error", volleyError);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(MyConstants.REQUEST_LOAD_TIME,
                 DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(clazz.getSimpleName());
        VolleyUtil.getRequestQueue().add(stringRequest);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Fragment fragment = getParentFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
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
}
