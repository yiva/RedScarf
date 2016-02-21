package com.redscarf.weidou.activity.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.redscarf.weidou.activity.R;
import com.redscarf.weidou.util.ExceptionUtil;
import com.redscarf.weidou.util.MyPreferences;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 个人资料修改界面
 */
public class IndividualModifyFragment extends BaseFragment {

    private static final String TAG = IndividualModifyFragment.class.getSimpleName();
    private Bundle datas;
    private View rootView;

    private TextView txt_title;
    private EditText edit_modify;
    private TextView txt_submit;
    private int MSG_INDEX = 1;
    private String response;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_INDEX) {
                Bundle indexObj = msg.getData();
                response = indexObj.getString("response");
                if (validResponse(response)) {
                    Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "系统错误", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_individual_modify, container, false);
        datas = getActivity().getIntent().getExtras();
        registerButton();
        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 注册控件
     */
    private void registerButton() {
        txt_title = (TextView) getActivity().findViewById(R.id.title_individual_modify);
        edit_modify = (EditText) rootView.findViewById(R.id.edit_modify);
        txt_submit = (TextView) getActivity().findViewById(R.id.txt_individual_modify_submit);

        txt_title.setText(datas.getString("title"));
        edit_modify.setHint(datas.getString("value"));
        txt_submit.setOnClickListener(new OnUpdateUserMeta());
    }

    /**
     * 提交事件
     */
    private class OnUpdateUserMeta implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            url_map.put("cookie", MyPreferences.getAppPerenceAttribute("user_cookie"));
            url_map.put("meta_key", datas.getString("meta_key"));
            url_map.put("meta_value", edit_modify.getText().toString());
            doRequestURL(RequestURLFactory.sysRequestURL(RequestType.MODIFY_INDIVIDUAL,
                            new String[]{datas.getString("meta_key"), edit_modify.getText().toString()}),
                    IndividualModifyFragment.class, handler, MSG_INDEX);
        }
    }

    /**
     * 验证结果
     *
     * @param response
     * @return
     */
    private Boolean validResponse(String response) {
        boolean flag = false;
        try {
            JSONObject jo = new JSONObject(response);
            if (jo.getString("status").equals("ok")) {
                flag = true;
            }
        } catch (JSONException e) {
            ExceptionUtil.printAndRecord(TAG, e);
            flag = false;
        }
        return flag;
    }


}
