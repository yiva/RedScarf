package com.redscarf.weidou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.redscarf.weidou.util.ActionBarType;
import com.redscarf.weidou.util.GlobalApplication;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by yeahwang on 2016/5/14.
 */
public class AboutActivity extends BaseActivity{

    private TextView version;
    private TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);
        initView();
    }

    @Override
    public void initView() {
        setActionBarLayout(getResources().getString(R.string.title_about), ActionBarType.WITHBACK);
        version = (TextView) findViewById(R.id.txt_about_version);
        email = (TextView) findViewById(R.id.txt_about_email_address);

        String str_version = GlobalApplication.getVersion();
        if (StringUtils.isNotBlank(str_version)) {
            version.setText(str_version);
        }

        email.setOnClickListener(new OnSendEmailClick());
    }

    private class OnSendEmailClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            // 必须明确使用mailto前缀来修饰邮件地址,如果使用

            // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
//            startActivity(Intent.createChooser(intent, "请选择邮件类应用"));

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "");//主题
            intent.putExtra(Intent.EXTRA_TEXT, "");//正文
            intent.setData(Uri.parse("mailto:"+email.getText().toString()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
