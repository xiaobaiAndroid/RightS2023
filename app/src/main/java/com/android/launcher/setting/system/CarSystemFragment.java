package com.android.launcher.setting.system;

import android.os.Bundle;
import android.view.View;

import module.common.MessageEvent;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class CarSystemFragment extends FragmentBase {

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_common_list;
    }
}
