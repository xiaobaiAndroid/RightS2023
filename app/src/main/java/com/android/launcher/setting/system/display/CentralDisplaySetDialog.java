package com.android.launcher.setting.system.display;

import android.content.Context;

import com.android.launcher.R;
import com.android.launcher.common.CommonSetDialog;
import module.common.utils.AppUtils;
import com.android.launcher.util.BigDecimalUtils;
import com.android.launcher.util.SystemUtils;
import com.bzf.module_db.entity.CarSetupTable;
import com.bzf.module_db.repository.CarSetupRepository;

/**
 * @date： 2023/10/18
 * @author: 78495
 */
public class CentralDisplaySetDialog extends CommonSetDialog {


    public CentralDisplaySetDialog(Context context) {
        super(context);
    }

    @Override
    protected void initData() {
        ratio = BigDecimalUtils.div("255", "100", 2);
        mExecutor.execute(() -> {
            CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), AppUtils.getDeviceId(getContext()));
            if (carSetupTable != null) {
                final int centralDisplayLum = carSetupTable.getCentralDisplayLum();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        currentProgress = (int) BigDecimalUtils.div(String.valueOf(centralDisplayLum), String.valueOf(ratio), 0);
                        if (seekBar != null) {
                            seekBar.setProgress(currentProgress);
                        }
                        SystemUtils.setBrightness(getContext(), centralDisplayLum);
                    });
                }
            }
        });
    }

    @Override
    protected void updateData(int value, boolean isIncrease) {
        mExecutor.execute(() -> {
            CarSetupTable carSetupTable = CarSetupRepository.getInstance().getData(getContext(), AppUtils.getDeviceId(getContext()));
            if (carSetupTable != null) {
                carSetupTable.setCentralDisplayLum(value);
                CarSetupRepository.getInstance().updateData(getContext(), carSetupTable);
            }
        });

        SystemUtils.setBrightness(getContext(), value);
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.central_display_luminance);
    }

}
