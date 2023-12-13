package com.android.launcher.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.launcher.R;
import com.lxj.xpopup.core.CenterPopupView;

import module.common.utils.StringUtils;

/**
 * @date： 2023/11/15
 * @author: 78495
*/
public class HintMessageDialog extends CenterPopupView {

    private String message;

    public HintMessageDialog(Context context,String message) {
        super(context);
        this.message = message;
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        TextView contentTV = findViewById(R.id.contentTV);

        contentTV.setText(StringUtils.removeNull(message));

        TextView confirmTV = findViewById(R.id.confirmTV);
        confirmTV.setOnClickListener(v -> dismiss());
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_hint_message;
    }
}
