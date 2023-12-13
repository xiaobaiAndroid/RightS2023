package com.android.launcher.can.receiver;

import android.content.Context;

import module.common.MessageEvent;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.lin.entity.Lin30Entity;

import module.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
* @description:  时钟
* @createDate: 2023/7/24
*/
public class Can39FReceiver extends CanReceiverBase{

    public Can39FReceiver(Context context) {
        super(context);
    }

    @Override
    protected void disposeData(String data) {

        LogUtils.printI(TAG, "data="+data);

        String d1 = data.substring(0, 2);
        String d2 = data.substring(2, 4);
        String d3 = data.substring(4, 6);


        Lin30Entity lin30Entity = new Lin30Entity();
        lin30Entity.setD2(new BinaryEntity(d1));
        lin30Entity.setD3(new BinaryEntity(d3));
        lin30Entity.setD4(new BinaryEntity(d2));

        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CAN39F_TO_LIN30);
        messageEvent.data = lin30Entity;
        EventBus.getDefault().post(messageEvent);
    }
}