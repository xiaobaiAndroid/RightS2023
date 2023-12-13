package com.android.launcher.common;

import android.content.Context;
import android.widget.TextView;

import com.android.launcher.setting.system.audio.VolumeSetDialog;
import com.android.launcher.util.BigDecimalUtils;
import com.android.launcher.R;

import module.common.utils.LogUtils;

import com.lxj.xpopup.core.CenterPopupView;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @date： 2023/10/19
 * @author: 78495
 */
public abstract class CommonSetDialog extends CenterPopupView {

    protected String TAG;

    protected int progressMax = 100;


    // 创建一个根据需要创建新线程的线程池，可扩容
    protected ExecutorService mExecutor;

    protected float ratio;
    protected int currentProgress;
    protected IndicatorSeekBar seekBar;
    private TextView titleTV;

    public CommonSetDialog(Context context) {
        super(context);
        mExecutor = Executors.newCachedThreadPool();
    }

    @Override
    protected final void onCreate() {
        super.onCreate();

        TAG = getClass().getSimpleName();

        titleTV = findViewById(R.id.titleTV);
        titleTV.setText(getTitle());
        seekBar = findViewById(R.id.seekBar);

        progressMax = setMaxProgress();
        seekBar.setMax(progressMax);

        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                int progress = seekParams.progress;
                if (currentProgress == progress) {
                    return;
                }

                boolean isIncrease = false;
                if (progress > currentProgress) {
                    isIncrease = true;
                }
                currentProgress = progress;

                int value = (int) BigDecimalUtils.mul(String.valueOf(progress),String.valueOf(ratio));
                LogUtils.printI(VolumeSetDialog.class.getSimpleName(), "onProgressChanged---progress=" + progress + ",volume=" + value);

                updateData(value, isIncrease);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

        initData();
    }

    protected abstract void initData();

    protected int setMaxProgress() {
        return 100;
    }


    protected abstract void updateData(int value, boolean isIncrease);

    protected abstract String getTitle();

    @Override
    protected final int getImplLayoutId() {
        return R.layout.dialog_set_common;
    }

}
