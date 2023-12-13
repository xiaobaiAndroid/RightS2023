package com.android.launcher.setting.system.audio;

import android.content.Context;

import com.android.launcher.util.BigDecimalUtils;
import com.android.launcher.R;
import com.android.launcher.common.CommonSetDialog;
import com.android.launcher.util.AudioUtils;

import module.common.utils.LogUtils;
import module.common.utils.SPUtils;
import com.android.launcher.util.SendcanCD;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class VolumeSetDialog extends CommonSetDialog {

    public VolumeSetDialog(Context context) {
        super(context);
    }

    @Override
    protected void initData() {

        int maxVolume = AudioUtils.getMediaMaxVolume(getContext());
        ratio = BigDecimalUtils.div(String.valueOf(maxVolume), String.valueOf(progressMax), 2);
        float savedValue = SPUtils.getInt(getContext(), SPUtils.SP_MUSIC_VOLUME, maxVolume/2);
        currentProgress = (int) BigDecimalUtils.div(String.valueOf(savedValue), String.valueOf(ratio), 2);

        LogUtils.printI(TAG, "ratio=" + ratio + ", savedValue=" + savedValue + ", currentProgress=" + currentProgress);
        seekBar.setProgress(currentProgress);
    }



    @Override
    protected void updateData(int value, boolean isIncrease) {
        setVolume(value,isIncrease);
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.volume_modify);
    }

    @Override
    protected int setMaxProgress() {
        return AudioUtils.getMediaMaxVolume(getContext());
    }

    private void setVolume(int volume, boolean isIncrease) {
        mExecutor.execute(() -> {
            try {
                if(isIncrease){ //音量增大
                    SendcanCD.handler("AA000002000000FA8000000000000000");
                }else{ //音量减小
                    SendcanCD.handler("AA000002000000FA7E00000000000000");
                }

                LogUtils.printI(VolumeSetDialog.class.getSimpleName(), "setVolume---volume="+volume + ",isIncrease="+isIncrease);
                SPUtils.putInt(getContext(), SPUtils.SP_MUSIC_VOLUME,volume);
                AudioUtils.setMediaVolume(getContext(),volume,false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
