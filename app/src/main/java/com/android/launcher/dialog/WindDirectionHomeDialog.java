package com.android.launcher.dialog;

import android.content.Context;

import com.android.launcher.R;
import com.lxj.xpopup.core.PositionPopupView;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public class WindDirectionHomeDialog extends PositionPopupView {

    public WindDirectionHomeDialog(Context context) {
        super(context);
    }


    @Override
    protected void onCreate() {
        super.onCreate();
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_wind_direction_home;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
