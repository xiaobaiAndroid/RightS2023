package com.android.launcher.can.receiver;

import android.content.Context;

/**
* @description:  照明灯状态
* @createDate: 2023/7/24
*/
public class Can025Receiver extends CanReceiverBase{

    public Can025Receiver(Context context) {
        super(context);
    }

    @Override
    protected void disposeData(String data) {
        //07 00 04 00 00 00 00
        taskExecutor.execute(() -> {
            if (data == null || data.length() != 14) {
                return;
            }
            String d1 = data.substring(0, 2);
            String d2 = data.substring(2, 4);
            String d3 = data.substring(4, 6);

        });
    }
}