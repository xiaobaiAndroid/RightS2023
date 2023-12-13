package com.android.launcher.setting.system.audio;

import android.content.Context;

import com.android.launcher.common.CommonSetDialog;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.util.BigDecimalUtils;
import com.android.launcher.R;

import module.common.utils.LogUtils;
import module.common.utils.SPUtils;

/**
 * 警报音量设置
 *
 * @date： 2023/10/18
 * @author: 78495
 */
public class AlarmSetDialog extends CommonSetDialog {

    public AlarmSetDialog(Context context) {
        super(context);
    }

    @Override
    protected void initData() {
        int maxVolume;
//        if(App.isCompleteBody){
        maxVolume = 30;
//        }else{
//            maxVolume = AudioUtils.getMediaMaxVolume(getContext());
//        }

        ratio = BigDecimalUtils.div(String.valueOf(maxVolume), String.valueOf(progressMax), 2);
        float savedValue = SPUtils.getInt(getContext(), SPUtils.SP_CAR_ALARM_VOLUME, maxVolume / 2);
        currentProgress = (int) BigDecimalUtils.div(String.valueOf(savedValue), String.valueOf(ratio), 2);

        LogUtils.printI(TAG, "ratio=" + ratio + ", savedValue=" + savedValue + ", currentProgress=" + currentProgress);
        seekBar.setProgress(currentProgress);
    }


    @Override
    protected void updateData(int value, boolean isIncrease) {
        setVolume(value, isIncrease);
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.alarm_volume_setting);
    }

    @Override
    protected int setMaxProgress() {
        return 30;
    }

    private void setVolume(int volume, boolean isIncrease) {
        mExecutor.execute(() -> {
            try {
                SPUtils.putInt(getContext(), SPUtils.SP_CAR_ALARM_VOLUME, volume);

                String volumeStr = String.valueOf(volume);
                if (volume < 10) {
                    volumeStr = "0" + volume;
                }
                String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.ALARM_SET.getTypeValue() + volumeStr + SerialPortDataFlag.END_FLAG;
                SendHelperLeft.handler(send);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
