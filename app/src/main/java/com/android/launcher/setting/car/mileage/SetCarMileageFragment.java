package com.android.launcher.setting.car.mileage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import module.common.MessageEvent;

import com.android.launcher.base.FragmentBase;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.view.GridSpaceDecoration;
import com.android.launcher.R;

import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;
import module.common.utils.ToastUtils;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置原车公里数
 *
 * @date： 2023/10/18
 * @author: 78495
 */
public class SetCarMileageFragment extends FragmentBase {


    private TextView contentTV;
    private BasePopupView loadingView;
    private BasePopupView inputDialog;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        contentTV = view.findViewById(R.id.contentTV);
        TextView confirmBt = view.findViewById(R.id.setBt);

        confirmBt.setOnClickListener(v -> {
            inputDialog = new XPopup.Builder(getActivity()).isViewMode(true)
                    .hasShadowBg(true)
                    .hasStatusBar(false)
                    .borderRadius(getResources().getDimension(R.dimen.dp_6))
                    .asCustom(new NumberInputView(getActivity(), mileage -> {
                        setCarMileage(mileage);
                        if(inputDialog!=null){
                            inputDialog.dismiss();
                            inputDialog = null;
                        }
                    })).show();

        });
    }

    private void setCarMileage(String mileage) {
        loadingView = new XPopup.Builder(getActivity())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .isViewMode(true)
                .asLoading()
                .show();
        mExecutor.execute(() -> {
            int dotIndex = mileage.indexOf(".");
            final String send;
            if (dotIndex == -1) { //只有整数
                send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.CAR_MILEAGE.getTypeValue() + mileage + SerialPortDataFlag.END_FLAG;
            } else {
                String integerPart = mileage.substring(0, dotIndex);
                String decimalPart = mileage.substring(dotIndex + 1);
                //用E表示小数点
                send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.CAR_MILEAGE.getTypeValue() + integerPart + "E" + decimalPart + SerialPortDataFlag.END_FLAG;
            }
            SendHelperLeft.handler(send);

        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (loadingView != null) {
                loadingView.dismiss();
            }
            contentTV.setText("");
        }, 2000);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_set_car_mileage;
    }


    @Override
    protected void setupData() {
        super.setupData();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadingView != null) {
            loadingView.dismiss();
            loadingView = null;
        }
        if (inputDialog != null) {
            inputDialog.dismiss();
            inputDialog = null;
        }
    }
}
