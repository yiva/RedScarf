package com.redscarf.weidou.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class SearchDetailActivity extends BaseActivity {

    private EditText search_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionbar_search_detail);
        initView();
    }

    private void initView() {
        search_content = (EditText) findViewById(R.id.edit_search_detail);

        search_content.addTextChangedListener(new OnSearchContentChangedListener());
    }

    private class OnSearchContentChangedListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            callChangedText();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            callChangedText();
        }

        @Override
        public void afterTextChanged(Editable s) {
            callChangedText();
        }
    }

    private boolean callChangedText(){
        String text = search_content.getText().toString();
        Toast.makeText(SearchDetailActivity.this, text, Toast.LENGTH_SHORT).show();
        return true;
    }
}
