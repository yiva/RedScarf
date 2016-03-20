package com.redscarf.weidou.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.redscarf.weidou.activity.fragment.IndividualModifyFragment;

/**
 * Created by yeahwang on 2015/11/4.
 */
public class ModifyActivity extends BaseActivity {

//    private Bundle datas;
    private static final String MODIFY_CONTAINER = IndividualModifyFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        FragmentTransaction transaction = basicFragment.beginTransaction();
        transaction.add(R.id.modifyFragment, new IndividualModifyFragment(), MODIFY_CONTAINER);
        transaction.addToBackStack(MODIFY_CONTAINER);
        transaction.commit();

    }

    @Override
    public void initView() {

    }

}
