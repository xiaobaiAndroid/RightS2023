package com.android.launcher.setting.system.reset;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import module.common.MessageEvent;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import module.common.utils.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

/**
 * @date： 2023/10/19
 * @author: 78495
*/
public class ResetHomeFragment extends FragmentBase {

    private BasePopupView loadingDialog;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        TextView startTV = view.findViewById(R.id.startTV);
        startTV.setOnClickListener(v -> {
            showResetDialog();
        });
    }

    private void showResetDialog() {
        loadingDialog = new XPopup.Builder(getActivity())
                .isViewMode(true)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .asLoading(getResources().getString(R.string.resetting))
                .show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if(loadingDialog!=null){
                loadingDialog.dismiss();
            }
            ToastUtils.showLong(getContext(),getResources().getString(R.string.reset_complete));
        },2000);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_reset;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(loadingDialog!=null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}
