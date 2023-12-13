package com.android.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import module.common.utils.LogUtils;

/**
 * @date： 2023/11/11
 * @author: 78495
*/
public class HomeKeyWatcherRecevier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra("reason");

            LogUtils.printI(HomeKeyWatcherRecevier.class.getSimpleName(), "reason="+reason);
            if (reason != null) {
                if (reason.equals("homekey")) {
                    // 处理 Home 键被按下的逻辑
                }
            }
        }
    }

}
