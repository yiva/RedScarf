package com.redscarf.weidou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.redscarf.weidou.adapter.RedScarfBodyAdapter;
import com.redscarf.weidou.network.RequestType;
import com.redscarf.weidou.network.RequestURLFactory;
import com.redscarf.weidou.pojo.NonceBody;
import com.redscarf.weidou.pojo.RedScarfBody;
import com.redscarf.weidou.pojo.UserInfo;
import com.redscarf.weidou.util.GlobalApplication;
import com.redscarf.weidou.util.MyConstants;
import com.redscarf.weidou.util.MyPreferences;

import org.json.JSONException;

import cn.finalteam.toolsfinal.StringUtils;

/**
 * Created by yeahwang on 2015/10/9.
 */
public class RegisterActivity extends BaseActivity {

    private EditText edit_nickname;
    private EditText edit_email;
    private EditText edit_password;
    private Button btn_register;

    private String email;
    private String nickname;
    private String password;
    private String response;
    private NonceBody nonce;
    private UserInfo user_info;
    private ImageButton btn_back;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle indexObj = msg.getData();
            response = indexObj.getString("response");
            try {
            switch (msg.what) {
                case MSG_NONCE:
                    nonce = (NonceBody) RedScarfBodyAdapter.parseObj(response,
                            Class.forName("com.redscarf.weidou.pojo.NonceBody"));
                    register(nonce.getNonce());
                    break;
                case MSG_INDEX:
                    user_info = (UserInfo) RedScarfBodyAdapter.parseObj(response,
                            Class.forName("com.redscarf.weidou.pojo.UserInfo"));
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    if ("ok".equals(user_info.getStatus())) {
                        JumpToActivity(RegisterActivity.this, LoginActivity.class, null);
                        finish();
                    }
                    break;
                default:
                    break;
            }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally {
                hideProgressDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_register);
        GlobalApplication.getInstance().addActivity(this);
        this.initView();

    }

    @Override
    public void initView() {
        edit_nickname = (EditText) this.findViewById(R.id.edit_register_username);
        edit_email = (EditText) this.findViewById(R.id.edit_register_email);
        edit_password = (EditText) this.findViewById(R.id.edit_register_pwd);
        btn_register = (Button) this.findViewById(R.id.btn_register_submit);
        btn_back = (ImageButton) this.findViewById(R.id.btn_register_back);

        btn_register.setOnClickListener(new OnRegisterClick());
        btn_back.setOnClickListener(new OnBackClickListener());
    }

    private class OnRegisterClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            nickname = String.valueOf(edit_nickname.getText().toString().trim());
            email =String.valueOf(edit_email.getText().toString().trim());
            password = String.valueOf(edit_password.getText().toString().trim());
            if (nickname != null && email != null && password != null) {
                doRequestURL(Request.Method.GET, RequestURLFactory.sysRequestURL(RequestType
                                .NONCE_VALUE, new String[]{"user", "register"}),
                        RegisterActivity.class, handler, MSG_NONCE, PROGRESS_NO_CANCELABLE,"nonce");
            }
        }
    }


    private void register(String nonce) {
        doRequestURL(Request.Method.GET, RequestURLFactory.sysRequestURL(RequestType
                        .REGISTER, new String[]{email, email, nickname, nickname, password, nonce}),
                RegisterActivity.class, handler, MSG_INDEX, PROGRESS_NO_CANCELABLE,"register");
    }

}
